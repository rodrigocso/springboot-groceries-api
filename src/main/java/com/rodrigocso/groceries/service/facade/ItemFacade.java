package com.rodrigocso.groceries.service.facade;

import com.rodrigocso.groceries.dto.ItemDto;
import com.rodrigocso.groceries.repository.ItemRepository;
import com.rodrigocso.groceries.service.mapper.ItemMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ItemFacade {
    private final ItemRepository itemRepository;

    public ItemFacade(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    public List<ItemDto> findAll() {
        return itemRepository.findAll().stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    public ItemDto save(ItemDto dto) {
        return ItemMapper.toItemDto(itemRepository.save(ItemMapper.toItem(dto)));
    }
}
