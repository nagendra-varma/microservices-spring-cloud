package com.techo.productservice.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.log4j.Logger;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Product {

    private static final Logger LOG = Logger.getLogger(Product.class);

    @Id
    @GeneratedValue
    private Long id;

    @NotEmpty(message = "Product name should not be empty")
    private String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String toJson() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(this);
    }

    public String toString() {
        try {
            return toJson();
        } catch (JsonProcessingException e) {
            LOG.error("Json parse error : ", e);
        }
        return super.toString();
    }
}
