package com.rodrigocso.groceries.test.repository;

import com.rodrigocso.groceries.model.*;
import com.rodrigocso.groceries.repository.PurchaseRepository;
import com.rodrigocso.groceries.test.util.builder.*;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@ExtendWith(SpringExtension.class)
public class PurchaseRepositoryTests {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private PurchaseRepository purchaseRepository;

    @Test
    public void canFindByProduct() {
        Store s = StoreBuilder.builder().build();
        Brand b = BrandBuilder.builder().build();

        List<Product> products = Lists.newArrayList(
                ProductBuilder.builder().withBrand(b).build(),
                ProductBuilder.builder().withBrand(b).build(),
                ProductBuilder.builder().withBrand(b).build()
        );
        List<Item> items = Lists.newArrayList(
                ItemBuilder.builder().withProduct(products.get(0)).build(),
                ItemBuilder.builder().withProduct(products.get(0)).build(),
                ItemBuilder.builder().withProduct(products.get(1)).build(),
                ItemBuilder.builder().withProduct(products.get(2)).build()
        );
        List<Purchase> purchases = Lists.newArrayList(
                PurchaseBuilder.builder().withStore(s).withItem(items.get(0)).build(),
                PurchaseBuilder.builder().withStore(s).withItem(items.get(1)).build(),
                PurchaseBuilder.builder().withStore(s).withItem(items.get(1)).build(),
                PurchaseBuilder.builder().withStore(s).withItem(items.get(2)).build(),
                PurchaseBuilder.builder().withStore(s).withItem(items.get(2)).build(),
                PurchaseBuilder.builder().withStore(s).withItem(items.get(3)).build()
        );

        entityManager.persist(s);
        entityManager.persist(b);
        products.forEach(entityManager::persist);
        items.forEach(entityManager::persist);
        purchases.forEach(entityManager::persist);

        assertThat(purchaseRepository.findByProduct(products.get(1)).size()).isEqualTo(2);
    }
}
