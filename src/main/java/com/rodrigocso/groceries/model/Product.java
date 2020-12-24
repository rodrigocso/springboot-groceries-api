package com.rodrigocso.groceries.model;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(unique = true)
    @NotBlank(message = "IS_REQUIRED")
    private String name;

    @ManyToOne
    private Brand brand;

    public Product() {}

    public Product(String name, Brand brand) {
        this.name = name;
        this.brand = brand;
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

    public Brand getBrand() {
        return brand;
    }

    public void setBrand(Brand brand) {
        this.brand = brand;
    }
}
