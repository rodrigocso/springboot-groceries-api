package com.rodrigocso.groceries.test.service.facade;

import com.rodrigocso.groceries.dto.ItemDto;
import com.rodrigocso.groceries.model.Item;
import com.rodrigocso.groceries.model.Product;
import com.rodrigocso.groceries.repository.ItemRepository;
import com.rodrigocso.groceries.repository.ProductRepository;
import com.rodrigocso.groceries.service.facade.ItemFacade;
import com.rodrigocso.groceries.service.mapper.ItemMapper;
import com.rodrigocso.groceries.test.util.builder.ItemBuilder;
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
public class ItemFacadeTests {
    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private ProductRepository productRepository;

    private ItemFacade itemFacade;
    private ItemMapper itemMapper;

    @BeforeEach
    public void setup() {
        itemMapper = new ItemMapper(productRepository);
        itemFacade = new ItemFacade(productRepository, itemMapper, itemRepository);
    }

    @Test
    public void canFindAll() {
        Product p = productRepository.save(ProductBuilder.builder().build());

        List<Item> items = Lists.newArrayList(
                ItemBuilder.builder().withProduct(p).build(),
                ItemBuilder.builder().withProduct(p).build());

        List<ItemDto> savedItems = itemRepository.saveAll(items).stream()
                .map(itemMapper::toDto).collect(Collectors.toList());

        assertThat(itemFacade.findAll()).usingRecursiveComparison().isEqualTo(savedItems);
    }

    @Test
    public void canFindById() {
        Product p = productRepository.save(ProductBuilder.builder().build());

        List<Item> items = Lists.newArrayList(
                ItemBuilder.builder().withProduct(p).build(),
                ItemBuilder.builder().withProduct(p).build());

        List<ItemDto> savedItems = itemRepository.saveAll(items).stream()
                .map(itemMapper::toDto).collect(Collectors.toList());

        ItemDto retrievedItem = itemFacade.findById(savedItems.get(0).getId()).orElse(null);
        assertThat(retrievedItem).usingRecursiveComparison().isEqualTo(savedItems.get(0));
    }

    @Test
    public void canFindByProductId() {
        Product p = productRepository.save(ProductBuilder.builder().build());

        List<Item> items = Lists.newArrayList(
                ItemBuilder.builder().withProduct(p).build(),
                ItemBuilder.builder().withProduct(p).build());

        List<ItemDto> savedItems = itemRepository.saveAll(items).stream()
                .map(itemMapper::toDto).collect(Collectors.toList());

        List<ItemDto> retrievedItems = itemFacade.findByProductId(p.getId()).orElse(null);
        assertThat(retrievedItems).usingRecursiveComparison().isEqualTo(savedItems);
    }

    @Test
    public void canCreate() {
        Product p = productRepository.save(ProductBuilder.builder().build());
        ItemDto item = itemMapper.toDto(ItemBuilder.builder().withProduct(p).build());
        ItemDto savedItem = itemFacade.create(item);
        assertThat(savedItem.getId()).isNotNull();
    }

    @Test
    public void canUpdate() {
        Product p = productRepository.save(ProductBuilder.builder().build());
        ItemDto item = itemMapper.toDto(itemRepository.save(ItemBuilder.builder().withProduct(p).build()));
        item.setPackageSize(500F);
        assertThat(itemFacade.update(item.getId(), item)).usingRecursiveComparison().isEqualTo(item);
    }
}
