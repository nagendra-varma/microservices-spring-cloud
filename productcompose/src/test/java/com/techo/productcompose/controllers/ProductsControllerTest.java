package com.techo.productcompose.controllers;


import com.techo.productcompose.ProductComposeApplication;
import com.techo.productcompose.model.Product;
import com.techo.productcompose.model.Review;
import com.techo.productcompose.services.product.ProductAPIService;
import com.techo.productcompose.services.reviews.ReviewAPIService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.context.WebApplicationContext;

import java.util.Arrays;

import static org.apache.commons.lang.builder.EqualsBuilder.reflectionEquals;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.ResponseEntity.notFound;
import static org.springframework.http.ResponseEntity.ok;
import static org.springframework.http.ResponseEntity.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ProductComposeApplication.class)
@WebAppConfiguration
public class ProductsControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @MockBean
    private ProductAPIService productAPIService;

    @MockBean
    private ReviewAPIService reviewAPIService;

    private MockMvc mockMvc;

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void shouldCreateProduct() throws Exception {
        Product product = getProductWithReviews();
        when(productAPIService.createProduct(any(Product.class))).thenReturn(ok(product));
        mockMvc.perform(post("/products")
                .contentType(APPLICATION_JSON)
                .content(product.toJson()))
                .andExpect(status().isOk())
                .andExpect(content().json(product.toJson()));
    }

    @Test
    public void shouldForwardTheSameErrorFromAPIServiceToClient() throws Exception {
        Product product = getProductWithReviews();
        product.setName(null);
        when(productAPIService.createProduct(any(Product.class)))
                .thenThrow(new HttpClientErrorException(BAD_REQUEST, "Product name should not be null"));
        mockMvc.perform(post("/products")
                .contentType(APPLICATION_JSON)
                .content(product.toJson()))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldReturnAllProductsWithReviews() throws Exception {
        Product product = getProductWithReviews();
        when(productAPIService.getAllProducts()).thenReturn(Arrays.asList(product));
        mockMvc.perform(get("/products")
                .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(Arrays.toString(new Product[]{product})));
    }

    @Test
    public void shouldReturnTheRequestProductWithReviews() throws Exception {
        Product product = getProductWithReviews();
        when(productAPIService.getProduct(1L)).thenReturn(ok(product));
        when(reviewAPIService.getReviews(1L)).thenReturn(product.getReviews());
        mockMvc.perform(get("/products/1")
                .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(product.toJson()));
    }

    @Test
    public void shouldAddReviewsToProducts() throws Exception {
        Review review = getReviewOne();
        review.setProductId(1L);
        ResponseEntity<Review> responseEntity = ResponseEntity.ok(review);
        when(reviewAPIService.addReview(review)).thenReturn(responseEntity);
        when(productAPIService.getProduct(1L)).thenReturn(ok(mock(Product.class)));
        mockMvc.perform(post("/products/1/reviews")
                .contentType(APPLICATION_JSON)
                .content(review.toJson()))
                .andExpect(status().isOk());
        ArgumentCaptor<Review> argumentCaptor = ArgumentCaptor.forClass(Review.class);
        verify(reviewAPIService).addReview(argumentCaptor.capture());
        reflectionEquals(argumentCaptor.getValue(), review);
    }

    @Test
    public void shouldReturnNotFoundIfAddingReviewToUnavailableProduct() throws Exception {
        Review review = getReviewOne();
        review.setProductId(1L);
        ResponseEntity<Review> responseEntity = ResponseEntity.ok(review);
        when(reviewAPIService.addReview(review)).thenReturn(responseEntity);
        ResponseEntity<Product> notFound = notFound().build();
        when(productAPIService.getProduct(1L)).thenReturn(notFound);
        mockMvc.perform(post("/products/1/reviews")
                .contentType(APPLICATION_JSON)
                .content(review.toJson()))
                .andExpect(status().isNotFound());
        verifyNoMoreInteractions(reviewAPIService);
    }

    private Product getProductWithReviews() {
        Product product = new Product();
        product.setId(1L);
        product.setName("TestProduct");
        Review review1 = getReviewOne();
        review1.setProductId(1L);
        Review review2 = getReviewTwo();
        review2.setProductId(2L);
        product.setReviews(Arrays.asList(review1, review2));
        return product;
    }

    private Review getReviewOne() {
        Review review = new Review();
        review.setReviewId(1L);
        review.setAuthor("Review One");
        return review;
    }

    private Review getReviewTwo() {
        Review review = new Review();
        review.setReviewId(1L);
        review.setAuthor("Review two");
        return review;
    }
}