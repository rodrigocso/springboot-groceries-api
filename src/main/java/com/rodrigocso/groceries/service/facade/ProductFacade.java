package com.rodrigocso.groceries.service.facade;

import com.rodrigocso.groceries.dto.ProductDto;
import com.rodrigocso.groceries.model.Brand;
import com.rodrigocso.groceries.model.Product;
import com.rodrigocso.groceries.repository.BrandRepository;
import com.rodrigocso.groceries.repository.ProductRepository;
import com.rodrigocso.groceries.service.mapper.ProductMapper;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductFacade {
    private final BrandRepository brandRepository;
    private final ProductMapper productMapper;
    private final ProductRepository productRepository;

    public ProductFacade(BrandRepository brandRepository, ProductMapper productMapper,
                         ProductRepository productRepository) {
        this.brandRepository = brandRepository;
        this.productMapper = productMapper;
        this.productRepository = productRepository;
    }

    public List<ProductDto> findAll() {
        return productRepository.findAll().stream()
                .map(productMapper::toDto)
                .collect(Collectors.toList());
    }

    public Optional<ProductDto> findById(Long id) {
        return productRepository.findById(id).map(productMapper::toDto);
    }

    public List<ProductDto> findByNameContaining(String partialName) {
        return productRepository.findByNameContainingIgnoreCase(partialName)
                .stream()
                .map(productMapper::toDto)
                .collect(Collectors.toList());
    }

    public Optional<List<ProductDto>> findByBrandId(Long brandId) {
        return brandRepository.findById(brandId)
                .map(Brand::getId)
                .map(productRepository::findByBrandId)
                .map(products -> products.stream()
                        .map(productMapper::toDto)
                        .collect(Collectors.toList()));
    }

    public List<Product> findByBrandIdAndNameContaining(Long brandId, String partialName) {
        Sort.TypedSort<Product> product = Sort.sort(Product.class);
        Sort sort = product.by(Product::getName).ascending();

        return productRepository.findFirst10ByBrandIdAndNameContainingIgnoreCase(brandId, partialName, sort);
    }

    public ProductDto create(ProductDto dto) {
        return productMapper.toDto(productRepository.save(productMapper.toProduct(dto)));
    }

    public ProductDto update(Long id, ProductDto dto) {
        if (productRepository.findById(id).isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        if (!id.equals(dto.getId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        return productMapper.toDto(productRepository.save(productMapper.toProduct(dto)));
    }

    public void deleteById(Long id) {
        if (productRepository.findById(id).isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        productRepository.deleteById(id);
    }
}
