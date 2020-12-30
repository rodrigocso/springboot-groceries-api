package com.rodrigocso.groceries.model;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

@Entity
public class Item {
    @Id
    @GeneratedValue
    private Long id;

    @NotNull(message = "IS_REQUIRED")
    @ManyToOne(fetch = FetchType.LAZY)
    private Product product;

    @NotNull(message = "IS_REQUIRED")
    @Positive(message = "HAS_TO_BE_POSITIVE")
    private Float packageSize;

    @NotBlank(message = "IS_REQUIRED")
    @Size(max = 3, message = "MAX_3_CHARS")
    private String unit;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
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
