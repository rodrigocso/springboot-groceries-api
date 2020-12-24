package com.rodrigocso.groceries.controller;

import com.rodrigocso.groceries.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ProductControllerTests {
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private String productsUrl;

    @BeforeEach
    public void beforeEach() {
        productsUrl = "http://localhost:" + port + "/products";
    }

    @Test
    public void get_ProductsAlreadySaved_ShouldReturnNonEmptyProductArray() {
        assertThat(restTemplate.getForEntity(productsUrl, Product[].class).getBody())
                .isNotEmpty()
                .hasOnlyElementsOfType(Product.class);
    }
}
