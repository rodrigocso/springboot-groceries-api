package com.rodrigocso.groceries.test.util.builder;

import com.rodrigocso.groceries.model.Brand;
import com.rodrigocso.groceries.model.Product;

public class ProductBuilder {
    private Long id;
    private String name;
    private Brand brand;

    private ProductBuilder() {
        id = null;
        name = "Some product";
        brand = BrandBuilder.builder().withId(1L).build();
    }

    public static ProductBuilder builder() {
        return new ProductBuilder();
    }

    public Product build() {
        Product p = new Product();
        p.setId(id);
        p.setName(name);
        p.setBrand(brand);
        return p;
    }

    public ProductBuilder from(Product template) {
        id = template.getId();
        name = template.getName();
        brand = BrandBuilder.builder().from(template.getBrand()).build();
        return this;
    }

    public ProductBuilder withId(Long id) {
        this.id = id;
        return this;
    }

    public ProductBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public ProductBuilder withBrand(Brand brand) {
        this.brand = brand;
        return this;
    }
}
