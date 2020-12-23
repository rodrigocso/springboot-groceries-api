package com.rodrigocso.groceries.brand;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.HttpClientBuilder;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;


import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BrandControllerTests {
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private ObjectMapper objectMapper;

    @BeforeEach
    public void beforeEach() throws Exception {
        this.restTemplate.postForEntity("http://localhost:" + port + "/brands", new Brand("Amazon"), Brand.class);
        this.restTemplate.postForEntity("http://localhost:" + port + "/brands", new Brand("Apple"), Brand.class);
        this.restTemplate.postForEntity("http://localhost:" + port + "/brands", new Brand("Google"), Brand.class);
        this.restTemplate.postForEntity("http://localhost:" + port + "/brands", new Brand("Microsoft"), Brand.class);
    }

    @Test
    public void getBrandsShouldReturnBrandList() throws Exception {
        assertThat(this.restTemplate.getForEntity("http://localhost:" + port + "/brands", Brand[].class).getBody())
            .isNotEmpty()
            .hasOnlyElementsOfType(Brand.class);
    }
}
