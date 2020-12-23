package com.rodrigocso.groceries.brand;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BrandControllerTests {
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private String endpointUrl;

    @BeforeEach
    public void beforeEach() {
        endpointUrl = "http://localhost:" + port + "/brands";

        restTemplate.postForEntity(endpointUrl, new Brand("Amazon"), Brand.class);
        restTemplate.postForEntity(endpointUrl, new Brand("Apple"), Brand.class);
        restTemplate.postForEntity(endpointUrl, new Brand("Google"), Brand.class);
        restTemplate.postForEntity(endpointUrl, new Brand("Microsoft"), Brand.class);
    }

    @Test
    public void get_BrandsAlreadySaved_ShouldReturnNonEmptyBrandArray() {
        assertThat(restTemplate.getForEntity(endpointUrl, Brand[].class).getBody())
                .isNotEmpty()
                .hasOnlyElementsOfType(Brand.class);
    }

    @Test
    public void post_BrandWithBlankName_ShouldReturnBadRequest() {
        assertThat(this.restTemplate.postForEntity(endpointUrl, new Brand(), Brand.class).getStatusCodeValue())
                .isEqualTo(400);
    }

    @Test
    public void getById_BrandDoesNotExist_ShouldReturnNotFound() {
        assertThat(restTemplate.getForEntity(endpointUrl + "/5", Brand.class).getStatusCodeValue())
                .isEqualTo(404);
    }

    @Test
    public void post_DuplicatedBrand_ShouldReturnError() {
        assertThat(restTemplate.postForEntity(endpointUrl, new Brand("Apple"), Brand.class).getStatusCodeValue())
                .isEqualTo(422);
    }
}
