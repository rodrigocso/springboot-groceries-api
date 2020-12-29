package com.rodrigocso.groceries.controller;

import com.rodrigocso.groceries.dto.ItemDto;
import com.rodrigocso.groceries.service.facade.ItemFacade;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "/items")
public class ItemController {
    private final ItemFacade itemFacade;

    public ItemController(ItemFacade itemFacade) {
        this.itemFacade = itemFacade;
    }

    @GetMapping
    public ResponseEntity<Iterable<ItemDto>> findAll() {
        return ResponseEntity.ok(itemFacade.findAll());
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<ItemDto> findItemById(@PathVariable Integer id) {
        return itemFacade.findById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @GetMapping(path = "/product/{productId}")
    public ResponseEntity<Iterable<ItemDto>> findAllItemsByProduct(@PathVariable Integer productId) {
        return ResponseEntity.ok(itemFacade.findAllByProductId(productId));
    }

    @PostMapping
    public ResponseEntity<ItemDto> addItem(@Valid @RequestBody ItemDto item) {
        return ResponseEntity.ok(itemFacade.save(item));
    }

    @PutMapping(path = "{id}")
    public ResponseEntity<ItemDto> updateItem(@Valid @RequestBody ItemDto item, @PathVariable Integer id) {
        if (!id.equals(item.getId())) {
            return ResponseEntity.badRequest().build();
        }
        if (itemFacade.findById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(itemFacade.save(item));
    }
}
