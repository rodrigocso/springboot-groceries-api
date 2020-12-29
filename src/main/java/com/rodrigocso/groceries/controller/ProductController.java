package com.rodrigocso.groceries.controller;

import com.rodrigocso.groceries.dto.ProductDto;
import com.rodrigocso.groceries.service.facade.ProductFacade;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping(path = "/products")
public class ProductController {
    private final ProductFacade productFacade;

    public ProductController(ProductFacade productFacade) {
        this.productFacade = productFacade;
    }

    @GetMapping
    public ResponseEntity<Iterable<ProductDto>> getAllProducts() {
        return ResponseEntity.ok(productFacade.findAll());
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<ProductDto> findProductById(@PathVariable Integer id) {
        return productFacade.findById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @GetMapping(path = "/search")
    public ResponseEntity<Iterable<ProductDto>> findProductByName(@RequestParam String name) {
        return ResponseEntity.ok(productFacade.findByNameContaining(name));
    }

    @GetMapping(path = "/brand/{brandId}")
    public ResponseEntity<Iterable<ProductDto>> findAllProductsByBrand(@PathVariable Integer brandId) {
        return ResponseEntity.ok(productFacade.findByBrandId(brandId));
    }

    @PostMapping
    public ResponseEntity<ProductDto> addProduct(@Valid @RequestBody ProductDto newProduct) {
        ProductDto savedProduct = this.productFacade.save(newProduct);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedProduct.getId())
                .toUri();
        return ResponseEntity.created(location).body(savedProduct);
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<ProductDto> updateProduct(@Valid @RequestBody ProductDto product, @PathVariable Integer id) {
        if (!id.equals(product.getId())) {
            return ResponseEntity.badRequest().build();
        }
        if (productFacade.findById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(productFacade.save(product));
    }
}
