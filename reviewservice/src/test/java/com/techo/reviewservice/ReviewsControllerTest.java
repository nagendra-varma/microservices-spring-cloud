package com.techo.reviewservice;

import com.techo.reviewservice.model.Review;
import com.techo.reviewservice.service.ReviewService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import java.util.Arrays;

import static org.apache.commons.lang.builder.EqualsBuilder.reflectionEquals;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ReviewsApplication.class)
@WebAppConfiguration
@DirtiesContext(classMode = BEFORE_EACH_TEST_METHOD)
public class ReviewsControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private ReviewService reviewService;

    @Before
    public void setup() {
        mockMvc = webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void shouldCreateReview() throws Exception {
        Review review = getReview();
        mockMvc.perform(post("/reviews").contentType(APPLICATION_JSON)
                .content(review.toJson()))
                .andExpect(status().isCreated())
                .andExpect(content().json(review.toJson()));
        reflectionEquals(review, reviewService.findById(review.getReviewId()));
    }

    @Test
    public void shouldReturnAllReviews() throws Exception {
        Review review = insertReview();
        mockMvc.perform(get("/reviews")
                .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
        .andExpect(content().json(Arrays.toString(new Review[]{review})));
    }

    @Test
    public void shouldReturnFilteredReviews() throws Exception {
        Review review1 = insertReview();
        Review review2 = insertReview(2L, 2L);
        mockMvc.perform(get("/reviews?productId=2")
                .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(Arrays.toString(new Review[]{review2})));
    }

    @Test
    public void shouldReturnTheRequestedReview() throws Exception {
        Review review = insertReview();
        mockMvc.perform(get("/reviews/1")
                .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(review.toJson()));
    }

    @Test
    public void shouldReturnNotFoundIfRequestReviewNotFound() throws Exception {
        mockMvc.perform(get("/reviews/1")
                .contentType(APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldNotCreateReviewIfParamsAreInvalid() throws Exception {
        Review review = getReview();
        review.setAuthor(null);
        mockMvc.perform(post("/reviews")
                .contentType(APPLICATION_JSON)
                .content(review.toJson()))
                .andExpect(status().isBadRequest());

        review = getReview();
        review.setSubject(null);
        mockMvc.perform(post("/reviews")
                .contentType(APPLICATION_JSON)
                .content(review.toJson()))
                .andExpect(status().isBadRequest());
    }

    public Review getReview() {
        Review review = new Review();
        review.setAuthor("Author1");
        review.setReviewId(1L);
        review.setSubject("Test subject-1");
        review.setProductId(1L);
        return review;
    }

    private Review insertReview(Long productId, Long reviewId) {
        Review review = getReview();
        review.setProductId(productId);
        review.setReviewId(reviewId);
        reviewService.save(review);
        return review;
    }

    private Review insertReview() {
        Review review = getReview();
        reviewService.save(review);
        return review;
    }
}