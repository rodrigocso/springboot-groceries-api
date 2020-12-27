package com.rodrigocso.groceries.repository;

import com.rodrigocso.groceries.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Integer> {
    List<Product> findByBrandId(Integer brandId);
    Optional<Product> findByBrandIdAndName(Integer brandId, String name);
    List<Product> findByNameContainingIgnoreCase(String name);
}
