package com.rodrigocso.groceries.service.facade;

import com.rodrigocso.groceries.dto.BrandDto;
import com.rodrigocso.groceries.repository.BrandRepository;
import com.rodrigocso.groceries.util.builder.BrandBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

import javax.validation.ConstraintViolationException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest
public class BrandFacadeTests {
    @Autowired
    private BrandRepository brandRepository;

    private BrandFacade brandFacade;

    @BeforeEach
    public void setup() {
        this.brandFacade = new BrandFacade(brandRepository);
    }

    @Test
    public void brandFacadeIsNotNull() {
        assertThat(brandFacade).isNotNull();
    }

    @Test
    public void whenSaved_findsById() {
        BrandDto dto = brandFacade.save(BrandBuilder.builder().buildDto());
        assertThat(brandFacade.findById(dto.getId())).usingRecursiveComparison().isEqualTo(dto);
    }

    @Test
    public void whenInsertWithSameName_thenThrowError() {
        brandFacade.save(BrandBuilder.builder().withName("Kirkland").buildDto());
        assertThrows(DataIntegrityViolationException.class, () -> brandFacade.save(
                BrandBuilder.builder().withName("Kirkland").buildDto()));
    }

    @Test
    public void whenInsertWithEmptyName_thenThrowError() {
        brandFacade.save(BrandBuilder.builder().withName("").buildDto());
        assertThrows(ConstraintViolationException.class, () -> brandRepository.flush());
    }

    @Test
    public void whenSearchByName_thenReturnList() {
        brandFacade.save(BrandBuilder.builder().withName("Kirkland").buildDto());
        brandFacade.save(BrandBuilder.builder().withName("Timberland").buildDto());
        assertThat(brandFacade.findByNameContaining("LAN").size()).isEqualTo(2);
    }
}
