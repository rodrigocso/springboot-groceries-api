package com.rodrigocso.groceries.repository;

import com.rodrigocso.groceries.model.Brand;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BrandRepository extends JpaRepository<Brand, Integer> {
    List<Brand> findByNameContainingIgnoreCase(String partialName);
}
