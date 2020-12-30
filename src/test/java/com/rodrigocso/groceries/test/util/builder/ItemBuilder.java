package com.rodrigocso.groceries.test.util.builder;

import com.rodrigocso.groceries.model.Item;
import com.rodrigocso.groceries.model.Product;

public class ItemBuilder {
    private Long id;
    private Product product;
    private Float packageSize;
    private String unit;

    private ItemBuilder() {
        id = null;
        product = ProductBuilder.builder().build();
        packageSize = 1F;
        unit = "kg";
    }

    public static ItemBuilder builder() {
        return new ItemBuilder();
    }

    public Item build() {
        return makeItem();
    }

    public ItemBuilder from(Item template) {
        id = template.getId();
        product = template.getProduct();
        packageSize = template.getPackageSize();
        unit = template.getUnit();
        return this;
    }


    public ItemBuilder withId(Long id) {
        this.id = id;
        return this;
    }

    public ItemBuilder withProduct(Product product) {
        this.product = product;
        return this;
    }

    public ItemBuilder withPackageSize(Float packageSize) {
        this.packageSize = packageSize;
        return this;
    }

    public ItemBuilder withUnit(String unit) {
        this.unit = unit;
        return this;
    }

    private Item makeItem() {
        Item item = new Item();
        item.setId(id);
        item.setProduct(product);
        item.setPackageSize(packageSize);
        item.setUnit(unit);
        return item;
    }
}
