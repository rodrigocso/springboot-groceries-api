package com.rodrigocso.groceries.controller;

import com.rodrigocso.groceries.dto.PurchaseDto;
import com.rodrigocso.groceries.service.facade.PurchaseFacade;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping(path = "/purchases")
public class PurchaseController {
    private final PurchaseFacade purchaseFacade;

    public PurchaseController(PurchaseFacade purchaseFacade) {
        this.purchaseFacade = purchaseFacade;
    }

    @GetMapping
    public ResponseEntity<Iterable<PurchaseDto>> findAll() {
        return ResponseEntity.ok(purchaseFacade.findAll());
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<PurchaseDto> findById(@PathVariable Long id) {
        return purchaseFacade.findById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @GetMapping(path = "/product/{productId}")
    public ResponseEntity<List<PurchaseDto>> findByProductId(@PathVariable Long productId) {
        return purchaseFacade.findByProductId(productId)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<PurchaseDto> create(@RequestBody @Valid PurchaseDto purchase) {
        return ResponseEntity.ok(purchaseFacade.create(purchase));
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<PurchaseDto> update(@PathVariable Long id, @RequestBody @Valid PurchaseDto purchase) {
        return ResponseEntity.ok(purchaseFacade.update(id, purchase));
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<PurchaseDto> delete(@PathVariable Long id) {
        purchaseFacade.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
