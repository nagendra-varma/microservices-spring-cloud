package com.techo.reviewservice.service;

import com.techo.reviewservice.model.Review;
import com.techo.reviewservice.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    public Review save(Review review) {
        return reviewRepository.save(review);
    }

    public Review findById(Long reviewId) {
        return reviewRepository.findOne(reviewId);
    }

    public List<Review> findAll() {
        return (List<Review>) reviewRepository.findAll();
    }

    public List<Review> findByProductId(Long productId) {
        return reviewRepository.findByProductId(productId);
    }
}
