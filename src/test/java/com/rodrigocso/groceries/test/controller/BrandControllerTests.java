package com.rodrigocso.groceries.test.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rodrigocso.groceries.controller.BrandController;
import com.rodrigocso.groceries.exception.ControllerExceptionHandler;
import com.rodrigocso.groceries.model.Brand;
import com.rodrigocso.groceries.repository.BrandRepository;
import com.rodrigocso.groceries.test.util.builder.BrandBuilder;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import java.util.Optional;

import static com.rodrigocso.groceries.test.util.ResponseBodyMatchers.responseBody;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class BrandControllerTests {
    private MockMvc mvc;

    @Mock
    private BrandRepository brandRepository;

    @InjectMocks
    private BrandController brandController;

    private JacksonTester<Brand> jsonBrandDto;

    @BeforeEach
    public void setup() {
        JacksonTester.initFields(this, new ObjectMapper());
        mvc = MockMvcBuilders.standaloneSetup(brandController)
                .setControllerAdvice(new ControllerExceptionHandler())
                .build();
    }

    @Test
    public void whenGetBrands_thenReturnNonEmptyBrandArray() throws Exception {
        List<Brand> testBrands = Lists.newArrayList(
                BrandBuilder.builder().withName("Kirkland").build(),
                BrandBuilder.builder().withName("Apple").build());

        when(brandRepository.findAll()).thenReturn(testBrands);

        mvc.perform(get("/brands"))
                .andExpect(status().isOk())
                .andExpect(responseBody().containsObjectAsJson(testBrands.toArray(), Brand[].class));
    }

    @Test
    public void whenPostBrandWithoutName_thenReturn400AndErrorResult() throws Exception {
        mvc.perform(post("/brands")
                .contentType("application/json")
                .content(jsonBrandDto.write(BrandBuilder.builder().withName("").build()).getJson()))
                .andExpect(status().isBadRequest())
                .andExpect(responseBody().containsError("name", "IS_REQUIRED"));
    }

    @Test
    public void whenGetNonExistingBrandById_thenReturn404() throws Exception {
        when(brandRepository.findById(5L)).thenReturn(Optional.empty());
        mvc.perform(get("/brands/5")).andExpect(status().isNotFound());
    }

    @Test
    public void whenPostBrandThatAlreadyExists_thenReturn409() throws Exception {
        when(brandRepository.save(any(Brand.class))).thenThrow(new DataIntegrityViolationException("Oops"));
        mvc.perform(post("/brands")
                .contentType("application/json")
                .content(jsonBrandDto.write(BrandBuilder.builder().build()).getJson()))
                .andExpect(status().isConflict());
    }

    @Test
    public void whenPutValidExistingBrand_thenReturn200() throws Exception {
        Brand brand = BrandBuilder.builder().withId(1L).build();
        when(brandRepository.findById(1L)).thenReturn(Optional.of(brand));
        when(brandRepository.save(any(Brand.class))).thenReturn(brand);
        mvc.perform(put("/brands/1")
                .contentType("application/json")
                .content(jsonBrandDto.write(brand).getJson()))
                .andExpect(status().isOk());
    }

    @Test
    public void whenPutMismatchedId_thenReturn400() throws Exception {
        mvc.perform(put("/brands/1")
                .contentType("application/json")
                .content(jsonBrandDto.write(BrandBuilder.builder().withId(2L).build()).getJson()))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void whenPutWithoutBody_thenReturn400() throws Exception {
        mvc.perform(put("/brands/1")
                .contentType("application/json"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void whenPutNonExistingBrand_thenReturn404() throws Exception {
        when(brandRepository.findById(1L)).thenReturn(Optional.empty());
        mvc.perform(put("/brands/1")
                .contentType("application/json")
                .content(jsonBrandDto.write(BrandBuilder.builder().withId(1L).build()).getJson()))
                .andExpect(status().isNotFound());
    }
}
