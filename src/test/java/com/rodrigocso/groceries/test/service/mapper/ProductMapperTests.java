package com.rodrigocso.groceries.test.service.mapper;

import com.rodrigocso.groceries.dto.ProductDto;
import com.rodrigocso.groceries.model.Brand;
import com.rodrigocso.groceries.model.Product;
import com.rodrigocso.groceries.repository.BrandRepository;
import com.rodrigocso.groceries.service.mapper.ProductMapper;
import com.rodrigocso.groceries.test.util.builder.BrandBuilder;
import com.rodrigocso.groceries.test.util.builder.ProductBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProductMapperTests {
    private ProductMapper productMapper;

    @Mock
    private BrandRepository brandRepository;

    @BeforeEach
    public void setup() {
        productMapper = new ProductMapper(brandRepository);
    }

    @Test
    public void canConvertToDto() {
        Product p = ProductBuilder.builder().withBrand(BrandBuilder.builder().build()).build();
        ProductDto dto = productMapper.toDto(p);

        assertThat(dto.getId()).isEqualTo(p.getId());
        assertThat(dto.getName()).isEqualTo(p.getName());
        assertThat(dto.getBrandId()).isEqualTo(p.getBrand().getId());
    }

    @Test
    public void canConvertToProduct() {
        Brand b = BrandBuilder.builder().withId(1L).build();
        ProductDto dto = productMapper.toDto(ProductBuilder.builder().withBrand(b).build());
        when(brandRepository.findById(1L)).thenReturn(Optional.of(b));
        Product p = productMapper.toProduct(dto);

        assertThat(dto.getId()).isEqualTo(p.getId());
        assertThat(dto.getName()).isEqualTo(p.getName());
        assertThat(dto.getBrandId()).isEqualTo(p.getBrand().getId());
    }
}
