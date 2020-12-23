package com.rodrigocso.groceries.brand;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(path = "/brands")
public class BrandController {
    private final BrandRepository brandRepository;

    public BrandController(BrandRepository brandRepository) {
        this.brandRepository = brandRepository;
    }

    @PostMapping
    public @ResponseBody Brand addNewBrand(@RequestBody Brand newBrand) {
        return this.brandRepository.save(newBrand);
    }

    @GetMapping
    public @ResponseBody Iterable<Brand> getAllBrands() {
        return this.brandRepository.findAll();
    }
}
