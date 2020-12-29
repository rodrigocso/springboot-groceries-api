package com.rodrigocso.groceries.service.mapper;

import com.rodrigocso.groceries.dto.StoreDto;
import com.rodrigocso.groceries.model.Store;

public class StoreMapper {
    public static StoreDto toStoreDto(Store store) {
        if (store == null) return null;
        StoreDto dto = new StoreDto();
        dto.setId(store.getId());
        dto.setName(store.getName());
        dto.setCity(store.getCity());
        dto.setLatitude(store.getLatitude());
        dto.setLongitude(store.getLongitude());
        return dto;
    }

    public static Store toStore(StoreDto dto) {
        if (dto == null) return null;
        Store store = new Store();
        store.setId(dto.getId());
        store.setName(dto.getName());
        store.setCity(dto.getCity());
        store.setLatitude(dto.getLatitude());
        store.setLongitude(dto.getLongitude());
        return store;
    }
}
