package com.rodrigocso.groceries.repository;

import com.rodrigocso.groceries.model.*;
import com.rodrigocso.groceries.model.Item_;
import com.rodrigocso.groceries.model.Purchase_;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.List;

@Repository
public class PurchaseRepositoryImpl implements PurchaseCustomRepository {
    private final EntityManager entityManager;

    public PurchaseRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public List<Purchase> findByProduct(Product product) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Purchase> criteriaQuery = cb.createQuery(Purchase.class);
        Root<Purchase> root = criteriaQuery.from(Purchase.class);
        Join<Purchase, Item> itemJoin = root.join(Purchase_.item, JoinType.INNER);
        criteriaQuery
                .select(root)
                .where(cb.equal(itemJoin.get(Item_.product), product));
        TypedQuery<Purchase> query = entityManager.createQuery(criteriaQuery);
        return query.getResultList();
    }
}
