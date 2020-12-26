package com.rodrigocso.groceries.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rodrigocso.groceries.dto.BrandDto;
import com.rodrigocso.groceries.dto.ProductDto;
import com.rodrigocso.groceries.exception.ControllerExceptionHandler;
import com.rodrigocso.groceries.service.facade.ProductFacade;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static com.rodrigocso.groceries.util.ResponseBodyMatchers.responseBody;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class ProductControllerTests {
    private MockMvc mvc;

    @Mock
    private ProductFacade productFacade;

    @InjectMocks
    private ProductController productController;

    private JacksonTester<ProductDto> jsonProductDto;

    @BeforeEach
    public void setup() {
        JacksonTester.initFields(this, new ObjectMapper());
        mvc = MockMvcBuilders.standaloneSetup(productController)
                .setControllerAdvice(new ControllerExceptionHandler())
                .build();
    }

    @Test
    public void whenGetProducts_thenReturnNonEmptyProductArray() throws Exception {
        List<ProductDto> mockProducts = Lists.newArrayList(
                new ProductDto(1, "Filtered water", new BrandDto(1, "Kirkland")),
                new ProductDto(2, "iPad Pro", new BrandDto(2, "Apple"))
        );

        when(productFacade.findAll()).thenReturn(mockProducts);

        mvc.perform(get("/products"))
                .andExpect(status().isOk())
                .andExpect(responseBody().containsObjectAsJson(mockProducts.toArray(), ProductDto[].class));
    }
}
