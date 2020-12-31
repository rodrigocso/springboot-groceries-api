package com.rodrigocso.groceries.controller;

import com.rodrigocso.groceries.model.Purchase;
import com.rodrigocso.groceries.repository.PurchaseRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;

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

    @GetMapping(path = "/{id}")
    public ResponseEntity<Purchase> findById(@PathVariable Long id) {
        return purchaseRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<Purchase> create(@RequestBody @Valid Purchase purchase) {
        return ResponseEntity.ok(purchaseRepository.save(purchase));
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<Purchase> update(@PathVariable Long id, @RequestBody @Valid Purchase purchase) {
        if (!id.equals(purchase.getId())) {
            return ResponseEntity.badRequest().build();
        }
        if (purchaseRepository.findById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(purchaseRepository.save(purchase));
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Purchase> delete(@PathVariable Long id) {
        purchaseRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        purchaseRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
