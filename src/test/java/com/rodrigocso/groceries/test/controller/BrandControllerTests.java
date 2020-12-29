package com.rodrigocso.groceries.test.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rodrigocso.groceries.controller.BrandController;
import com.rodrigocso.groceries.dto.BrandDto;
import com.rodrigocso.groceries.exception.ControllerExceptionHandler;
import com.rodrigocso.groceries.service.facade.BrandFacade;
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
    private BrandFacade brandFacade;

    @InjectMocks
    private BrandController brandController;

    private JacksonTester<BrandDto> jsonBrandDto;

    @BeforeEach
    public void setup() {
        JacksonTester.initFields(this, new ObjectMapper());
        mvc = MockMvcBuilders.standaloneSetup(brandController)
                .setControllerAdvice(new ControllerExceptionHandler())
                .build();
    }

    @Test
    public void whenGetBrands_thenReturnNonEmptyBrandArray() throws Exception {
        List<BrandDto> mockBrands = Lists.newArrayList(
                BrandBuilder.builder().withName("Kirkland").buildDto(),
                BrandBuilder.builder().withName("Apple").buildDto());

        when(brandFacade.findAll()).thenReturn(mockBrands);

        mvc.perform(get("/brands"))
                .andExpect(status().isOk())
                .andExpect(responseBody().containsObjectAsJson(mockBrands.toArray(), BrandDto[].class));
    }

    @Test
    public void whenPostBrandWithoutName_thenReturn400AndErrorResult() throws Exception {
        mvc.perform(post("/brands")
                .contentType("application/json")
                .content(jsonBrandDto.write(BrandBuilder.builder().withName("").buildDto()).getJson()))
                .andExpect(status().isBadRequest())
                .andExpect(responseBody().containsError("name", "IS_REQUIRED"));
    }

    @Test
    public void whenGetNonExistingBrandById_thenReturn404() throws Exception {
        when(brandFacade.findById(5)).thenReturn(Optional.empty());
        mvc.perform(get("/brands/5")).andExpect(status().isNotFound());
    }

    @Test
    public void whenPostBrandThatAlreadyExists_thenReturn409() throws Exception {
        when(brandFacade.save(any(BrandDto.class))).thenThrow(new DataIntegrityViolationException("Oops"));
        mvc.perform(post("/brands")
                .contentType("application/json")
                .content(jsonBrandDto.write(BrandBuilder.builder().buildDto()).getJson()))
                .andExpect(status().isConflict());
    }

    @Test
    public void whenPutValidExistingBrand_thenReturn200() throws Exception {
        BrandDto dto = BrandBuilder.builder().withId(1).buildDto();
        when(brandFacade.findById(1)).thenReturn(Optional.of(dto));
        when(brandFacade.save(any(BrandDto.class))).thenReturn(dto);
        mvc.perform(put("/brands/1")
                .contentType("application/json")
                .content(jsonBrandDto.write(dto).getJson()))
                .andExpect(status().isOk());
    }

    @Test
    public void whenPutMismatchedId_thenReturn400() throws Exception {
        mvc.perform(put("/brands/1")
                .contentType("application/json")
                .content(jsonBrandDto.write(BrandBuilder.builder().withId(2).buildDto()).getJson()))
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
        when(brandFacade.findById(1)).thenReturn(Optional.empty());
        mvc.perform(put("/brands/1")
                .contentType("application/json")
                .content(jsonBrandDto.write(BrandBuilder.builder().withId(1).buildDto()).getJson()))
                .andExpect(status().isNotFound());
    }
}
