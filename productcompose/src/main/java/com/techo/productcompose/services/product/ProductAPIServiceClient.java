package com.techo.productcompose.services.product;

import com.techo.productcompose.model.Product;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

import static com.techo.productcompose.services.product.ProductServiceUrlProvider.REST_SERVICE_NAME;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@FeignClient(name = REST_SERVICE_NAME)
public interface ProductAPIServiceClient {

    @RequestMapping(method = GET, value = "/products", consumes = APPLICATION_JSON_UTF8_VALUE)
    List<Product> getAllProducts();

    @RequestMapping(method = GET, value = "/products/{id}", consumes = APPLICATION_JSON_UTF8_VALUE)
    ResponseEntity<Product> getProduct(@PathVariable("id") Long id);

    @RequestMapping(method = POST, value = "/products", consumes = APPLICATION_JSON_UTF8_VALUE)
    ResponseEntity<Product> createProduct(Product product);
}
