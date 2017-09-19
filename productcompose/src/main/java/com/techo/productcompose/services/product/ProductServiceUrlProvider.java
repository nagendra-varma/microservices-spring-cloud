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

    private static final Logger LOG = getLogger(ProductServiceUrlProvider.class);

    private static final String GET_PRODUCTS = "/products";
    private static final String GET_PRODUCT_BY_ID = "/products/%d";
    public static final String REST_SERVICE_NAME = "products-service";

    @Autowired
    private LoadBalancerClient loadBalancer;

    public String getProductsUrl() {
        return getHostUrl().concat(GET_PRODUCTS);
    }

    public String getProductUrl(Long id) {
        return getHostUrl().concat(format(GET_PRODUCT_BY_ID, id));
    }

    private String getHostUrl() {
        return getUrl(REST_SERVICE_NAME);
    }

    private String getUrl(String serviceName) {
        ServiceInstance serviceInstance = loadBalancer.choose(serviceName);
        URI uri = serviceInstance.getUri();
        LOG.debug("Service Instance id - '{}', url - '{}'.", serviceInstance);
        return uri.toString();
    }
}
