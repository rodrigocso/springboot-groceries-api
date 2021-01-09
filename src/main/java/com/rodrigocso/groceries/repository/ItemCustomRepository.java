package com.rodrigocso.groceries.repository;

import com.rodrigocso.groceries.model.Item;

import java.util.List;

public interface ItemCustomRepository {
    List<Item> findByBrandOrProductName(String search);
}
