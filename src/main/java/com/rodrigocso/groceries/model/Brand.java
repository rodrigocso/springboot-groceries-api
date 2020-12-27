package com.rodrigocso.groceries.model;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
public class Brand {
    @Id
    @GeneratedValue
    private Integer id;

    @Column(unique = true)
    @NotBlank(message = "IS_REQUIRED")
    private String name;

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
