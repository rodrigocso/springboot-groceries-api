package com.rodrigocso.groceries.repository;

import com.rodrigocso.groceries.model.Purchase;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface PurchaseRepository extends JpaRepository<Purchase, Long>, PurchaseCustomRepository {
    List<Purchase> findByStoreIdAndTransactionDate(Long storeId, LocalDate transactionDate);
}
