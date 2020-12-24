package com.rodrigocso.groceries.repository;

import com.rodrigocso.groceries.model.Product;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface ProductRepository extends CrudRepository<Product, Integer> {
    Optional<Product> findByNameIgnoreCase(String name);
}
