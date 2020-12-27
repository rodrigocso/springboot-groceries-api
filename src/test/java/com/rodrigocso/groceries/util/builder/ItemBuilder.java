package com.rodrigocso.groceries.util.builder;

import com.rodrigocso.groceries.dto.ItemDto;
import com.rodrigocso.groceries.model.Item;
import com.rodrigocso.groceries.model.Product;
import com.rodrigocso.groceries.service.mapper.ItemMapper;

public class ItemBuilder {
    private Integer id;
    private Product product;
    private Float packageSize;
    private String unit;

    private ItemBuilder() {
        id = 1;
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

    public ItemDto buildDto() {
        return ItemMapper.toItemDto(makeItem());
    }

    public ItemBuilder from(Item template) {
        id = template.getId();
        product = template.getProduct();
        packageSize = template.getPackageSize();
        unit = template.getUnit();
        return this;
    }

    public ItemBuilder from(ItemDto template) {
        return from(ItemMapper.toItem(template));
    }

    public ItemBuilder withId(Integer id) {
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
