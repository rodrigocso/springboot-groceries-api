package com.rodrigocso.groceries.util.builder;

import com.rodrigocso.groceries.dto.ProductDto;
import com.rodrigocso.groceries.model.Brand;
import com.rodrigocso.groceries.model.Product;
import com.rodrigocso.groceries.service.mapper.ProductMapper;

public class ProductBuilder {
    private Integer id;
    private String name;
    private Brand brand;

    private ProductBuilder() {
        id = 1;
        name = "Some product";
        brand = BrandBuilder.builder().build();
    }

    public static ProductBuilder builder() {
        return new ProductBuilder();
    }

    public Product build() {
        return makeProduct();
    }

    public ProductDto buildDto() {
        return ProductMapper.toProductDto(makeProduct());
    }

    public ProductBuilder from(Product template) {
        this.id = template.getId();
        this.name = template.getName();
        this.brand = BrandBuilder.builder().from(template.getBrand()).build();
        return this;
    }

    public ProductBuilder from(ProductDto template) {
        return from(ProductMapper.toProduct(template));
    }

    public ProductBuilder withId(Integer id) {
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

    private Product makeProduct() {
        Product p = new Product();
        p.setId(id);
        p.setName(name);
        p.setBrand(brand);
        return p;
    }
}
