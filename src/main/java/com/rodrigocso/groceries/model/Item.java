package com.rodrigocso.groceries.model;

import javax.persistence.*;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

@Entity
public class Item {
    @Id
    @GeneratedValue
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Product product;

    @Positive(message = "HAS_TO_BE_POSITIVE")
    private Float packageSize;

    @Size(max = 3, message = "MAX_3_CHARS")
    private String unit;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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
