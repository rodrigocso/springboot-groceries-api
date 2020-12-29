package com.rodrigocso.groceries.repository;

import com.rodrigocso.groceries.model.Store;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StoreRepository extends JpaRepository<Store, Integer> {
    Optional<Store> findByNameIgnoreCase(String name);
    List<Store> findByNameContainingIgnoreCase(String partialName);
}
