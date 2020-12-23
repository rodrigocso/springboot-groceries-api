package com.rodrigocso.groceries.brand;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import javax.validation.Valid;

@RestController
@RequestMapping(path = "/brands")
public class BrandController {
    private final BrandRepository brandRepository;

    public BrandController(BrandRepository brandRepository) {
        this.brandRepository = brandRepository;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }

    @PostMapping
    public ResponseEntity<Brand> addNewBrand(@Valid @RequestBody Brand newBrand) {
         Brand savedBrand = this.brandRepository.save(newBrand);
         URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                 .path("/{id}")
                 .buildAndExpand(savedBrand.getId())
                 .toUri();
         return ResponseEntity.created(location).body(savedBrand);
    }

    @GetMapping
    public ResponseEntity<Iterable<Brand>> getAllBrands() {
        return ResponseEntity.ok(brandRepository.findAll());
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<Brand> findBrandById(@PathVariable Integer id) {
        Optional<Brand> brand = brandRepository.findById(id);
        return brandRepository.findById(id).isPresent() ?
                ResponseEntity.ok(brand.get()) :
                ResponseEntity.notFound().build();
    }
}
