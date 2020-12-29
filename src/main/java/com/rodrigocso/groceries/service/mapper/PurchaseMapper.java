package com.rodrigocso.groceries.service.mapper;

import com.rodrigocso.groceries.dto.PurchaseDto;
import com.rodrigocso.groceries.model.Purchase;

public class PurchaseMapper {
    public static PurchaseDto toPurchaseDto(Purchase purchase) {
        if (purchase == null) return null;
        PurchaseDto dto = new PurchaseDto();
        dto.setId(purchase.getId());
        dto.setStore(purchase.getStore());
        dto.setTransactionDate(purchase.getTransactionDate());
        dto.setItem(purchase.getItem());
        dto.setQuantity(purchase.getQuantity());
        dto.setPrice(purchase.getPrice());
        return dto;
    }

    public static Purchase toPurchase(PurchaseDto dto) {
        if (dto == null) return null;
        Purchase purchase = new Purchase();
        purchase.setId(dto.getId());
        purchase.setStore(dto.getStore());
        purchase.setTransactionDate(dto.getTransactionDate());
        purchase.setItem(dto.getItem());
        purchase.setQuantity(dto.getQuantity());
        purchase.setPrice(dto.getPrice());
        return purchase;
    }
}
