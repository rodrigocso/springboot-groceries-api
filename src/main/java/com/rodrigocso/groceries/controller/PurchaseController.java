package com.rodrigocso.groceries.controller;

import com.rodrigocso.groceries.dto.PurchaseDto;
import com.rodrigocso.groceries.service.facade.PurchaseFacade;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/purchases")
public class PurchaseController {
    private PurchaseFacade purchaseFacade;

    public PurchaseController(PurchaseFacade purchaseFacade) {
        this.purchaseFacade = purchaseFacade;
    }

    @GetMapping
    public ResponseEntity<Iterable<PurchaseDto>> findAll() {
        return ResponseEntity.ok(purchaseFacade.findAll());
    }
}
