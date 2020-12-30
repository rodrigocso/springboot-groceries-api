package com.rodrigocso.groceries.repository;

import com.rodrigocso.groceries.model.Brand;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BrandRepository extends JpaRepository<Brand, Long> {
    Optional<Brand> findByNameIgnoreCase(String name);
    List<Brand> findByNameContainingIgnoreCase(String partialName);
}
