package com.techo.productcompose.services;

import com.techo.productcompose.exceptions.ServerUnSuccessfulResponseException;
import com.techo.productcompose.model.Product;
import com.techo.productcompose.model.Review;
import com.techo.productcompose.services.product.ProductAPIService;
import com.techo.productcompose.services.reviews.ReviewAPIService;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import static com.techo.productcompose.helpers.ServerResponseParseHelper.ensureSuccessResponse;
import static com.techo.productcompose.helpers.ServerResponseParseHelper.isSuccess;
import static org.slf4j.LoggerFactory.getLogger;

@Service
public class ProductComposeService {

    private static final Logger LOG = getLogger(ProductComposeService.class);

    @Autowired
    private ProductAPIService productAPIService;

    @Autowired
    private ReviewAPIService reviewAPIService;

    public List<Product> getAllProducts() {
        List<Product> products = productAPIService.getAllProducts();
        List<Review> reviews = reviewAPIService.getAllReviews();
        return mergeProductsAndReviews(products, reviews);
    }

    private List<Product> mergeProductsAndReviews(List<Product> products, List<Review> reviews) {
        products.forEach((product) -> {
            List<Review> reviewList = reviews.stream()
                .filter((review -> review.getProductId() == product.getId()))
                .collect(Collectors.toList());
            product.setReviews(reviewList);
        });
        return products;
    }

    public Product getProductWithReview(Long id) {
        ResponseEntity<Product> productEntity = productAPIService.getProduct(id);
        ensureSuccessResponse(productEntity);
        Product product = productEntity.getBody();
        List<Review> reviewList = reviewAPIService.getReviews(product.getId());
        product.setReviews(reviewList);
        return product;
    }

    public ResponseEntity<Product> getProduct(Long id) {
        return productAPIService.getProduct(id);
    }

    public Product createProduct(Product product) {
        ResponseEntity productResponseEntity = productAPIService.createProduct(product);
        if (isSuccess(productResponseEntity)) {
            try {
                return Product.fromJson(productResponseEntity.getBody().toString());
            } catch (IOException e) {
                LOG.error("Error while converting from json to product, body - " + productResponseEntity.getBody());
            }
        }
        throw new ServerUnSuccessfulResponseException(productResponseEntity.getStatusCode(),
                productResponseEntity.getBody().toString());

    }

    public ResponseEntity<Review> addReview(Review review) {
        return reviewAPIService.addReview(review);
    }
}
