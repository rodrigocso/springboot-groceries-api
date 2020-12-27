package com.rodrigocso.groceries.util.builder;

import com.rodrigocso.groceries.dto.BrandDto;
import com.rodrigocso.groceries.model.Brand;
import com.rodrigocso.groceries.service.mapper.BrandMapper;

public class BrandBuilder {
    private Integer id;
    private String name;

    private BrandBuilder() {
        id = 1;
        name = "Some brand";
    }

    public static BrandBuilder builder() {
        return new BrandBuilder();
    }

    public Brand build() {
        return makeBrand();
    }

    public BrandDto buildDto() {
        return BrandMapper.toBrandDto(makeBrand());
    }

    public BrandBuilder withId(Integer id) {
        this.id = id;
        return this;
    }

    public BrandBuilder withName(String name) {
        this.name = name;
        return this;
    }

    private Brand makeBrand() {
        Brand b = new Brand();
        b.setId(id);
        b.setName(name);
        return b;
    }
}
