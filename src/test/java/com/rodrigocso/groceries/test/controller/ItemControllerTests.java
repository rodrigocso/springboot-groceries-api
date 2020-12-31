package com.rodrigocso.groceries.test.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rodrigocso.groceries.controller.ItemController;
import com.rodrigocso.groceries.dto.ItemDto;
import com.rodrigocso.groceries.exception.ControllerExceptionHandler;
import com.rodrigocso.groceries.repository.ProductRepository;
import com.rodrigocso.groceries.service.facade.ItemFacade;
import com.rodrigocso.groceries.service.mapper.ItemMapper;
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
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.rodrigocso.groceries.test.util.ResponseBodyMatchers.responseBody;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class ItemControllerTests {
    private MockMvc mvc;
    private ItemMapper itemMapper;
    private JacksonTester<ItemDto> jsonItem;
    private JacksonTester<List<ItemDto>> jsonItemList;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ItemFacade itemFacade;

    @InjectMocks
    private ItemController itemController;

    @BeforeEach
    public void setup() {
        itemMapper = new ItemMapper(productRepository);
        JacksonTester.initFields(this, new ObjectMapper());
        mvc = MockMvcBuilders.standaloneSetup(itemController)
                .setControllerAdvice(new ControllerExceptionHandler())
                .build();
    }

    @Test
    public void canGetAllItems() throws Exception {
        List<ItemDto> items = Lists.newArrayList(
                ItemBuilder.builder().build(),
                ItemBuilder.builder().build()
        ).stream().map(itemMapper::toDto).collect(Collectors.toList());

        when(itemFacade.findAll()).thenReturn(items);

        mvc.perform(get("/items"))
                .andExpect(status().isOk())
                .andExpect(responseBody().isEqualTo(jsonItemList.write(items).getJson()));
    }

    @Test
    public void canGetItemByIdWhenExists() throws Exception {
        ItemDto item = itemMapper.toDto(ItemBuilder.builder().build());
        when(itemFacade.findById(1L)).thenReturn(Optional.of(item));
        mvc.perform(get("/items/1"))
                .andExpect(status().isOk())
                .andExpect(responseBody().isEqualTo(jsonItem.write(item).getJson()));
    }

    @Test
    public void whenGetItemByIdDoesNotExist_thenReturnNotFound() throws Exception {
        when(itemFacade.findById(1L)).thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND));
        mvc.perform(get("/items/1")).andExpect(status().isNotFound());
    }

    @Test
    public void canGetItemsByProductId() throws Exception {
        List<ItemDto> items = Lists.newArrayList(
                ItemBuilder.builder().build(),
                ItemBuilder.builder().build()
        ).stream().map(itemMapper::toDto).collect(Collectors.toList());

        when(itemFacade.findByProductId(any(Long.class))).thenReturn(Optional.of(items));

        mvc.perform(get("/items/product/1"))
                .andExpect(status().isOk())
                .andExpect(responseBody().isEqualTo(jsonItemList.write(items).getJson()));
    }

    @Test
    public void whenGetItemsByProductIdThatDoesNotExist_thenReturnNotFound() throws Exception {
        when(itemFacade.findByProductId(1L)).thenReturn(Optional.empty());
        mvc.perform(get("/items/product/1")).andExpect(status().isNotFound());
    }

    @Test
    public void canCreateNewItem() throws Exception {
        ItemDto item = itemMapper.toDto(ItemBuilder.builder().build());
        when(itemFacade.create(any(ItemDto.class))).thenReturn(item);
        mvc.perform(post("/items")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonItem.write(item).getJson()))
                .andExpect(status().isOk())
                .andExpect(responseBody().isEqualTo(jsonItem.write(item).getJson()));
    }

    @Test
    public void whenCreateInvalidNewItem_thenReturnBadRequestWithValidationErrors() throws Exception {
        ItemDto item = itemMapper.toDto(ItemBuilder.builder()
                .withPackageSize(-5F)
                .withProduct(null)
                .withUnit(null)
                .build());
        mvc.perform(post("/items")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonItem.write(item).getJson()))
                .andExpect(status().isBadRequest())
                .andExpect(responseBody().containsError("packageSize", "HAS_TO_BE_POSITIVE"))
                .andExpect(responseBody().containsError("productId", "IS_REQUIRED"))
                .andExpect(responseBody().containsError("unit", "IS_REQUIRED"));
    }

    @Test
    public void canUpdateItem() throws Exception {
        ItemDto item = itemMapper.toDto(ItemBuilder.builder().withId(1L).build());
        when(itemFacade.update(any(Long.class), any(ItemDto.class))).thenReturn(item);
        mvc.perform(put("/items/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonItem.write(item).getJson()))
                .andExpect(status().isOk())
                .andExpect(responseBody().isEqualTo(jsonItem.write(item).getJson()));
    }

    @Test
    public void whenUpdateWithoutBody_thenReturnBadRequest() throws Exception {
        mvc.perform(put("/items/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}
