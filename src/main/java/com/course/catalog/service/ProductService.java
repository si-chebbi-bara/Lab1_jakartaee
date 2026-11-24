package com.course.catalog.service;

import com.course.catalog.model.Product;
import com.course.catalog.model.ProductDTO;
import com.course.catalog.model.Review;
import com.course.catalog.repository.ProductRepository;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import java.util.List;

@Stateless
public class ProductService {

    @Inject
    private ProductRepository productRepository;

    public List<ProductDTO> getAllProducts() {
        return productRepository.findAll().stream()
                .map(this::toDTO)
                .toList();
    }

    public List<ProductDTO> getByCategory(String category) {
        return productRepository.findByCategory(category).stream()
                .map(this::toDTO)
                .toList();
    }

    public ProductDTO getProduct(Long id) {
        Product product = productRepository.findById(id);
        return product == null ? null : toDTO(product);
    }

    public List<Review> getReviews(Long productId) {
        return productRepository.findReviewsByProductId(productId);
    }

    private ProductDTO toDTO(Product p) {
        return new ProductDTO(p.getId(), p.getName(), p.getCategory(), p.getPrice());
    }
}