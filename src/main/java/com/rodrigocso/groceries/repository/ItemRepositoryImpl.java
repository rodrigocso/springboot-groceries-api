package com.rodrigocso.groceries.repository;

import com.rodrigocso.groceries.model.*;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.List;

@Repository
public class ItemRepositoryImpl implements ItemCustomRepository {
    private final EntityManager entityManager;

    public ItemRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public List<Item> findByBrandOrProductName(String search) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Item> criteriaQuery = cb.createQuery(Item.class);
        Root<Item> itemRoot = criteriaQuery.from(Item.class);
        Join<Item, Product> productJoin = itemRoot.join(Item_.product, JoinType.INNER);
        Join<Product, Brand> brandJoin = productJoin.join(Product_.brand, JoinType.LEFT);

        criteriaQuery
                .select(itemRoot)
                .where(cb.or(
                        cb.like(brandJoin.get(Brand_.name), "%" + search + "%"),
                        cb.like(productJoin.get(Product_.name), "%" + search + "%")))
                .orderBy(cb.asc(productJoin.get(Product_.name)));

        TypedQuery<Item> query = entityManager.createQuery(criteriaQuery).setMaxResults(10);
        return query.getResultList();
    }
}
