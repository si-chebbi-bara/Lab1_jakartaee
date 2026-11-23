package com.course.catalog.service;

import com.course.catalog.model.ProductDTO;
import jakarta.ejb.Stateful;
import jakarta.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Stateful
@SessionScoped
public class CartService implements Serializable {

    private List<ProductDTO> items = new ArrayList<>();

    public void addItem(ProductDTO product) {
        items.add(product);
    }

    public void removeItem(Long productId) {
        items.removeIf(p -> p.getId().equals(productId));
    }

    public List<ProductDTO> getItems() {
        return items;
    }

    public void clear() {
        items.clear();
    }
    // Design choice: the discount lives in CartService instead of a separate
    // PromotionService. It is a flat 10% on the cart total, so it only needs
    // the items already held here and a separate bean would add little.
    // Trade-off: CartService now knows one pricing rule. If promotions grow
    // (coupons, tiers), this should move to its own service.
    public double getTotal() {
        return items.stream().mapToDouble(ProductDTO::getPrice).sum();
    }

    public double getDiscountedTotal() {
        return getTotal() * 0.90;
    }
}