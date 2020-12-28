package com.rodrigocso.groceries.dto;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

public class ItemDto {
    private Integer id;

    @NotNull(message = "IS_REQUIRED")
    @Valid
    private ProductDto product;

    @NotNull
    @Positive(message = "HAS_TO_BE_POSITIVE")
    private Float packageSize;

    @NotBlank(message = "IS_REQUIRED")
    @Size(max = 3, message = "MAX_3_CHARS")
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
