package com.techo.productcompose.services.product;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.stereotype.Component;

import java.net.URI;

import static java.lang.String.format;
import static org.slf4j.LoggerFactory.getLogger;

@Component
public class ProductServiceUrlProvider {

    private static final Logger LOG = getLogger(ProductAPIService.class);

    private static final String GET_PRODUCTS = "/products";
    private static final String GET_PRODUCT_BY_ID = "/products/%d";
    private static final String REST_SERVICE_NAME = "products-service";
    public static final String DEFAULT_URL = "http://localhost:8080";

    @Autowired
    private LoadBalancerClient loadBalancer;

    public String getProductsUrl() {
        return getHostUrl().concat(GET_PRODUCTS);
    }

    public String getProductUrl(Long id) {
        return getHostUrl().concat(format(GET_PRODUCT_BY_ID, id));
    }

    private String getHostUrl() {
        return getUrl(REST_SERVICE_NAME, DEFAULT_URL);
    }

    private String getUrl(String serviceName, String defaultUrl) {
        URI uri;
        try {
            ServiceInstance serviceInstance = loadBalancer.choose(serviceName);
            uri = serviceInstance.getUri();
            LOG.debug("Service Instance id - '{}', url - '{}'.", serviceInstance, uri);
        } catch (RuntimeException e) {
            uri = URI.create(defaultUrl);
            LOG.warn("Failed to get serviceId '{}', falling back to default url - '{}'.", serviceName, uri);
        }
        return uri.toString();
    }
}
