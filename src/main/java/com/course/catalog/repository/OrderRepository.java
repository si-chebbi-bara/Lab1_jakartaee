package com.course.catalog.repository;

import com.course.catalog.model.Customer;
import com.course.catalog.model.Order;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;

@Stateless
public class OrderRepository {

    @PersistenceContext(unitName = "catalogPU")
    private EntityManager em;

    public void save(Order order) {
        em.persist(order);
    }

    public List<Order> findByCustomer(Customer customer) {
        return em.createQuery(
                        "SELECT o FROM Order o WHERE o.customer = :customer", Order.class)
                .setParameter("customer", customer)
                .getResultList();
    }
}