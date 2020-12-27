package com.rodrigocso.groceries.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rodrigocso.groceries.dto.ItemDto;
import com.rodrigocso.groceries.exception.ControllerExceptionHandler;
import com.rodrigocso.groceries.service.facade.ItemFacade;
import com.rodrigocso.groceries.util.builder.ItemBuilder;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class ItemControllerTests {
    private MockMvc mvc;

    @Mock
    private ItemFacade itemFacade;

    @InjectMocks
    private ItemController itemController;

    private JacksonTester<ItemDto> jsonItemDto;

    @BeforeEach
    public void setup() {
        JacksonTester.initFields(this, new ObjectMapper());
        mvc = MockMvcBuilders.standaloneSetup(itemController)
                .setControllerAdvice(new ControllerExceptionHandler())
                .build();
    }

    @Test
    public void whenGetItems_thenReturnNonEmptyItemArray() throws Exception {
        List<ItemDto> mockItems = Lists.newArrayList(
                ItemBuilder.builder().buildDto(),
                ItemBuilder.builder().buildDto());

        when(itemFacade.findAll()).thenReturn(mockItems);

        mvc.perform(get("/items"))
                .andExpect(status().isOk())
                .andExpect(responseBody().containsObjectAsJson(mockItems.toArray(), ItemDto[].class));
    }

    @Test
    public void whenPostValidItem_thenReturn200() throws Exception {
        ItemDto dto = ItemBuilder.builder().buildDto();
        when(itemFacade.save(any(ItemDto.class))).thenReturn(dto);
        mvc.perform(post("/items")
                .contentType("application/json")
                .content(jsonItemDto.write(dto).getJson()))
                .andExpect(status().isOk())
                .andExpect(responseBody().containsObjectAsJson(dto, ItemDto.class));
    }
}
