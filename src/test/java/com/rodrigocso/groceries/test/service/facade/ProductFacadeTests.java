package com.rodrigocso.groceries.test.service.facade;

import com.rodrigocso.groceries.dto.ProductDto;
import com.rodrigocso.groceries.model.Brand;
import com.rodrigocso.groceries.repository.BrandRepository;
import com.rodrigocso.groceries.repository.ProductRepository;
import com.rodrigocso.groceries.service.facade.ProductFacade;
import com.rodrigocso.groceries.test.util.builder.BrandBuilder;
import com.rodrigocso.groceries.test.util.builder.ProductBuilder;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest
public class ProductFacadeTests {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private BrandRepository brandRepository;

    private ProductFacade productFacade;

    private Brand testBrand;

    @BeforeEach
    public void setup() {
        productFacade = new ProductFacade(productRepository);
        testBrand = brandRepository.save(BrandBuilder.builder().build());
    }

    @Test
    public void productFacadeIsNotNull() {
        assertThat(productFacade).isNotNull();
    }

    @Test
    public void whenSaveAllProducts_thenCanFindThem() {
        List<ProductDto> products = productFacade.saveAll(Lists.newArrayList(
                ProductBuilder.builder().withName("P1").withBrand(testBrand).buildDto(),
                ProductBuilder.builder().withName("P2").withBrand(testBrand).buildDto()));

        assertThat(productFacade.findAll()).usingRecursiveComparison().isEqualTo(products);
    }

    @Test
    public void whenSaveAllWithNull_thenThrowBadRequestException() {
        assertThat(assertThrows(
                ResponseStatusException.class,
                () -> productFacade.saveAll(null))
                .getRawStatusCode())
                .isEqualTo(400);
    }

    @Test
    public void whenSaveProduct_thenCanFindIt() {
        Integer productId = productFacade.save(ProductBuilder.builder().withBrand(testBrand).buildDto()).getId();
        assertThat(productFacade.findById(productId)).isNotNull();
    }

    @Test
    public void whenGetNonExistingProductById_thenThrowNotFoundException() {
        assertThat(productFacade.findById(1)).isEqualTo(Optional.empty());
    }

    @Test
    public void whenGetProductByName_thenReturnMatchingProductList() {
        productRepository.saveAll(Lists.newArrayList(
                ProductBuilder.builder().withBrand(testBrand).withName("Match 1").build(),
                ProductBuilder.builder().withBrand(testBrand).withName("MATCH 2").build()));
        assertThat(productFacade.findByNameContaining("mAtCH").size()).isEqualTo(2);
    }

    @Test
    public void whenGetProductsByBrandId_thenReturnProductList() {
        productRepository.saveAll(Lists.newArrayList(
                ProductBuilder.builder().withName("P1").withBrand(testBrand).build(),
                ProductBuilder.builder().withName("P2").withBrand(testBrand).build()));
        assertThat(productFacade.findByBrandId(testBrand.getId()).size()).isEqualTo(2);
    }

    @Test
    public void whenGetProductsByNonExistingBrandId_thenReturnEmptyProductList() {
        assertThat(productFacade.findByBrandId(4).size()).isEqualTo(0);
    }

    @Test
    public void canUpdateProduct() {
        ProductDto original = productFacade.save(
                ProductBuilder.builder().withBrand(testBrand).withName("Old name").buildDto());
        ProductDto modified = ProductBuilder.builder().withBrand(testBrand).withId(original.getId()).withName("New name").buildDto();
        assertThat(productFacade.save(modified)).usingRecursiveComparison().isEqualTo(modified);
    }

    @Test
    public void whenUpdateProductToDuplicated_thenThrowDataIntegrityViolationException() {
        ProductDto p1 = productFacade.save(ProductBuilder.builder().withBrand(testBrand).withName("P1").buildDto());
        ProductDto p2 = productFacade.save(ProductBuilder.builder().withBrand(testBrand).withName("P2").buildDto());
        p1.setName(p2.getName());
        assertThrows(DataIntegrityViolationException.class, () -> productFacade.save(p1));
    }
}
