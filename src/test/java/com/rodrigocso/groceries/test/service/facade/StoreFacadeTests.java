package com.rodrigocso.groceries.test.service.facade;

import com.rodrigocso.groceries.dto.StoreDto;
import com.rodrigocso.groceries.repository.StoreRepository;
import com.rodrigocso.groceries.service.facade.StoreFacade;
import com.rodrigocso.groceries.test.util.builder.StoreBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import javax.validation.ConstraintViolationException;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest
public class StoreFacadeTests {
    @Autowired
    private StoreRepository storeRepository;

    private StoreFacade storeFacade;

    @BeforeEach
    public void setup() {
        storeFacade = new StoreFacade(storeRepository);
    }

    @Test
    public void storeFacadeIsNotNull() {
        assertThat(storeFacade).isNotNull();
    }

    @Test
    public void whenFindAll_thenReturnStoreList() {
        storeFacade.save(StoreBuilder.builder().withName("S1").buildDto());
        storeFacade.save(StoreBuilder.builder().withName("S2").buildDto());
        storeFacade.save(StoreBuilder.builder().withName("S3").buildDto());
        assertThat(storeFacade.findAll().size()).isEqualTo(3);
    }

    @Test
    public void whenSaved_findsById() {
        StoreDto dto = storeFacade.save(StoreBuilder.builder().buildDto());
        assertThat(storeFacade.findById(dto.getId())).usingRecursiveComparison().isEqualTo(Optional.of(dto));
    }

    @Test
    public void whenInsertWithEmptyName_thenThrowError() {
        storeFacade.save(StoreBuilder.builder().withName("").buildDto());
        assertThrows(ConstraintViolationException.class, () -> storeRepository.flush());
    }

    @Test
    public void whenSearchByName_thenReturnList() {
        storeFacade.save(StoreBuilder.builder().withName("Costco").buildDto());
        storeFacade.save(StoreBuilder.builder().withName("Ostrich").buildDto());
        assertThat(storeFacade.findByNameContaining("ost").size()).isEqualTo(2);
    }
}
