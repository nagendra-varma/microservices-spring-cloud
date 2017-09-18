package com.techo.productcompose.services.product;

import com.techo.productcompose.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.springframework.http.HttpMethod.GET;

@Service
public class ProductAPIService {

    private RestTemplate restTemplate = new RestTemplate();

    @Autowired
    private ProductServiceUrlProvider urlProvider;

    public ResponseEntity<Product> getProduct(Long id) {
        return restTemplate.getForEntity(urlProvider.getProductUrl(id), Product.class);
    }

    public List<Product> getAllProducts() {
        return restTemplate.exchange(urlProvider.getProductsUrl(), GET, null,
                new ParameterizedTypeReference<List<Product>>(){}).getBody();
    }

    public ResponseEntity createProduct(Product product) {
        return restTemplate.postForEntity(urlProvider.getProductsUrl(), product, Product.class);
    }
}
