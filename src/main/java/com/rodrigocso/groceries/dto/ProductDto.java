package com.rodrigocso.groceries.dto;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

public class ProductDto {
    private Integer id;

    @NotBlank(message = "IS_REQUIRED")
    private String name;

    @Valid
    private BrandDto brand;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BrandDto getBrand() {
        return brand;
    }

    public void setBrand(BrandDto brand) {
        this.brand = brand;
    }
}
