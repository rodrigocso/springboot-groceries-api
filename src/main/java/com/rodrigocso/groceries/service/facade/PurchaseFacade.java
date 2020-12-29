package com.rodrigocso.groceries.service.facade;

import com.rodrigocso.groceries.dto.PurchaseDto;
import com.rodrigocso.groceries.repository.PurchaseRepository;
import com.rodrigocso.groceries.service.mapper.PurchaseMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PurchaseFacade {
    private final PurchaseRepository purchaseRepository;

    public PurchaseFacade(PurchaseRepository purchaseRepository) {
        this.purchaseRepository = purchaseRepository;
    }

    public List<PurchaseDto> findAll() {
        return purchaseRepository.findAll().stream()
                .map(PurchaseMapper::toPurchaseDto)
                .collect(Collectors.toList());
    }
}
