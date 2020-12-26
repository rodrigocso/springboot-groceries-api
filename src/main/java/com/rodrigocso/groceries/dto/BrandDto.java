package com.rodrigocso.groceries.dto;

import javax.validation.constraints.NotBlank;

public class BrandDto {
    private Integer id;

    @NotBlank(message = "IS_REQUIRED")
    private String name;

    public BrandDto() {}

    public BrandDto(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

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
}
