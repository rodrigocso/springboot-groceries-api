package com.rodrigocso.groceries.dto;

import javax.validation.constraints.NotBlank;

public class ProductDto {
    private Long id;

    @NotBlank(message = "IS_REQUIRED")
    private String name;

    private Long brandId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getBrandId() {
        return brandId;
    }

    public void setBrandId(Long brandId) {
        this.brandId = brandId;
    }
}
