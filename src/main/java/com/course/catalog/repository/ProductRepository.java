package com.course.catalog.repository;

import com.course.catalog.model.Product;
import com.course.catalog.model.Review;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;

@Stateless
public class ProductRepository {

    @PersistenceContext(unitName = "catalogPU")
    private EntityManager em;

    public Product findById(Long id) {
        return em.find(Product.class, id);
    }

    public List<Product> findAll() {
        return em.createQuery("SELECT p FROM Product p", Product.class)
                .getResultList();
    }

    public List<Product> findByCategory(String category) {
        return em.createQuery(
                        "SELECT p FROM Product p WHERE p.category = :category", Product.class)
                .setParameter("category", category)
                .getResultList();
    }

    public void save(Product product) {
        em.persist(product);
    }

    public List<Review> findReviewsByProductId(Long productId) {
        return em.createQuery(
                        "SELECT r FROM Review r WHERE r.product.id = :productId", Review.class)
                .setParameter("productId", productId)
                .getResultList();
    }

    public List<Product> findByPriceLessThan(Double maxPrice) {
        return em.createQuery(
                        "SELECT p FROM Product p WHERE p.price < :maxPrice", Product.class)
                .setParameter("maxPrice", maxPrice)
                .getResultList();
    }
}