package com.rodrigocso.groceries.controller;

import com.rodrigocso.groceries.dto.ItemDto;
import com.rodrigocso.groceries.service.facade.ItemFacade;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "/items")
public class ItemController {
    private final ItemFacade itemFacade;

    public ItemController(ItemFacade itemFacade) {
        this.itemFacade = itemFacade;
    }

    @GetMapping
    public ResponseEntity<List<ItemDto>> findAll() {
        return ResponseEntity.ok(itemFacade.findAll());
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<ItemDto> findById(@PathVariable Long id) {
        return itemFacade.findById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @GetMapping(path = "/product/{productId}")
    public ResponseEntity<List<ItemDto>> findByProductId(@PathVariable Long productId) {
        return itemFacade.findByProductId(productId)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<ItemDto> create(@Valid @RequestBody ItemDto item) {
        return ResponseEntity.ok(itemFacade.create(item));
    }

    @PutMapping(path = "{id}")
    public ResponseEntity<ItemDto> update(@Valid @RequestBody ItemDto item, @PathVariable Long id) {
        return ResponseEntity.ok(itemFacade.update(id, item));
    }
}
