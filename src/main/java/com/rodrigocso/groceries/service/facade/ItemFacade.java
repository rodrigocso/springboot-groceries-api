package com.rodrigocso.groceries.service.facade;

import com.rodrigocso.groceries.dto.ItemDto;
import com.rodrigocso.groceries.model.Product;
import com.rodrigocso.groceries.repository.ItemRepository;
import com.rodrigocso.groceries.repository.ProductRepository;
import com.rodrigocso.groceries.service.mapper.ItemMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ItemFacade {
    private final ProductRepository productRepository;
    private final ItemMapper itemMapper;
    private final ItemRepository itemRepository;

    public ItemFacade(ProductRepository productRepository, ItemMapper itemMapper, ItemRepository itemRepository) {
        this.productRepository = productRepository;
        this.itemMapper = itemMapper;
        this.itemRepository = itemRepository;
    }

    public List<ItemDto> findAll() {
        return itemRepository.findAll().stream()
                .map(itemMapper::toDto)
                .collect(Collectors.toList());
    }

    public Optional<ItemDto> findById(Long id) {
        return itemRepository.findById(id).map(itemMapper::toDto);
    }

    public Optional<List<ItemDto>> findByProductId(Long productId) {
        return productRepository.findById(productId)
                .map(Product::getId)
                .map(itemRepository::findByProductId)
                .map(items -> items.stream()
                        .map(itemMapper::toDto)
                        .collect(Collectors.toList()));
    }

    public ItemDto create(ItemDto dto) {
        return itemMapper.toDto(itemRepository.save(itemMapper.toItem(dto)));
    }

    public ItemDto update(Long id, ItemDto dto) {
        if (itemRepository.findById(id).isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        if (!id.equals(dto.getId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        return itemMapper.toDto(itemRepository.save(itemMapper.toItem(dto)));
    }
}
