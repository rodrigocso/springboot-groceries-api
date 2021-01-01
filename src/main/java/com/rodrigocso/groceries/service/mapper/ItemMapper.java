package com.rodrigocso.groceries.service.mapper;

import com.rodrigocso.groceries.dto.ItemDto;
import com.rodrigocso.groceries.model.Item;
import com.rodrigocso.groceries.repository.ProductRepository;
import org.springframework.stereotype.Component;

@Component
public class ItemMapper {
    private final ProductRepository productRepository;

    public ItemMapper(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public ItemDto toDto(Item item) {
        ItemDto dto = new ItemDto();
        dto.setId(item.getId());
        dto.setPackageSize(item.getPackageSize());
        dto.setUnit(item.getUnit());
        if (item.getProduct() != null) {
            dto.setProductId(item.getProduct().getId());
        }
        return dto;
    }

    public Item toItem(ItemDto dto) {
        Item item = new Item();
        item.setId(dto.getId());
        item.setProduct(productRepository.findById(dto.getProductId()).orElse(null));
        item.setPackageSize(dto.getPackageSize());
        item.setUnit(dto.getUnit());
        return item;
    }
}
