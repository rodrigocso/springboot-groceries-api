package com.rodrigocso.groceries.test.service.facade;

import com.rodrigocso.groceries.dto.ProductDto;
import com.rodrigocso.groceries.model.Brand;
import com.rodrigocso.groceries.model.Product;
import com.rodrigocso.groceries.repository.BrandRepository;
import com.rodrigocso.groceries.repository.ProductRepository;
import com.rodrigocso.groceries.service.facade.ProductFacade;
import com.rodrigocso.groceries.service.mapper.ProductMapper;
import com.rodrigocso.groceries.test.util.builder.BrandBuilder;
import com.rodrigocso.groceries.test.util.builder.ProductBuilder;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
public class ProductFacadeTests {
    @Autowired
    private BrandRepository brandRepository;

    @Autowired
    private ProductRepository productRepository;

    private ProductFacade productFacade;
    private ProductMapper productMapper;

    @BeforeEach
    public void setup() {
        productMapper = new ProductMapper(brandRepository);
        productFacade = new ProductFacade(brandRepository, productMapper, productRepository);
    }

    @Test
    public void canFindAll() {
        List<Product> products = Lists.newArrayList(
                ProductBuilder.builder().build(),
                ProductBuilder.builder().build(),
                ProductBuilder.builder().build()
        );

        List<ProductDto> savedProducts = productRepository.saveAll(products).stream()
                .map(productMapper::toDto).collect(Collectors.toList());

        assertThat(productFacade.findAll()).usingRecursiveComparison().isEqualTo(savedProducts);
    }

    @Test
    public void canFindById() {
        List<Product> products = Lists.newArrayList(
                ProductBuilder.builder().build(),
                ProductBuilder.builder().build(),
                ProductBuilder.builder().build()
        );

        List<ProductDto> savedProducts = productRepository.saveAll(products).stream()
                .map(productMapper::toDto).collect(Collectors.toList());

        ProductDto retrievedProduct = productFacade.findById(savedProducts.get(0).getId()).orElse(null);
        assertThat(retrievedProduct).usingRecursiveComparison().isEqualTo(savedProducts.get(0));
    }

    @Test
    public void canFindByNameContaining() {
        productRepository.saveAll(Lists.newArrayList(
                ProductBuilder.builder().withName("Cheese bread").build(),
                ProductBuilder.builder().withName("Gouda cheese").build(),
                ProductBuilder.builder().withName("Whole milk").build()));

        assertThat(productFacade.findByNameContaining("CHEESE").size()).isEqualTo(2);
    }

    @Test
    public void canFindByBrandId() {
        Brand b = brandRepository.save(BrandBuilder.builder().build());

        productRepository.saveAll(Lists.newArrayList(
                ProductBuilder.builder().build(),
                ProductBuilder.builder().withBrand(b).build(),
                ProductBuilder.builder().withBrand(b).build()));

        assertThat(productFacade.findByBrandId(b.getId()).orElse(Lists.emptyList()).size()).isEqualTo(2);
    }

    @Test
    public void canCreate() {
        ProductDto createdProduct = productFacade.create(productMapper.toDto(ProductBuilder.builder().build()));
        assertThat(createdProduct.getId()).isNotNull();
    }

    @Test
    public void canUpdate() {
        ProductDto product = productMapper.toDto(productRepository.save(ProductBuilder.builder().build()));
        product.setName("Milk");

        assertThat(productFacade.update(product.getId(), product).getName()).isEqualTo("Milk");
    }

    @Test
    public void canDelete() {
        ProductDto product = productMapper.toDto(productRepository.save(ProductBuilder.builder().build()));
        productFacade.deleteById(product.getId());
        assertThat(productFacade.findAll().size()).isEqualTo(0);
    }
}
