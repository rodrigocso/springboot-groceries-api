package com.rodrigocso.groceries.brand;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface BrandRepository extends CrudRepository<Brand, Integer> {
     Optional<Brand> findByNameIgnoreCase(String name);
}
