package com.rodrigocso.groceries.test.service.facade;

import com.rodrigocso.groceries.dto.PurchaseDto;
import com.rodrigocso.groceries.dto.PurchaseResponse;
import com.rodrigocso.groceries.model.Item;
import com.rodrigocso.groceries.model.Product;
import com.rodrigocso.groceries.model.Purchase;
import com.rodrigocso.groceries.model.Store;
import com.rodrigocso.groceries.repository.ItemRepository;
import com.rodrigocso.groceries.repository.ProductRepository;
import com.rodrigocso.groceries.repository.PurchaseRepository;
import com.rodrigocso.groceries.repository.StoreRepository;
import com.rodrigocso.groceries.service.facade.PurchaseFacade;
import com.rodrigocso.groceries.service.mapper.ItemMapper;
import com.rodrigocso.groceries.service.mapper.PurchaseMapper;
import com.rodrigocso.groceries.test.util.builder.ItemBuilder;
import com.rodrigocso.groceries.test.util.builder.ProductBuilder;
import com.rodrigocso.groceries.test.util.builder.PurchaseBuilder;
import com.rodrigocso.groceries.test.util.builder.StoreBuilder;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
public class PurchaseFacadeTests {
    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private PurchaseRepository purchaseRepository;

    @Mock
    private ItemMapper itemMapper;

    private PurchaseFacade purchaseFacade;
    private PurchaseMapper purchaseMapper;

    @BeforeEach
    public void setup() {
        purchaseMapper = new PurchaseMapper(itemMapper, storeRepository, itemRepository);
        purchaseFacade = new PurchaseFacade(productRepository, purchaseRepository, purchaseMapper);
    }

    @Test
    public void canFindAll() {
        Store s = storeRepository.save(StoreBuilder.builder().build());
        Product p = productRepository.save(ProductBuilder.builder().build());
        Item i = itemRepository.save(ItemBuilder.builder().withProduct(p).build());

        purchaseRepository.saveAll(Lists.newArrayList(
                PurchaseBuilder.builder().withStore(s).withItem(i).build(),
                PurchaseBuilder.builder().withStore(s).withItem(i).build(),
                PurchaseBuilder.builder().withStore(s).withItem(i).build(),
                PurchaseBuilder.builder().withStore(s).withItem(i).build()));

        assertThat(purchaseFacade.findAll().size()).isEqualTo(4);
    }

    @Test
    public void canFindById() {
        Store s = storeRepository.save(StoreBuilder.builder().build());
        Product p = productRepository.save(ProductBuilder.builder().build());
        Item i = itemRepository.save(ItemBuilder.builder().withProduct(p).build());
        Purchase pu = purchaseRepository.save(PurchaseBuilder.builder().withStore(s).withItem(i).build());

        assertThat(purchaseFacade.findById(pu.getId())).isNotNull();
    }

    @Test
    public void canFindByProductId() {
        Store s = storeRepository.save(StoreBuilder.builder().build());
        Product p1 = productRepository.save(ProductBuilder.builder().build());
        Product p2 = productRepository.save(ProductBuilder.builder().build());
        Item i1 = itemRepository.save(ItemBuilder.builder().withProduct(p1).build());
        Item i2 = itemRepository.save(ItemBuilder.builder().withProduct(p1).build());
        Item i3 = itemRepository.save(ItemBuilder.builder().withProduct(p2).build());

        purchaseRepository.saveAll(Lists.newArrayList(
                PurchaseBuilder.builder().withStore(s).withItem(i1).build(),
                PurchaseBuilder.builder().withStore(s).withItem(i1).build(),
                PurchaseBuilder.builder().withStore(s).withItem(i2).build(),
                PurchaseBuilder.builder().withStore(s).withItem(i2).build(),
                PurchaseBuilder.builder().withStore(s).withItem(i3).build()));

        assertThat(purchaseFacade.findByProductId(p1.getId()).orElse(Lists.emptyList()).size()).isEqualTo(4);
    }

    @Test
    public void canCreate() {
        Store s = storeRepository.save(StoreBuilder.builder().build());
        Product p = productRepository.save(ProductBuilder.builder().build());
        Item i = itemRepository.save(ItemBuilder.builder().withProduct(p).build());
        PurchaseDto dto = purchaseMapper.toDto(PurchaseBuilder.builder().withStore(s).withItem(i).build());

        assertThat(purchaseFacade.create(dto).getId()).isNotNull();
    }

    @Test
    public void canUpdate() {
        Store s = storeRepository.save(StoreBuilder.builder().build());
        Product p = productRepository.save(ProductBuilder.builder().build());
        Item i = itemRepository.save(ItemBuilder.builder().withProduct(p).build());
        PurchaseDto dto = purchaseMapper.toDto(
                purchaseRepository.save(PurchaseBuilder.builder().withStore(s).withItem(i).build()));
        dto.setPrice(15F);
        assertThat(purchaseFacade.update(dto.getId(), dto).getPrice()).isEqualTo(15F);
    }

    @Test
    public void canFindByStoreIdAndTransactionDate() {
        Store s = storeRepository.save(StoreBuilder.builder().build());
        Store s2 = storeRepository.save(StoreBuilder.builder().build());
        Product p = productRepository.save(ProductBuilder.builder().build());
        Item i = itemRepository.save(ItemBuilder.builder().withProduct(p).build());
        purchaseRepository.saveAll(Lists.newArrayList(
                PurchaseBuilder.builder()
                        .withStore(s)
                        .withItem(i)
                        .withDate(LocalDate.of(2020, 10, 12))
                        .build(),
                PurchaseBuilder.builder()
                        .withStore(s)
                        .withItem(i)
                        .withDate(LocalDate.of(2020, 10, 12))
                        .build(),
                PurchaseBuilder.builder()
                        .withStore(s)
                        .withItem(i)
                        .withDate(LocalDate.of(2020, 11, 20))
                        .build(),
                PurchaseBuilder.builder()
                        .withStore(s2)
                        .withItem(i)
                        .withDate(LocalDate.of(2020, 10, 12))
                        .build()
        ));
        List<PurchaseResponse> purchases = purchaseRepository
                .findByStoreIdAndTransactionDate(s.getId(), LocalDate.of(2020, 10, 12))
                .stream()
                .map(purchaseMapper::toPurchaseResponse)
                .collect(Collectors.toList());

        assertThat(purchases.size()).isEqualTo(2);
    }
}
