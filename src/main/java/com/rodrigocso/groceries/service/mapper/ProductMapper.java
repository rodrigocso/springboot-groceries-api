package com.rodrigocso.groceries.service.mapper;

import com.rodrigocso.groceries.dto.ProductDTO;
import com.rodrigocso.groceries.model.Product;

public class ProductMapper {
    public static ProductDTO toProductDTO(Product product) {
        ProductDTO dto = new ProductDTO();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setBrand(BrandMapper.toBrandDTO(product.getBrand()));
        return dto;
    }

    public static Product toProduct(ProductDTO dto) {
        Product product = new Product();
        product.setId(dto.getId());
        product.setName(dto.getName());
        product.setBrand(BrandMapper.toBrand(dto.getBrand()));
        return product;
    }
}
