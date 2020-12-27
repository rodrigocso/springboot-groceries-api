package com.rodrigocso.groceries.service.mapper;

import com.rodrigocso.groceries.dto.ItemDto;
import com.rodrigocso.groceries.model.Item;

public class ItemMapper {
    public static ItemDto toItemDto(Item item) {
        ItemDto dto = new ItemDto();
        dto.setId(item.getId());
        dto.setProduct(ProductMapper.toProductDto(item.getProduct()));
        dto.setPackageSize(item.getPackageSize());
        dto.setUnit(item.getUnit());
        return dto;
    }

    public static Item toItem(ItemDto dto) {
        Item item = new Item();
        item.setId(dto.getId());
        item.setProduct(ProductMapper.toProduct(dto.getProduct()));
        item.setPackageSize(dto.getPackageSize());
        item.setUnit(dto.getUnit());
        return item;
    }
}
