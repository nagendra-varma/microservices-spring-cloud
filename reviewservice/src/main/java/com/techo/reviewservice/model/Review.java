package com.techo.reviewservice.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.log4j.Logger;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@Entity
public class Review {

    private static final Logger LOG = Logger.getLogger(Review.class);

    @Id
    @GeneratedValue
    private Long reviewId;

    @NotEmpty(message = "Subject should not be empty")
    private String subject;

    @NotNull(message = "Author should not be null")
    private String author;

    @NotNull(message = "ProductId should not be null")
    private Long productId;

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public void setReviewId(Long reviewId) {
        this.reviewId = reviewId;
    }

    public Long getReviewId() {
        return reviewId;
    }

    public String getSubject() {
        return subject;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String toJson() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(this);
    }

    public String toString() {
        try {
            return toJson();
        } catch (JsonProcessingException e) {
            LOG.warn("Json parse error : ", e);
        }
        return "";
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }
}
