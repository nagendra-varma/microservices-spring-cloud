package com.techo.productservice.controller;

import com.techo.productservice.ProductServiceApplication;
import com.techo.productservice.model.Product;
import com.techo.productservice.service.ProductService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import java.util.Arrays;

import static org.apache.commons.lang.builder.EqualsBuilder.reflectionEquals;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = ProductServiceApplication.class)
@WebAppConfiguration
@DirtiesContext(classMode = BEFORE_EACH_TEST_METHOD)
public class ProductsControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    private ProductService customerService;

    @Before
    public void setup() {
        mockMvc = webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void shouldCreateProduct() throws Exception {
        Product product = getProduct();
        mockMvc.perform(post("/products")
                .contentType(APPLICATION_JSON)
                .content(product.toJson()))
                .andExpect(status().isCreated())
                .andExpect(content().json(product.toJson()));

        reflectionEquals(product, customerService.findById(product.getId()));
    }

    @Test
    public void shouldGetAllProducts() throws Exception {
        Product product = insertProduct();
        mockMvc.perform(get("/products")
                .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(Arrays.toString(new Product[]{product})));
    }

    @Test
    public void shouldNotCreateProductIfInvalidParamsSupplied() throws Exception {
        Product product = getProduct();
        product.setName(null);
        mockMvc.perform(post("/products")
                .contentType(APPLICATION_JSON)
                .content(product.toJson()))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldReturnTheRequestedProduct() throws Exception {
        Product product = insertProduct();
        mockMvc.perform(get("/products/1")
                .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(product.toJson()));
    }

    @Test
    public void shouldReturnNotFoundIfRequestedProductNotAvailable() throws Exception {
        mockMvc.perform(get("/products/1")
                .contentType(APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    private Product insertProduct() throws Exception {
        Product customer = getProduct();
        customerService.save(customer);
        return customer;
    }

    public Product getProduct() {
        Product product = new Product();
        product.setId(1L);
        product.setName("Test1");
        return product;
    }
}