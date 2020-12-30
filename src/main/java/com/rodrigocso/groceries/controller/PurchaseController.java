package com.rodrigocso.groceries.controller;

import com.rodrigocso.groceries.model.Purchase;
import com.rodrigocso.groceries.repository.PurchaseRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/purchases")
public class PurchaseController {
    private final PurchaseRepository purchaseRepository;

    public PurchaseController(PurchaseRepository purchaseRepository) {
        this.purchaseRepository = purchaseRepository;
    }

    @GetMapping
    public ResponseEntity<Iterable<Purchase>> findAll() {
        return ResponseEntity.ok(purchaseRepository.findAll());
    }
}
