package com.rodrigocso.groceries.service.mapper;

import com.rodrigocso.groceries.dto.BrandDTO;
import com.rodrigocso.groceries.model.Brand;

public class BrandMapper {
    public static BrandDTO toBrandDTO(Brand brand) {
        BrandDTO dto = new BrandDTO();
        dto.setId(brand.getId());
        dto.setName(brand.getName());
        return dto;
    }

    public static Brand toBrand(BrandDTO dto) {
        Brand brand = new Brand();
        brand.setId(dto.getId());
        brand.setName(dto.getName());
        return brand;
    }
}
