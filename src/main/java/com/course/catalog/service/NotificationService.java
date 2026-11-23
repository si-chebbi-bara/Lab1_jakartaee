package com.course.catalog.service;

import jakarta.ejb.Singleton;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Singleton
public class NotificationService {

    private static final int MAX_HISTORY = 5;
    private final LinkedList<String> history = new LinkedList<>();

    public void logOrderConfirmation(String customerName) {
        System.out.println("[NOTIFY] Order confirmed for: " + customerName);

        history.addFirst(customerName + " at " + LocalDateTime.now());
        if (history.size() > MAX_HISTORY) {
            history.removeLast();
        }
    }

    public List<String> getHistory() {
        return new ArrayList<>(history);
    }
}