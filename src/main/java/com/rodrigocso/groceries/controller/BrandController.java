package com.rodrigocso.groceries.controller;

import com.rodrigocso.groceries.dto.BrandDTO;
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
    public ResponseEntity<Iterable<BrandDTO>> findAll() {
        return ResponseEntity.ok(brandFacade.findAll());
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<BrandDTO> findById(@PathVariable Integer id) {
        return ResponseEntity.ok(brandFacade.findById(id));
    }

    @GetMapping(path = "/search")
    public ResponseEntity<Iterable<BrandDTO>> findByNameContaining(@RequestParam String partialName) {
        return ResponseEntity.ok(brandFacade.findByNameContaining(partialName));
    }

    @PostMapping
    public ResponseEntity<BrandDTO> addBrand(@Valid @RequestBody BrandDTO newBrand) {
        BrandDTO savedBrand = brandFacade.save(newBrand);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedBrand.getId())
                .toUri();
        return ResponseEntity.created(location).body(savedBrand);
    }
}
