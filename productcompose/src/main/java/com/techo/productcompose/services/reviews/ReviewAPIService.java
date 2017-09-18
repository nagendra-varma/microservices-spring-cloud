package com.techo.productcompose.services.reviews;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.techo.productcompose.model.Review;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.springframework.http.HttpMethod.GET;

@Service
public class ReviewAPIService {

    @LoadBalanced
    private RestTemplate restTemplate = new RestTemplate();

    @Autowired
    private ReviewServiceUrlProvider urlProvider;

    public List<Review> getReviews(Long id) {
        return restTemplate.exchange(urlProvider.getReviewsByProductIdUrl(id), GET, null,
                new ParameterizedTypeReference<List<Review>>(){}).getBody();
//        String response = restTemplate.getForEntity(urlProvider.getReviewsByProductIdUrl(id), String.class);
//        ObjectMapper mapper = new ObjectMapper();
//        return mapper.convertValue(response, new TypeReference<List<Review>>() {});
    }

    public List<Review> getAllReviews() {
        return restTemplate.exchange(urlProvider.getReviews(), GET, null,
                new ParameterizedTypeReference<List<Review>>(){}).getBody();
    }

    public ResponseEntity<Review> addReview(Review review) {
        return restTemplate.postForEntity(urlProvider.getReviews(), review, Review.class);
    }
}
