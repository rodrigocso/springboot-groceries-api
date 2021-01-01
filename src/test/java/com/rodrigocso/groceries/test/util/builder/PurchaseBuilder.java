package com.rodrigocso.groceries.test.util.builder;

import com.rodrigocso.groceries.model.Item;
import com.rodrigocso.groceries.model.Purchase;
import com.rodrigocso.groceries.model.Store;

import java.time.LocalDate;

public class PurchaseBuilder {
    private Long id;
    private Store store;
    private LocalDate transactionDate;
    private Item item;
    private Float quantity;
    private Float price;

    private PurchaseBuilder() {
        id = null;
        store = StoreBuilder.builder().withId(1L).build();
        transactionDate = LocalDate.now();
        item = ItemBuilder.builder().withId(1L).build();
        quantity = 1F;
        price = 1F;
    }

    public static PurchaseBuilder builder() {
        return new PurchaseBuilder();
    }

    public Purchase build() {
        Purchase p = new Purchase();
        p.setId(id);
        p.setStore(store);
        p.setTransactionDate(transactionDate);
        p.setItem(item);
        p.setQuantity(quantity);
        p.setPrice(price);
        return p;
    }

    public PurchaseBuilder withId(Long id) {
        this.id = id;
        return this;
    }

    public PurchaseBuilder withStore(Store store) {
        this.store = store;
        return this;
    }

    public PurchaseBuilder withDate(LocalDate transactionDate) {
        this.transactionDate = transactionDate;
        return this;
    }

    public PurchaseBuilder withItem(Item item) {
        this.item = item;
        return this;
    }

    public PurchaseBuilder withQuantity(Float quantity) {
        this.quantity = quantity;
        return this;
    }

    public PurchaseBuilder withPrice(Float price) {
        this.price = price;
        return this;
    }
}
