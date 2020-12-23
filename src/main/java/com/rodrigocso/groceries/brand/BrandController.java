package com.rodrigocso.groceries.brand;

import com.rodrigocso.groceries.AbstractController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import javax.validation.Valid;

@RestController
@RequestMapping(path = "/brands")
public class BrandController extends AbstractController {
    private final BrandRepository brandRepository;

    public BrandController(BrandRepository brandRepository) {
        this.brandRepository = brandRepository;
    }

    @GetMapping
    public ResponseEntity<Iterable<Brand>> getAllBrands() {
        return ResponseEntity.ok(brandRepository.findAll());
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<Brand> findBrandById(@PathVariable Integer id) {
        return brandRepository
                .findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping(path = "/search")
    public ResponseEntity<Brand> findBrandByName(@RequestParam String name) {
        return brandRepository
                .findByNameIgnoreCase(name)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Brand> addBrand(@Valid @RequestBody Brand newBrand) {
        Brand savedBrand = this.brandRepository.save(newBrand);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedBrand.getId())
                .toUri();
        return ResponseEntity.created(location).body(savedBrand);
    }
}
