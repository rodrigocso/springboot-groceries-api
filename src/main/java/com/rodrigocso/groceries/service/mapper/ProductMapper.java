package com.rodrigocso.groceries.service.mapper;

import com.rodrigocso.groceries.dto.ProductDto;
import com.rodrigocso.groceries.model.Product;
import com.rodrigocso.groceries.repository.BrandRepository;
import org.springframework.stereotype.Service;

@Service
public class ProductMapper {
    private final BrandRepository brandRepository;

    public ProductMapper(BrandRepository brandRepository) {
        this.brandRepository = brandRepository;
    }

    public ProductDto toDto(Product product) {
        ProductDto dto = new ProductDto();
        dto.setId(product.getId());
        dto.setName(product.getName());
        if (product.getBrand() != null) {
            dto.setBrandId(product.getBrand().getId());
        }
        return dto;
    }

    public Product toProduct(ProductDto dto) {
        Product product = new Product();
        product.setId(dto.getId());
        product.setName(dto.getName());
        product.setBrand(brandRepository.findById(dto.getBrandId()).orElse(null));
        return product;
    }
}
