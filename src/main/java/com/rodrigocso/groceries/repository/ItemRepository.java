package com.rodrigocso.groceries.repository;

import com.rodrigocso.groceries.model.Item;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<Item, Integer> {
}
