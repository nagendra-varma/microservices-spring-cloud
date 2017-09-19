package com.techo.productcompose.controllers;

import com.google.common.annotations.VisibleForTesting;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.techo.productcompose.exceptions.ProductNotExistsException;
import com.techo.productcompose.exceptions.ServerUnSuccessfulResponseException;
import com.techo.productcompose.model.Product;
import com.techo.productcompose.model.Review;
import com.techo.productcompose.services.ProductComposeService;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.techo.productcompose.helpers.ServerResponseParseHelper.isSuccess;
import static org.slf4j.LoggerFactory.getLogger;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.ResponseEntity.ok;
import static org.springframework.http.ResponseEntity.status;

@RestController
@RequestMapping("/products")
public class ProductsController {

    private static final Logger LOG = getLogger(ProductsController.class);

    @Autowired
    private ProductComposeService productComposeService;

    @HystrixCommand(commandKey = "productcompose", fallbackMethod = "getAllProductsFallback")
    @GetMapping
    public ResponseEntity getAllProducts() {
        return productComposeService.getAllProducts();
    }

    public ResponseEntity getAllProductsFallback() {
        return status(INTERNAL_SERVER_ERROR).body("Some of the services are unavailable");
    }

    @PostMapping
    public Product createProduct(@RequestBody Product product) {
        return productComposeService.createProduct(product);
    }

    @PostMapping("/{id}/reviews")
    public ResponseEntity<Review> addReview(@PathVariable("id") Long productId, @RequestBody Review review) {
        ensureProductExists(productId);
        review.setProductId(productId);
        return productComposeService.addReview(review);
    }

    @HystrixCommand(commandKey = "productcompose", fallbackMethod = "getProductFallback")
    @GetMapping("/{id}")
    public ResponseEntity getProduct(@PathVariable("id") Long id) {
        return ok(productComposeService.getProductWithReview(id));
    }

    public ResponseEntity getProductFallback(@PathVariable("id") Long id) {
        return status(INTERNAL_SERVER_ERROR).body("Some of the services are unavailable");
    }

    private void ensureProductExists(Long id) {
        ResponseEntity<Product> productResponseEntity = productComposeService.getProduct(id);
        if (!isSuccess(productResponseEntity)) {
            throw new ProductNotExistsException("Product with id - " + id + " not found.");
        }
    }

    @ExceptionHandler(HttpClientErrorException.class)
    @VisibleForTesting
    public void httpClientErrorExceptionHandler(HttpClientErrorException e, HttpServletResponse response) throws IOException {
        response.sendError(HttpStatus.BAD_REQUEST.value(), e.getResponseBodyAsString());
    }

    @ExceptionHandler(ServerUnSuccessfulResponseException.class)
    public void serverUnsuccessfulResponseException(ServerUnSuccessfulResponseException e,
                                                    HttpServletResponse response) throws IOException {
        response.sendError(e.getHttpStatus().value(), e.getMessage());
    }
}
