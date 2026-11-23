package com.course.catalog.service;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@Stateless
public class OrderService {

    @Inject
    private NotificationService notificationService;

    @Inject
    private CartService cartService;

    @Transactional
    public void createOrder(String name, String email, String address) {
        if (cartService.getItems().isEmpty()) {
            throw new IllegalStateException("Cannot create an order: the cart is empty");
        }

        // Order persistence arrives in Chapter 4 - for now, this represents
        // the transactional boundary the real save will live inside.
        notificationService.logOrderConfirmation(name);
        cartService.clear();
    }
}