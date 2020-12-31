package com.rodrigocso.groceries.controller;

import com.rodrigocso.groceries.dto.ProductDto;
import com.rodrigocso.groceries.repository.ProductRepository;
import com.rodrigocso.groceries.service.facade.ProductFacade;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(path = "/products")
public class ProductController {
    private final ProductFacade productFacade;
    private final ProductRepository productRepository;

    public ProductController(ProductFacade productFacade, ProductRepository productRepository) {
        this.productFacade = productFacade;
        this.productRepository = productRepository;
    }

    @GetMapping
    public ResponseEntity<List<ProductDto>> findAll() {
        return ResponseEntity.ok(productFacade.findAll());
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<ProductDto> findById(@PathVariable Long id) {
        return productFacade.findById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @GetMapping(path = "/search")
    public ResponseEntity<List<ProductDto>> findByNameContaining(@RequestParam String name) {
        return ResponseEntity.ok(productFacade.findByNameContaining(name));
    }

    @GetMapping(path = "/brand/{brandId}")
    public ResponseEntity<List<ProductDto>> findByBrandId(@PathVariable Long brandId) {
        return productFacade.findByBrandId(brandId)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<ProductDto> create(@Valid @RequestBody ProductDto newProduct) {
        ProductDto product = productFacade.create(newProduct);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(product.getId())
                .toUri();
        return ResponseEntity.created(location).body(product);
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<ProductDto> update(@Valid @RequestBody ProductDto product, @PathVariable Long id) {
        return ResponseEntity.ok(productFacade.update(id, product));
    }
}
