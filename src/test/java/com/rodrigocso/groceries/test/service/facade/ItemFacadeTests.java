package com.rodrigocso.groceries.test.service.facade;

import com.rodrigocso.groceries.dto.ItemDto;
import com.rodrigocso.groceries.model.Brand;
import com.rodrigocso.groceries.model.Product;
import com.rodrigocso.groceries.repository.BrandRepository;
import com.rodrigocso.groceries.repository.ItemRepository;
import com.rodrigocso.groceries.repository.ProductRepository;
import com.rodrigocso.groceries.service.facade.ItemFacade;
import com.rodrigocso.groceries.test.util.builder.BrandBuilder;
import com.rodrigocso.groceries.test.util.builder.ItemBuilder;
import com.rodrigocso.groceries.test.util.builder.ProductBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
public class ItemFacadeTests {
    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private BrandRepository brandRepository;

    private ItemFacade itemFacade;

    private Product testProduct;

    @BeforeEach
    public void setup() {
        itemFacade = new ItemFacade(itemRepository);
        Brand testBrand = brandRepository.save(BrandBuilder.builder().build());
        testProduct = productRepository.save(ProductBuilder.builder().withBrand(testBrand).build());
    }

    @Test
    public void itemFacadeIsNotNull() {
        assertThat(itemFacade).isNotNull();
    }

    @Test
    public void whenSaveItem_thenCanFindIt() {
        ItemDto testItem = itemFacade.save(ItemBuilder.builder().withProduct(testProduct).buildDto());
        assertThat(itemFacade.findById(testItem.getId())).usingRecursiveComparison().isEqualTo(Optional.of(testItem));
    }
}
