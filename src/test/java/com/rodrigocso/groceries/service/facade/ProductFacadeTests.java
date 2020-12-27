package com.rodrigocso.groceries.service.facade;

import com.rodrigocso.groceries.model.Brand;
import com.rodrigocso.groceries.repository.BrandRepository;
import com.rodrigocso.groceries.repository.ProductRepository;
import com.rodrigocso.groceries.util.builder.BrandBuilder;
import com.rodrigocso.groceries.util.builder.ProductBuilder;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
public class ProductFacadeTests {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private BrandRepository brandRepository;

    private ProductFacade productFacade;

    @BeforeEach
    public void setup() {
        this.productFacade = new ProductFacade(productRepository);
    }

    @Test
    public void productFacadeIsNotNull() {
        assertThat(productFacade).isNotNull();
    }

    @Test
    public void whenGetAllProducts_thenReturnProductList() {
        Brand b = brandRepository.save(BrandBuilder.builder().withName("Apple").build());
        productRepository.saveAll(Lists.newArrayList(
                ProductBuilder.builder().withName("iPhone").withBrand(b).build(),
                ProductBuilder.builder().withName("iPad").withBrand(b).build()));
        assertThat(productFacade.findAll().size()).isEqualTo(2);
    }

    @Test
    public void whenGetProductById_thenReturnProduct() {
        Brand b = brandRepository.save(BrandBuilder.builder().build());
        Integer productId = productRepository.save(ProductBuilder.builder().withBrand(b).build()).getId();
        assertThat(productFacade.findById(productId)).isNotNull();
    }

    @Test
    public void whenGetProductsByBrandId_thenReturnProductList() {
        Brand b = brandRepository.save(BrandBuilder.builder().withName("Apple").build());
        productRepository.saveAll(Lists.newArrayList(
                ProductBuilder.builder().withName("iPhone").withBrand(b).build(),
                ProductBuilder.builder().withName("iPad").withBrand(b).build()));
        assertThat(productFacade.findByBrandId(b.getId()).size()).isEqualTo(2);
    }
}
