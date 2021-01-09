package com.rodrigocso.groceries.test.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rodrigocso.groceries.controller.ProductController;
import com.rodrigocso.groceries.dto.ProductDto;
import com.rodrigocso.groceries.exception.ControllerExceptionHandler;
import com.rodrigocso.groceries.repository.BrandRepository;
import com.rodrigocso.groceries.service.facade.ProductFacade;
import com.rodrigocso.groceries.service.mapper.ProductMapper;
import com.rodrigocso.groceries.test.util.builder.ProductBuilder;
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
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class ProductControllerTests {
    private MockMvc mvc;
    private ProductMapper productMapper;
    private JacksonTester<ProductDto> jsonProduct;
    private JacksonTester<List<ProductDto>> jsonProductList;

    @Mock
    private BrandRepository brandRepository;

    @Mock
    private ProductFacade productFacade;

    @InjectMocks
    private ProductController productController;

    @BeforeEach
    public void setup() {
        productMapper = new ProductMapper(brandRepository);
        JacksonTester.initFields(this, new ObjectMapper());
        mvc = MockMvcBuilders.standaloneSetup(productController)
                .setControllerAdvice(new ControllerExceptionHandler())
                .build();
    }

    @Test
    public void canGetAllProducts() throws Exception {
        List<ProductDto> products = Lists.newArrayList(
                ProductBuilder.builder().build(),
                ProductBuilder.builder().build()
        ).stream().map(productMapper::toDto).collect(Collectors.toList());

        when(productFacade.findAll()).thenReturn(products);

        mvc.perform(get("/products"))
                .andExpect(status().isOk())
                .andExpect(responseBody().isEqualTo(jsonProductList.write(products).getJson()));
    }

    @Test
    public void canGetProductByIdWhenExists() throws Exception {
        ProductDto product = productMapper.toDto(ProductBuilder.builder().build());
        when(productFacade.findById(1L)).thenReturn(Optional.of(product));
        mvc.perform(get("/products/1"))
                .andExpect(status().isOk())
                .andExpect(responseBody().isEqualTo(jsonProduct.write(product).getJson()));
    }

    @Test
    public void whenGetProductByIdDoesNotExist_thenReturnNotFound() throws Exception {
        when(productFacade.findById(1L)).thenReturn(Optional.empty());
        mvc.perform(get("/products/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void canSearchProductsByName() throws Exception {
        List<ProductDto> products = Lists.newArrayList(
                ProductBuilder.builder().build(),
                ProductBuilder.builder().build()
        ).stream().map(productMapper::toDto).collect(Collectors.toList());

        when(productFacade.findByNameContaining("query")).thenReturn(products);

        mvc.perform(get("/products/search")
                .param("name", "query"))
                .andExpect(status().isOk())
                .andExpect(responseBody().isEqualTo(jsonProductList.write(products).getJson()));
    }

    @Test
    public void canGetProductsByBrandId() throws Exception {
        List<ProductDto> products = Lists.newArrayList(
                ProductBuilder.builder().build(),
                ProductBuilder.builder().build()
        ).stream().map(productMapper::toDto).collect(Collectors.toList());

        when(productFacade.findByBrandId(1L)).thenReturn(Optional.of(products));
        mvc.perform(get("/products/brand/1"))
                .andExpect(status().isOk())
                .andExpect(responseBody().isEqualTo(jsonProductList.write(products).getJson()));
    }

    @Test
    public void whenGetProductsByBrandIdThatDoesNotExist_thenReturnNotFound() throws Exception {
        when(productFacade.findByBrandId(1L)).thenReturn(Optional.empty());
        mvc.perform(get("/products/brand/1")).andExpect(status().isNotFound());
    }

    @Test
    public void canCreate() throws Exception {
        ProductDto dto = productMapper.toDto(ProductBuilder.builder().withId(1L).build());
        when(productFacade.create(any(ProductDto.class))).thenReturn(dto);
        mvc.perform(post("/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonProduct.write(dto).getJson()))
                .andExpect(status().isCreated());
    }

    @Test
    public void whenCreateInvalidNewProduct_thenReturnBadRequest() throws Exception {
        ProductDto dto = productMapper.toDto(ProductBuilder.builder().withName("").build());
        mvc.perform(post("/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonProduct.write(dto).getJson()))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void canUpdate() throws Exception {
        ProductDto dto = productMapper.toDto(ProductBuilder.builder().build());
        when(productFacade.update(any(Long.class), any(ProductDto.class))).thenReturn(dto);
        mvc.perform(put("/products/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonProduct.write(dto).getJson()))
                .andExpect(status().isOk())
                .andExpect(responseBody().isEqualTo(jsonProduct.write(dto).getJson()));
    }

    @Test
    public void whenUpdateWithInvalidProduct_thenReturnBadRequest() throws Exception {
        ProductDto dto = productMapper.toDto(ProductBuilder.builder().withName("").build());
        mvc.perform(put("/products/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonProduct.write(dto).getJson()))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void canDelete() throws Exception {
        mvc.perform(delete("/products/1"))
                .andExpect(status().isOk());
        verify(productFacade, times(1)).deleteById(1L);
    }
}
