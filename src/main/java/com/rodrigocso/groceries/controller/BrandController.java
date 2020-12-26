package com.rodrigocso.groceries.controller;

import com.rodrigocso.groceries.dto.BrandDto;
import com.rodrigocso.groceries.service.facade.BrandFacade;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import javax.validation.Valid;

@RestController
@RequestMapping(path = "/brands")
public class BrandController {
    private final BrandFacade brandFacade;

    public BrandController(BrandFacade brandFacade) {
        this.brandFacade = brandFacade;
    }

    @GetMapping
    public ResponseEntity<Iterable<BrandDto>> findAll() {
        return ResponseEntity.ok(brandFacade.findAll());
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<BrandDto> findById(@PathVariable Integer id) {
        return ResponseEntity.ok(brandFacade.findById(id));
    }

    @GetMapping(path = "/search")
    public ResponseEntity<Iterable<BrandDto>> findByNameContaining(@RequestParam String partialName) {
        return ResponseEntity.ok(brandFacade.findByNameContaining(partialName));
    }

    @PostMapping
    public ResponseEntity<BrandDto> addBrand(@Valid @RequestBody BrandDto newBrand) {
        BrandDto savedBrand = brandFacade.save(newBrand);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedBrand.getId())
                .toUri();
        return ResponseEntity.created(location).body(savedBrand);
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<BrandDto> updateBrand(@Valid @RequestBody BrandDto brand, @PathVariable Integer id) {
        return ResponseEntity.ok(brandFacade.update(id, brand));
    }
}
