package com.rodrigocso.groceries.service.facade;

import com.rodrigocso.groceries.dto.BrandDTO;
import com.rodrigocso.groceries.repository.BrandRepository;
import com.rodrigocso.groceries.service.mapper.BrandMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class BrandFacade {
    private final BrandRepository brandRepository;

    public BrandFacade(BrandRepository brandRepository) {
        this.brandRepository = brandRepository;
    }

    public List<BrandDTO> findAll() {
        return StreamSupport.stream(brandRepository.findAll().spliterator(), false)
                .map(BrandMapper::toBrandDTO)
                .collect(Collectors.toList());
    }

    public BrandDTO findById(Integer id) {
        return brandRepository.findById(id)
                .map(BrandMapper::toBrandDTO)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    public List<BrandDTO> findByNameContaining(String partialName) {
        return StreamSupport
                .stream(brandRepository.findByNameContainingIgnoreCase(partialName).spliterator(), false)
                .map(BrandMapper::toBrandDTO)
                .collect(Collectors.toList());
    }

    public BrandDTO save(BrandDTO dto) {
        return BrandMapper.toBrandDTO(brandRepository.save(BrandMapper.toBrand(dto)));
    }
}
