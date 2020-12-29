package com.rodrigocso.groceries.test.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rodrigocso.groceries.controller.StoreController;
import com.rodrigocso.groceries.dto.StoreDto;
import com.rodrigocso.groceries.exception.ControllerExceptionHandler;
import com.rodrigocso.groceries.service.facade.StoreFacade;
import com.rodrigocso.groceries.test.util.builder.StoreBuilder;
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
public class StoreControllerTests {
    private MockMvc mvc;

    @Mock
    private StoreFacade storeFacade;

    @InjectMocks
    private StoreController storeController;

    private JacksonTester<StoreDto> jsonStoreDto;

    @BeforeEach
    public void setup() {
        JacksonTester.initFields(this, new ObjectMapper());
        mvc = MockMvcBuilders.standaloneSetup(storeController)
                .setControllerAdvice(new ControllerExceptionHandler())
                .build();
    }

    @Test
    public void whenGetBrands_thenReturnNonEmptyBrandArray() throws Exception {
        List<StoreDto> testStores = Lists.newArrayList(
                StoreBuilder.builder().withName("Costco").buildDto(),
                StoreBuilder.builder().withName("Whole Foods").buildDto());

        when(storeFacade.findAll()).thenReturn(testStores);

        mvc.perform(get("/stores"))
                .andExpect(status().isOk())
                .andExpect(responseBody().containsObjectAsJson(testStores.toArray(), StoreDto[].class));
    }

    @Test
    public void whenPostBrandWithoutName_thenReturn400AndErrorResult() throws Exception {
        mvc.perform(post("/stores")
                .contentType("application/json")
                .content(jsonStoreDto.write(StoreBuilder.builder().withName("").buildDto()).getJson()))
                .andExpect(status().isBadRequest())
                .andExpect(responseBody().containsError("name", "IS_REQUIRED"));
    }

    @Test
    public void whenGetNonExistingBrandById_thenReturn404() throws Exception {
        when(storeFacade.findById(5)).thenReturn(Optional.empty());
        mvc.perform(get("/stores/5")).andExpect(status().isNotFound());
    }

    @Test
    public void whenPostBrandThatAlreadyExists_thenReturn409() throws Exception {
        when(storeFacade.save(any(StoreDto.class))).thenThrow(new DataIntegrityViolationException("Oops"));
        mvc.perform(post("/stores")
                .contentType("application/json")
                .content(jsonStoreDto.write(StoreBuilder.builder().buildDto()).getJson()))
                .andExpect(status().isConflict());
    }

    @Test
    public void whenPutValidExistingBrand_thenReturn200() throws Exception {
        StoreDto dto = StoreBuilder.builder().withId(1).buildDto();
        when(storeFacade.findById(1)).thenReturn(Optional.of(dto));
        when(storeFacade.save(any(StoreDto.class))).thenReturn(dto);
        mvc.perform(put("/stores/1")
                .contentType("application/json")
                .content(jsonStoreDto.write(dto).getJson()))
                .andExpect(status().isOk());
    }

    @Test
    public void whenPutMismatchedId_thenReturn400() throws Exception {
        mvc.perform(put("/stores/1")
                .contentType("application/json")
                .content(jsonStoreDto.write(StoreBuilder.builder().withId(2).buildDto()).getJson()))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void whenPutWithoutBody_thenReturn400() throws Exception {
        mvc.perform(put("/stores/1")
                .contentType("application/json"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void whenPutNonExistingBrand_thenReturn404() throws Exception {
        when(storeFacade.findById(1)).thenReturn(Optional.empty());
        mvc.perform(put("/stores/1")
                .contentType("application/json")
                .content(jsonStoreDto.write(StoreBuilder.builder().withId(1).buildDto()).getJson()))
                .andExpect(status().isNotFound());
    }
}
