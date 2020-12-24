package com.rodrigocso.groceries.repository;

import com.rodrigocso.groceries.model.Brand;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface BrandRepository extends CrudRepository<Brand, Integer> {
    Iterable<Brand> findByNameContainingIgnoreCase(String partialName);
}
