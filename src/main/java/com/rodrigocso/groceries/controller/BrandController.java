package com.rodrigocso.groceries.controller;

import com.rodrigocso.groceries.model.Brand;
import com.rodrigocso.groceries.repository.BrandRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import javax.validation.Valid;

@CrossOrigin
@RestController
@RequestMapping(path = "/brands")
public class BrandController {
    private final BrandRepository brandRepository;

    public BrandController(BrandRepository brandRepository) {
        this.brandRepository = brandRepository;
    }

    @GetMapping
    public ResponseEntity<Iterable<Brand>> findAll() {
        return ResponseEntity.ok(brandRepository.findAll());
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<Brand> findById(@PathVariable Long id) {
        return brandRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @GetMapping(path = "/search")
    public ResponseEntity<Iterable<Brand>> findByNameContaining(@RequestParam String partialName) {
        return ResponseEntity.ok(brandRepository.findByNameContainingIgnoreCase(partialName));
    }

    @PostMapping
    public ResponseEntity<Brand> addBrand(@Valid @RequestBody Brand newBrand) {
        Brand savedBrand = brandRepository.save(newBrand);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedBrand.getId())
                .toUri();
        return ResponseEntity.created(location).body(savedBrand);
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<Brand> updateBrand(@Valid @RequestBody Brand brand, @PathVariable Long id) {
        if (!id.equals(brand.getId())) {
            return ResponseEntity.badRequest().build();
        }
        if (brandRepository.findById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(brandRepository.save(brand));
    }
}
