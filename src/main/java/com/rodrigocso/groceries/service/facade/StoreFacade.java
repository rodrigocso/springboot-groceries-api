package com.rodrigocso.groceries.service.facade;

import com.rodrigocso.groceries.dto.StoreDto;
import com.rodrigocso.groceries.repository.StoreRepository;
import com.rodrigocso.groceries.service.mapper.StoreMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class StoreFacade {
    private final StoreRepository storeRepository;

    public StoreFacade(StoreRepository storeRepository) {
        this.storeRepository = storeRepository;
    }

    public List<StoreDto> findAll() {
        return storeRepository.findAll().stream()
                .map(StoreMapper::toStoreDto)
                .collect(Collectors.toList());
    }

    public Optional<StoreDto> findById(Integer id) {
        return storeRepository.findById(id).map(StoreMapper::toStoreDto);
    }

    public StoreDto save(StoreDto dto) {
        return StoreMapper.toStoreDto(storeRepository.save(StoreMapper.toStore(dto)));
    }

    public List<StoreDto> findByNameContaining(String partialName) {
        return storeRepository.findByNameContainingIgnoreCase(partialName).stream()
                .map(StoreMapper::toStoreDto)
                .collect(Collectors.toList());
    }
}
