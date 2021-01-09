package com.rodrigocso.groceries.controller;

import com.rodrigocso.groceries.model.Store;
import com.rodrigocso.groceries.repository.StoreRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

@CrossOrigin
@RestController
@RequestMapping(path = "/stores")
public class StoreController {
    private final StoreRepository storeRepository;

    public StoreController(StoreRepository storeRepository) {
        this.storeRepository = storeRepository;
    }

    @GetMapping
    public ResponseEntity<Iterable<Store>> findAll() {
        return ResponseEntity.ok(storeRepository.findAll());
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<Store> findById(@PathVariable Long id) {
        return storeRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @GetMapping(path = "/search")
    public ResponseEntity<Iterable<Store>> findByNameContaining(@RequestParam String partialName) {
        return ResponseEntity.ok(storeRepository.findByNameContainingIgnoreCase(partialName));
    }

    @PostMapping
    public ResponseEntity<Store> addStore(@Valid @RequestBody Store newStore) {
        Store savedStore = storeRepository.save(newStore);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedStore.getId())
                .toUri();
        return ResponseEntity.created(location).body(savedStore);
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<Store> updateStore(@Valid @RequestBody Store store, @PathVariable Long id) {
        if (!id.equals(store.getId())) {
            return ResponseEntity.badRequest().build();
        }
        if (storeRepository.findById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(storeRepository.save(store));
    }
}
