package com.rodrigocso.groceries.service.mapper;

import com.rodrigocso.groceries.dto.ProductDto;
import com.rodrigocso.groceries.model.Product;

public class ProductMapper {
    public static ProductDto toProductDto(Product product) {
        if (product == null) return null;
        ProductDto dto = new ProductDto();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setBrand(BrandMapper.toBrandDto(product.getBrand()));
        return dto;
    }

    public static Product toProduct(ProductDto dto) {
        if (dto == null) return null;
        Product product = new Product();
        product.setId(dto.getId());
        product.setName(dto.getName());
        product.setBrand(BrandMapper.toBrand(dto.getBrand()));
        return product;
    }
}
