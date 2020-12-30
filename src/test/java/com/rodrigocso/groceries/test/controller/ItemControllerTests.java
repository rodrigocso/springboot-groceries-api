package com.rodrigocso.groceries.test.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rodrigocso.groceries.controller.ItemController;
import com.rodrigocso.groceries.exception.ControllerExceptionHandler;
import com.rodrigocso.groceries.model.Item;
import com.rodrigocso.groceries.repository.ItemRepository;
import com.rodrigocso.groceries.test.util.builder.ItemBuilder;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import static com.rodrigocso.groceries.test.util.ResponseBodyMatchers.responseBody;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class ItemControllerTests {
    private MockMvc mvc;

    @Mock
    private ItemRepository itemRepository;

    @InjectMocks
    private ItemController itemController;

    private JacksonTester<Item> jsonItem;

    @BeforeEach
    public void setup() {
        JacksonTester.initFields(this, new ObjectMapper());
        mvc = MockMvcBuilders.standaloneSetup(itemController)
                .setControllerAdvice(new ControllerExceptionHandler())
                .build();
    }

    @Test
    public void whenGetItems_thenReturnItemArray() throws Exception {
        List<Item> mockItems = Lists.newArrayList(
                ItemBuilder.builder().build(),
                ItemBuilder.builder().build());

        when(itemRepository.findAll()).thenReturn(mockItems);

        mvc.perform(get("/items"))
                .andExpect(status().isOk())
                .andExpect(responseBody().containsObjectAsJson(mockItems.toArray(), Item[].class));
    }

    @Test
    public void whenGetItemByIdExists_thenReturnItem() throws Exception {
        Item testItem = ItemBuilder.builder().build();
        when(itemRepository.findById(1L)).thenReturn(Optional.of(testItem));
        mvc.perform(get("/items/1"))
                .andExpect(status().isOk())
                .andExpect(responseBody().containsObjectAsJson(testItem, Item.class));
    }

    @Test
    public void whenGetItemByIdDoesNotExist_thenReturn404() throws Exception {
        when(itemRepository.findById(1L)).thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND));
        mvc.perform(get("/items/1")).andExpect(status().isNotFound());
    }

    @Test
    public void whenGetItemsByProductId_thenReturnItemArray() throws Exception {
        List<Item> testItems = Lists.newArrayList(
                ItemBuilder.builder().build(),
                ItemBuilder.builder().build());

        when(itemRepository.findByProductId(any(Long.class))).thenReturn(testItems);

        mvc.perform(get("/items/product/1"))
                .andExpect(status().isOk())
                .andExpect(responseBody().containsObjectAsJson(testItems.toArray(), Item[].class));
    }

    @Test
    public void whenPostValidItem_thenReturn200() throws Exception {
        Item dto = ItemBuilder.builder().build();
        when(itemRepository.save(any(Item.class))).thenReturn(dto);
        mvc.perform(post("/items")
                .contentType("application/json")
                .content(jsonItem.write(dto).getJson()))
                .andExpect(status().isOk())
                .andExpect(responseBody().containsObjectAsJson(dto, Item.class));
    }

    @Test
    public void whenPostInvalidItem_thenReturn400() throws Exception {
        Item dto = ItemBuilder.builder()
                .withPackageSize(-5F)
                .withProduct(null)
                .withUnit(null)
                .build();
        mvc.perform(post("/items")
                .contentType("application/json")
                .content(jsonItem.write(dto).getJson()))
                .andExpect(status().isBadRequest())
                .andExpect(responseBody().containsError("packageSize", "HAS_TO_BE_POSITIVE"))
                .andExpect(responseBody().containsError("product", "IS_REQUIRED"))
                .andExpect(responseBody().containsError("unit", "IS_REQUIRED"));
    }

    @Test
    public void whenPutValidItem_thenReturn200() throws Exception {
        Item dto = ItemBuilder.builder().withId(1L).build();
        when(itemRepository.findById(1L)).thenReturn(Optional.of(dto));
        when(itemRepository.save(any(Item.class))).thenReturn(dto);
        mvc.perform(put("/items/1")
                .contentType("application/json")
                .content(jsonItem.write(dto).getJson()))
                .andExpect(status().isOk())
                .andExpect(responseBody().containsObjectAsJson(dto, Item.class));
    }

    @Test
    public void whenPutWithoutBody_thenReturn400() throws Exception {
        mvc.perform(put("/items/1")
                .contentType("application/json"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void whenPutNonExistingItem_thenReturn404() throws Exception {
        when(itemRepository.findById(1L)).thenReturn(Optional.empty());
        mvc.perform(put("/items/1")
                .contentType("application/json")
                .content(jsonItem.write(ItemBuilder.builder().withId(1L).build()).getJson()))
                .andExpect(status().isNotFound());
    }
}
