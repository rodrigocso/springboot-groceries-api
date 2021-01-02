package com.rodrigocso.groceries.test.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.rodrigocso.groceries.controller.PurchaseController;
import com.rodrigocso.groceries.dto.PurchaseDto;
import com.rodrigocso.groceries.exception.ControllerExceptionHandler;
import com.rodrigocso.groceries.repository.ItemRepository;
import com.rodrigocso.groceries.repository.StoreRepository;
import com.rodrigocso.groceries.service.facade.PurchaseFacade;
import com.rodrigocso.groceries.service.mapper.PurchaseMapper;
import com.rodrigocso.groceries.test.util.builder.PurchaseBuilder;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.rodrigocso.groceries.test.util.ResponseBodyMatchers.responseBody;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class PurchaseControllerTests {
    private MockMvc mvc;
    private PurchaseMapper purchaseMapper;
    private JacksonTester<PurchaseDto> jsonPurchase;
    private JacksonTester<List<PurchaseDto>> jsonPurchaseList;

    @Mock
    private PurchaseFacade purchaseFacade;

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private StoreRepository storeRepository;

    @InjectMocks
    private PurchaseController purchaseController;

    @BeforeEach
    public void setup() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        JacksonTester.initFields(this, mapper);
        purchaseMapper = new PurchaseMapper(storeRepository, itemRepository);
        mvc = MockMvcBuilders.standaloneSetup(purchaseController)
                .setControllerAdvice(new ControllerExceptionHandler())
                .build();
    }

    @Test
    public void canGetAllPurchases() throws Exception {
        List<PurchaseDto> purchases = Lists.newArrayList(
                PurchaseBuilder.builder().build(),
                PurchaseBuilder.builder().build(),
                PurchaseBuilder.builder().build()
        ).stream().map(purchaseMapper::toDto).collect(Collectors.toList());

        when(purchaseFacade.findAll()).thenReturn(purchases);

        mvc.perform(get("/purchases"))
                .andExpect(status().isOk())
                .andExpect(responseBody().isEqualTo(jsonPurchaseList.write(purchases).getJson()));
    }

    @Test
    public void canFindById() throws Exception {
        PurchaseDto purchase = purchaseMapper.toDto(PurchaseBuilder.builder().build());
        when(purchaseFacade.findById(1L)).thenReturn(Optional.of(purchase));
        mvc.perform(get("/purchases/1"))
                .andExpect(status().isOk())
                .andExpect(responseBody().isEqualTo(jsonPurchase.write(purchase).getJson()));
    }

    @Test
    public void canFindByProductId() throws Exception {
        List<PurchaseDto> purchases = Lists.newArrayList(purchaseMapper.toDto(PurchaseBuilder.builder().build()));
        when(purchaseFacade.findByProductId(1L)).thenReturn(Optional.of(purchases));
        mvc.perform(get("/purchases/product/1"))
                .andExpect(status().isOk())
                .andExpect(responseBody().isEqualTo(jsonPurchaseList.write(purchases).getJson()));
    }

    @Test
    public void canCreate() throws Exception {
        PurchaseDto purchase = purchaseMapper.toDto(PurchaseBuilder.builder().build());
        when(purchaseFacade.create(any(PurchaseDto.class))).thenReturn(purchase);
        mvc.perform(post("/purchases")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonPurchase.write(purchase).getJson()))
                .andExpect(status().isOk())
                .andExpect(responseBody().isEqualTo(jsonPurchase.write(purchase).getJson()));
    }

    @Test
    public void canUpdate() throws Exception {
        PurchaseDto purchase = purchaseMapper.toDto(PurchaseBuilder.builder().build());
        when(purchaseFacade.update(eq(1L), any(PurchaseDto.class))).thenReturn(purchase);
        mvc.perform(put("/purchases/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonPurchase.write(purchase).getJson()))
                .andExpect(status().isOk())
                .andExpect(responseBody().isEqualTo(jsonPurchase.write(purchase).getJson()));
    }

    @Test
    public void canDelete() throws Exception {
        mvc.perform(delete("/purchases/1"))
                .andExpect(status().isOk());
        verify(purchaseFacade, times(1)).deleteById(1L);
    }
}
