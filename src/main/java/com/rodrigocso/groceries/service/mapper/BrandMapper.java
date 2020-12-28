package com.rodrigocso.groceries.service.mapper;

import com.rodrigocso.groceries.dto.BrandDto;
import com.rodrigocso.groceries.model.Brand;

public class BrandMapper {
    public static BrandDto toBrandDto(Brand brand) {
        if (brand == null) return null;
        BrandDto dto = new BrandDto();
        dto.setId(brand.getId());
        dto.setName(brand.getName());
        return dto;
    }

    public static Brand toBrand(BrandDto dto) {
        if (dto == null) return null;
        Brand brand = new Brand();
        brand.setId(dto.getId());
        brand.setName(dto.getName());
        return brand;
    }
}
