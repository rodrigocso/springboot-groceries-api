package com.rodrigocso.groceries.service.facade;

import com.rodrigocso.groceries.dto.ProductDto;
import com.rodrigocso.groceries.repository.ProductRepository;
import com.rodrigocso.groceries.service.mapper.ProductMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductFacade {
    private final ProductRepository productRepository;

    public ProductFacade(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<ProductDto> findAll() {
        return productRepository.findAll().stream()
                .map(ProductMapper::toProductDto)
                .collect(Collectors.toList());
    }

    public Optional<ProductDto> findById(Integer id) {
        return productRepository.findById(id).map(ProductMapper::toProductDto);
    }

    public List<ProductDto> findByNameContaining(String partialName) {
        return productRepository.findByNameContainingIgnoreCase(partialName).stream()
                .map(ProductMapper::toProductDto)
                .collect(Collectors.toList());
    }

    public List<ProductDto> findByBrandId(Integer brandId) {
        return productRepository.findByBrandId(brandId).stream()
                .map(ProductMapper::toProductDto)
                .collect(Collectors.toList());
    }

    public ProductDto save(ProductDto dto) {
        if (isDuplicatedEntity(dto)) {
            throw new DataIntegrityViolationException("DUPLICATED_ENTITY");
        }
        return ProductMapper.toProductDto(productRepository.save(ProductMapper.toProduct(dto)));
    }

    public List<ProductDto> saveAll(List<ProductDto> dtoList) {
        if (dtoList == null || dtoList.size() == 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        return productRepository.saveAll(dtoList
                .stream().filter(dto -> !isDuplicatedEntity(dto))
                .map(ProductMapper::toProduct)
                .collect(Collectors.toList()))
                .stream().map(ProductMapper::toProductDto)
                .collect(Collectors.toList());
    }

    private boolean isDuplicatedEntity(ProductDto dto) {
        return productRepository.findByBrandIdAndName(dto.getBrand().getId(), dto.getName()).isPresent();
    }
}
