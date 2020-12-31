package com.rodrigocso.groceries.test.util.builder;

import com.rodrigocso.groceries.model.Brand;

public class BrandBuilder {
    private Long id;
    private String name;

    private BrandBuilder() {
        id = null;
        name = "Some brand";
    }

    public static BrandBuilder builder() {
        return new BrandBuilder();
    }

    public Brand build() {
        Brand b = new Brand();
        b.setId(id);
        b.setName(name);
        return b;
    }

    public BrandBuilder from(Brand template) {
        id = template.getId();
        name = template.getName();
        return this;
    }

    public BrandBuilder withId(Long id) {
        this.id = id;
        return this;
    }

    public BrandBuilder withName(String name) {
        this.name = name;
        return this;
    }
}
