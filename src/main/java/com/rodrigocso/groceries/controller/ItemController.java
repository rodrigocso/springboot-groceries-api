package com.rodrigocso.groceries.controller;

import com.rodrigocso.groceries.dto.ItemDto;
import com.rodrigocso.groceries.service.facade.ItemFacade;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping
    public ResponseEntity<ItemDto> addItem(@Valid @RequestBody ItemDto dto) {
        return ResponseEntity.ok(itemFacade.save(dto));
    }
}
