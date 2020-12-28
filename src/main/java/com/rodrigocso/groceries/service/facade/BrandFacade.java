package com.rodrigocso.groceries.service.facade;

import com.rodrigocso.groceries.dto.BrandDto;
import com.rodrigocso.groceries.repository.BrandRepository;
import com.rodrigocso.groceries.service.mapper.BrandMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BrandFacade {
    private final BrandRepository brandRepository;

    public BrandFacade(BrandRepository brandRepository) {
        this.brandRepository = brandRepository;
    }

    public List<BrandDto> findAll() {
        return brandRepository.findAll().stream()
                .map(BrandMapper::toBrandDto)
                .collect(Collectors.toList());
    }

    public Optional<BrandDto> findById(Integer id) {
        return brandRepository.findById(id).map(BrandMapper::toBrandDto);
    }

    public List<BrandDto> findByNameContaining(String partialName) {
        return brandRepository.findByNameContainingIgnoreCase(partialName).stream()
                .map(BrandMapper::toBrandDto)
                .collect(Collectors.toList());
    }

    public BrandDto save(BrandDto dto) {
        if (brandRepository.findByNameIgnoreCase(dto.getName()).isPresent()) {
            throw new DataIntegrityViolationException("ENTITY_EXISTS");
        } else {
            return BrandMapper.toBrandDto(brandRepository.save(BrandMapper.toBrand(dto)));
        }
    }
}
