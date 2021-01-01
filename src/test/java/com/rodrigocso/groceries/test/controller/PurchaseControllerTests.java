package com.rodrigocso.groceries.test.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rodrigocso.groceries.controller.PurchaseController;
import com.rodrigocso.groceries.exception.ControllerExceptionHandler;
import com.rodrigocso.groceries.model.Purchase;
import com.rodrigocso.groceries.repository.PurchaseRepositoryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(MockitoExtension.class)
public class PurchaseControllerTests {
    private MockMvc mvc;

    @Mock
    private PurchaseRepositoryImpl purchaseRepository;

    @InjectMocks
    private PurchaseController purchaseController;

    private JacksonTester<Purchase> jsonPurchase;

    @BeforeEach
    public void setup() {
        JacksonTester.initFields(this, new ObjectMapper());

        mvc = MockMvcBuilders.standaloneSetup(purchaseController)
                .setControllerAdvice(new ControllerExceptionHandler())
                .build();
    }

//    @Test
//    public void whenGetPurchases_thenReturnNonEmptyPurchaseArray() throws Exception {
//        List<Purchase> purchaseList = Lists.newArrayList(
//                PurchaseBuilder.builder().build(),
//                PurchaseBuilder.builder().build());
//        when(purchaseRepository.findAll()).thenReturn(purchaseList);
//        mvc.perform(get("/purchases"))
//                .andExpect(status().isOk())
//                .andExpect(responseBody().containsObjectAsJson(purchaseList.toArray(), Purchase[].class));
//    }
}
