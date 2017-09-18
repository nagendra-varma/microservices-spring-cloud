package com.techo.productcompose.services.reviews;

import com.techo.productcompose.services.product.ProductAPIService;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.stereotype.Component;

import java.net.URI;

import static java.lang.String.format;
import static org.slf4j.LoggerFactory.getLogger;

@Component
public class ReviewServiceUrlProvider {

    private static final Logger LOG = getLogger(ProductAPIService.class);
    private static final String GET_REVIEWS = "/reviews";
    private static final String GET_REVIEWS_BY_ID = "/reviews?productId=%d";
    private static final String REST_SERVICE_NAME = "reviews-service";
    public static final String DEFAULT_URL = "http://localhost:8090";

    @Autowired
    private LoadBalancerClient loadBalancer;

    public String getReviews() {
        return getUrl(REST_SERVICE_NAME, DEFAULT_URL)
                .concat(GET_REVIEWS);
    }

    public String getReviewsByProductIdUrl(Long id) {
        return getUrl(REST_SERVICE_NAME, DEFAULT_URL).concat(format(GET_REVIEWS_BY_ID, id));
    }

    private String getUrl(String serviceName, String defaultUrl) {
        URI uri;
        try {
            ServiceInstance serviceInstance = loadBalancer.choose(serviceName);
            uri = serviceInstance.getUri();
            LOG.info("Service Instance id - '{}', url - '{}'.", serviceInstance, uri);
        } catch (RuntimeException e) {
            uri = URI.create(defaultUrl);
            LOG.warn("Failed to get serviceId '{}', falling back to default url - '{}'.", serviceName, uri);
        }
        return uri.toString();
    }
}
