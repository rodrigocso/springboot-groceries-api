package com.rodrigocso.groceries.service.facade;

import com.rodrigocso.groceries.dto.PurchaseDto;
import com.rodrigocso.groceries.dto.PurchaseResponse;
import com.rodrigocso.groceries.repository.ProductRepository;
import com.rodrigocso.groceries.repository.PurchaseRepository;
import com.rodrigocso.groceries.service.mapper.PurchaseMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PurchaseFacade {
    private final ProductRepository productRepository;
    private final PurchaseRepository purchaseRepository;
    private final PurchaseMapper purchaseMapper;

    public PurchaseFacade(ProductRepository productRepository, PurchaseRepository purchaseRepository,
                          PurchaseMapper purchaseMapper) {
        this.productRepository = productRepository;
        this.purchaseRepository = purchaseRepository;
        this.purchaseMapper = purchaseMapper;
    }

    public List<PurchaseDto> findAll() {
        return purchaseRepository.findAll().stream()
                .map(purchaseMapper::toDto)
                .collect(Collectors.toList());
    }

    public Optional<PurchaseDto> findById(Long id) {
        return purchaseRepository.findById(id).map(purchaseMapper::toDto);
    }

    public Optional<List<PurchaseDto>> findByProductId(Long productId) {
        return productRepository.findById(productId)
                .map(purchaseRepository::findByProduct)
                .map(purchases -> purchases.stream()
                        .map(purchaseMapper::toDto)
                        .collect(Collectors.toList()));
    }

    public List<PurchaseResponse> findByStoreIdAndTransactionDate(Long storeId, LocalDate transactionDate) {
        return purchaseRepository.findByStoreIdAndTransactionDate(storeId, transactionDate).stream()
                .map(purchaseMapper::toPurchaseResponse)
                .collect(Collectors.toList());
    }

    public PurchaseDto create(PurchaseDto dto) {
        return purchaseMapper.toDto(purchaseRepository.save(purchaseMapper.toPurchase(dto)));
    }

    public PurchaseDto update(Long id, PurchaseDto dto) {
        if (purchaseRepository.findById(id).isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        if (!id.equals(dto.getId())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return purchaseMapper.toDto(purchaseRepository.save(purchaseMapper.toPurchase(dto)));
    }

    public void deleteById(Long id) {
        if (purchaseRepository.findById(id).isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        purchaseRepository.deleteById(id);
    }
}
