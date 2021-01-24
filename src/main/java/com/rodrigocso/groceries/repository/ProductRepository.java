package com.rodrigocso.groceries.repository;

import com.rodrigocso.groceries.model.Product;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByBrandId(Long brandId);
    Optional<Product> findByBrandIdAndName(Long brandId, String name);
    List<Product> findByNameContainingIgnoreCase(String name);
    List<Product> findFirst10ByBrandIdAndNameContainingIgnoreCase(Long brandId, String name, Sort sort);
}
