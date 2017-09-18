package com.techo.reviewservice.controller;

import com.techo.reviewservice.model.Review;
import com.techo.reviewservice.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.ResponseEntity.notFound;
import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/reviews")
public class ReviewsController {

    @Autowired
    private ReviewService reviewService;

    @PostMapping
    @ResponseStatus(CREATED)
    public Review createReview(@Valid @RequestBody Review review) {
        return reviewService.save(review);
    }

    @GetMapping
    public List<Review> getReviews(@RequestParam(name = "productId", required = false) Long productId) {
        if (productId != null) {
            return reviewService.findByProductId(productId);
        }
        return reviewService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity getReview(@PathVariable("id") Long id) {
        Review review = reviewService.findById(id);
        if(review == null) {
            return notFound().build();
        }
        return ok(review);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public void constraintViolationException(ConstraintViolationException e,
                                             HttpServletResponse response) throws IOException {
        response.sendError(BAD_REQUEST.value(), e.getMessage());
    }
}
