package com.rodrigocso.groceries.controller;

import com.rodrigocso.groceries.model.Item;
import com.rodrigocso.groceries.repository.ItemRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "/items")
public class ItemController {
    private final ItemRepository itemRepository;

    public ItemController(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    @GetMapping
    public ResponseEntity<Iterable<Item>> findAll() {
        return ResponseEntity.ok(itemRepository.findAll());
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<Item> findItemById(@PathVariable Long id) {
        return itemRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @GetMapping(path = "/product/{productId}")
    public ResponseEntity<Iterable<Item>> findAllItemsByProduct(@PathVariable Long productId) {
        return ResponseEntity.ok(itemRepository.findByProductId(productId));
    }

    @PostMapping
    public ResponseEntity<Item> addItem(@Valid @RequestBody Item item) {
        return ResponseEntity.ok(itemRepository.save(item));
    }

    @PutMapping(path = "{id}")
    public ResponseEntity<Item> updateItem(@Valid @RequestBody Item item, @PathVariable Long id) {
        if (!id.equals(item.getId())) {
            return ResponseEntity.badRequest().build();
        }
        if (itemRepository.findById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(itemRepository.save(item));
    }
}
