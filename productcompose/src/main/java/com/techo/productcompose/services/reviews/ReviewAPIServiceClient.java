package com.techo.productcompose.services.reviews;

import com.techo.productcompose.model.Review;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.websocket.server.PathParam;
import java.util.List;

import static com.techo.productcompose.services.reviews.ReviewServiceUrlProvider.REST_SERVICE_NAME;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;


@FeignClient(name = REST_SERVICE_NAME)
public interface ReviewAPIServiceClient {

    @RequestMapping(method = GET, value = "/reviews", consumes = APPLICATION_JSON_UTF8_VALUE)
    List<Review> getReviews(@RequestParam("productId") Long productId);

    @RequestMapping(method = GET, value = "/reviews", consumes = APPLICATION_JSON_UTF8_VALUE)
    List<Review> getAllReviews();

    @RequestMapping(method = POST, value = "/reviews", consumes = APPLICATION_JSON_UTF8_VALUE)
    ResponseEntity<Review> addReview(Review review);
}
