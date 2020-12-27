package com.rodrigocso.groceries.dto;

import javax.validation.Valid;

public class ItemDto {
    private Integer id;

    @Valid
    private ProductDto product;

    private Float packageSize;

    private String unit;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public ProductDto getProduct() {
        return product;
    }

    public void setProduct(ProductDto product) {
        this.product = product;
    }

    public Float getPackageSize() {
        return packageSize;
    }

    public void setPackageSize(Float packageSize) {
        this.packageSize = packageSize;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
}
