package com.rodrigocso.groceries.controller;

import com.rodrigocso.groceries.dto.StoreDto;
import com.rodrigocso.groceries.service.facade.StoreFacade;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping(path = "/stores")
public class StoreController {
    private final StoreFacade storeFacade;

    public StoreController(StoreFacade storeFacade) {
        this.storeFacade = storeFacade;
    }

    @GetMapping
    public ResponseEntity<Iterable<StoreDto>> findAll() {
        return ResponseEntity.ok(storeFacade.findAll());
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<StoreDto> findById(@PathVariable Integer id) {
        return storeFacade.findById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @GetMapping(path = "/search")
    public ResponseEntity<Iterable<StoreDto>> findByNameContaining(@RequestParam String partialName) {
        return ResponseEntity.ok(storeFacade.findByNameContaining(partialName));
    }

    @PostMapping
    public ResponseEntity<StoreDto> addStore(@Valid @RequestBody StoreDto newStore) {
        StoreDto savedStore = storeFacade.save(newStore);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedStore.getId())
                .toUri();
        return ResponseEntity.created(location).body(savedStore);
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<StoreDto> updateStore(@Valid @RequestBody StoreDto store, @PathVariable Integer id) {
        if (!id.equals(store.getId())) {
            return ResponseEntity.badRequest().build();
        }
        if (storeFacade.findById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(storeFacade.save(store));
    }
}
