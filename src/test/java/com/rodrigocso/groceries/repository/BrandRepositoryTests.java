package com.rodrigocso.groceries.repository;

import com.rodrigocso.groceries.model.Brand;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.persistence.EntityManager;
import javax.sql.DataSource;
import javax.validation.ConstraintViolationException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest
public class BrandRepositoryTests {
    @Autowired private DataSource dataSource;
    @Autowired private JdbcTemplate jdbcTemplate;
    @Autowired private EntityManager entityManager;
    @Autowired private BrandRepository brandRepository;

    @Test
    public void injectedComponentsAreNotNull() {
        assertThat(dataSource).isNotNull();
        assertThat(jdbcTemplate).isNotNull();
        assertThat(entityManager).isNotNull();
        assertThat(brandRepository).isNotNull();
    }

    @Test
    public void whenSaved_thenFindsById() {
        Brand b = brandRepository.save(new Brand("Kirkland"));
        assertThat(brandRepository.getOne(b.getId())).isEqualTo(b);
    }

    @Test
    public void whenInsertWithSameName_thenThrowError() {
        brandRepository.save(new Brand("Kirkland"));
        brandRepository.save(new Brand("Kirkland"));
        assertThrows(DataIntegrityViolationException.class, () -> brandRepository.flush());
    }

    @Test
    public void whenInsertWithEmptyName_thenThrowError() {
        brandRepository.save(new Brand());
        assertThrows(ConstraintViolationException.class, () -> brandRepository.flush());
    }

    @Test
    public void whenSearchByName_thenReturnList() {
        brandRepository.save(new Brand("Kirkland"));
        brandRepository.save(new Brand("Timberland"));
        assertThat(brandRepository.findByNameContainingIgnoreCase("LAN").size()).isEqualTo(2);
    }
}
