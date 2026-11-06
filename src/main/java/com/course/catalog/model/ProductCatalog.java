package com.course.catalog.model;

import java.util.List;

public class ProductCatalog {

    private static final List<ProductDTO> PRODUCTS = List.of(
            new ProductDTO(1L, "Laptop", "electronics", 999.0),
            new ProductDTO(2L, "Phone", "electronics", 599.0),
            new ProductDTO(3L, "Headphones", "electronics", 79.0),
            new ProductDTO(4L, "Desk", "furniture", 199.0),
            new ProductDTO(5L, "Chair", "furniture", 89.0)
    );

    public static List<ProductDTO> findAll() {
        return PRODUCTS;
    }

    public static ProductDTO findById(Long id) {
        return PRODUCTS.stream()
                .filter(p -> p.getId().equals(id))
                .findFirst()
                .orElse(null);
    }
}