package com.rodrigocso.groceries.service.facade;

import com.rodrigocso.groceries.dto.ProductDto;
import com.rodrigocso.groceries.repository.ProductRepository;
import com.rodrigocso.groceries.service.mapper.ProductMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
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

    public ProductDto findById(Integer id) {
        return productRepository.findById(id)
                .map(ProductMapper::toProductDto)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    public List<ProductDto> findByNameContaining(String partialName) {
        return productRepository.findByNameContainingIgnoreCase(partialName).stream()
                .map(ProductMapper::toProductDto)
                .collect(Collectors.toList());
    }

    public ProductDto save(ProductDto dto) {
        return ProductMapper.toProductDto(productRepository.save(ProductMapper.toProduct(dto)));
    }

    public ProductDto update(Integer id, ProductDto dto) {
        return productRepository.findById(id)
                .map((product) -> {
                    dto.setId(product.getId());
                    return dto;
                })
                .map(ProductMapper::toProduct)
                .map(productRepository::save)
                .map(ProductMapper::toProductDto)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }
}
