package com.rodrigocso.groceries.service.mapper;

import com.rodrigocso.groceries.dto.PurchaseDto;
import com.rodrigocso.groceries.dto.PurchaseResponse;
import com.rodrigocso.groceries.model.Purchase;
import com.rodrigocso.groceries.repository.ItemRepository;
import com.rodrigocso.groceries.repository.StoreRepository;
import org.springframework.stereotype.Component;

@Component
public class PurchaseMapper {
    private final ItemMapper itemMapper;
    private final ItemRepository itemRepository;
    private final StoreRepository storeRepository;

    public PurchaseMapper(ItemMapper itemMapper, StoreRepository storeRepository, ItemRepository itemRepository) {
        this.itemMapper = itemMapper;
        this.itemRepository = itemRepository;
        this.storeRepository = storeRepository;
    }

    public PurchaseDto toDto(Purchase purchase) {
        PurchaseDto dto = new PurchaseDto();
        dto.setId(purchase.getId());
        dto.setTransactionDate(purchase.getTransactionDate());
        dto.setQuantity(purchase.getQuantity());
        dto.setPrice(purchase.getPrice());
        if (purchase.getStore() != null) {
            dto.setStoreId(purchase.getStore().getId());
        }
        if (purchase.getItem() != null) {
            dto.setItemId(purchase.getItem().getId());
        }
        return dto;
    }

    public Purchase toPurchase(PurchaseDto dto) {
        Purchase purchase = new Purchase();
        purchase.setId(dto.getId());
        purchase.setTransactionDate(dto.getTransactionDate());
        purchase.setQuantity(dto.getQuantity());
        purchase.setPrice(dto.getPrice());
        purchase.setStore(storeRepository.findById(dto.getStoreId()).orElse(null));
        purchase.setItem(itemRepository.findById(dto.getItemId()).orElse(null));
        return purchase;
    }

    public PurchaseResponse toPurchaseResponse(Purchase purchase) {
        PurchaseResponse dto = new PurchaseResponse();
        dto.setId(purchase.getId());
        dto.setStoreId(purchase.getStore().getId());
        dto.setItem(itemMapper.toItemResponse(purchase.getItem()));
        dto.setQuantity(purchase.getQuantity());
        dto.setPrice(purchase.getPrice());
        return dto;
    }
}
