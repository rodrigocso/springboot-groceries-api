package com.rodrigocso.groceries.test.util.builder;

import com.rodrigocso.groceries.dto.StoreDto;
import com.rodrigocso.groceries.model.Store;
import com.rodrigocso.groceries.service.mapper.StoreMapper;

public class StoreBuilder {
    private Integer id;
    private String name;
    private String city;
    private Float latitude;
    private Float longitude;

    private StoreBuilder() {
        id = null;
        name = "Supermarket";
        city = "Seattle";
        latitude = 47.61143639419748F;
        longitude = -122.32136766306526F;
    }

    public static StoreBuilder builder() {
        return new StoreBuilder();
    }

    public Store build() {
        return makeStore();
    }

    public StoreDto buildDto() {
        return StoreMapper.toStoreDto(makeStore());
    }

    public StoreBuilder from(Store template) {
        id = template.getId();
        name = template.getName();
        return this;
    }

    public StoreBuilder from(StoreDto template) {
        return from(StoreMapper.toStore(template));
    }

    public StoreBuilder withId(Integer id) {
        this.id = id;
        return this;
    }

    public StoreBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public StoreBuilder withCity(String city) {
        this.city = city;
        return this;
    }

    public StoreBuilder withLatitude(Float latitude) {
        this.latitude = latitude;
        return this;
    }

    public StoreBuilder withLongitude(Float longitude) {
        this.longitude = longitude;
        return this;
    }

    private Store makeStore() {
        Store s = new Store();
        s.setId(id);
        s.setName(name);
        s.setCity(city);
        s.setLatitude(latitude);
        s.setLongitude(longitude);
        return s;
    }
}
