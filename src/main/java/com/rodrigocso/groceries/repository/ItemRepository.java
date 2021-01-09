package com.rodrigocso.groceries.repository;

import com.rodrigocso.groceries.model.Item;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long>, ItemCustomRepository {
    List<Item> findByProductId(Long productId);
}
