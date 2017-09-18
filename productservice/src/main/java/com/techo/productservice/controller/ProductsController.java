package com.techo.productservice.controller;

import com.techo.productservice.model.Product;
import com.techo.productservice.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.ResponseEntity.notFound;
import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/products")
public class ProductsController {

    @Autowired
    private ProductService productService;

    @PostMapping
    @ResponseStatus(CREATED)
    public Product createProduct(@Valid @RequestBody Product product) {
        return productService.save(product);
    }

    @GetMapping("/{id}")
    public ResponseEntity getProduct(@PathVariable("id") Long id) {
        Product product = productService.findById(id);
        if (product == null) {
            return notFound().build();
        }
        return ok(product);
    }

    @GetMapping
    @ResponseStatus(OK)
    public List<Product> getProducts() {
        return productService.findAll();
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public void constraintViolationExceptionHandler(ConstraintViolationException e,
                                             HttpServletResponse response) throws IOException {
        response.sendError(BAD_REQUEST.value(), e.getMessage());
    }
}
