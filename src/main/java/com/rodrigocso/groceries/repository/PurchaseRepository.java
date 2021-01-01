package com.rodrigocso.groceries.repository;

import com.rodrigocso.groceries.model.Purchase;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PurchaseRepository extends JpaRepository<Purchase, Long>, PurchaseCustomRepository {
}
