package com.rodrigocso.groceries.repository;

import com.rodrigocso.groceries.model.Product;
import com.rodrigocso.groceries.model.Purchase;

import java.util.List;

public interface PurchaseCustomRepository {
    List<Purchase> findByProduct(Product product);
}
