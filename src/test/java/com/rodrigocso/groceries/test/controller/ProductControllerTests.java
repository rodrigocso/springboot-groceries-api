package com.rodrigocso.groceries.test.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rodrigocso.groceries.controller.ProductController;
import com.rodrigocso.groceries.dto.ProductDto;
import com.rodrigocso.groceries.exception.ControllerExceptionHandler;
import com.rodrigocso.groceries.service.facade.ProductFacade;
import com.rodrigocso.groceries.test.util.builder.ProductBuilder;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static com.rodrigocso.groceries.test.util.ResponseBodyMatchers.responseBody;
import static org.mockito.ArgumentMatchers.any;
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
                ProductBuilder.builder().buildDto(),
                ProductBuilder.builder().buildDto()
        );

        when(productFacade.findAll()).thenReturn(mockProducts);

        mvc.perform(get("/products"))
                .andExpect(status().isOk())
                .andExpect(responseBody().containsObjectAsJson(mockProducts.toArray(), ProductDto[].class));
    }

    @Test
    public void whenPostProductWithoutName_thenReturn400AndErrorResult() throws Exception {
        mvc.perform(post("/products")
                .contentType("application/json")
                .content(jsonProductDto.write(ProductBuilder.builder().withName("").buildDto()).getJson()))
                .andExpect(status().isBadRequest())
                .andExpect(responseBody().containsError("name", "IS_REQUIRED"));

    }

    @Test
    public void whenGetNonExistingProductById_thenReturn404() throws Exception {
        when(productFacade.findById(1)).thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND));
        mvc.perform(get("/products/1")).andExpect(status().isNotFound());
    }

    @Test
    public void whenPostProductThatAlreadyExists_thenReturn409() throws Exception {
        when(productFacade.save(any(ProductDto.class))).thenThrow(new DataIntegrityViolationException("Oops"));
        mvc.perform(post("/products")
                .contentType("application/json")
                .content(jsonProductDto.write(ProductBuilder.builder().buildDto()).getJson()))
                .andExpect(status().isConflict());
    }

    @Test
    public void whenPostWithoutBody_thenReturn400() throws Exception {
        mvc.perform(post("/products").contentType("application/json")).andExpect(status().isBadRequest());
    }

    @Test
    public void whenValidUpdateExistingProduct_thenReturn200() throws Exception {
        when(productFacade.save(any(ProductDto.class))).thenReturn(null);
        mvc.perform(put("/products/1")
                .contentType("application/json")
                .content(jsonProductDto.write(ProductBuilder.builder().withId(1).buildDto()).getJson()))
                .andExpect(status().isOk());
    }

    @Test
    public void whenGetProductsByBrandId_thenReturnProductList() throws Exception {
        List<ProductDto> mockProducts = Lists.newArrayList(
                ProductBuilder.builder().buildDto(),
                ProductBuilder.builder().buildDto()
        );
        when(productFacade.findByBrandId(1)).thenReturn(mockProducts);
        mvc.perform(get("/products/brand/1"))
                .andExpect(status().isOk())
                .andExpect(responseBody().containsObjectAsJson(mockProducts.toArray(), ProductDto[].class));
    }

    @Test
    public void whenUpdateWithNullProduct_thenReturn400() throws Exception {
        mvc.perform(put("/products/1").contentType("application/json"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void whenUpdateWithMismatchedProduct_thenReturn400() throws Exception {
        mvc.perform(put("/products/1")
                .contentType("application/json")
                .content(jsonProductDto.write(ProductBuilder.builder().withId(2).buildDto()).getJson()))
                .andExpect(status().isBadRequest());
    }
}
