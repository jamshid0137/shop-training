package com.example.shopproject.observer;


import org.springframework.stereotype.Component;

@Component
public class EmailNotificationObserver implements OrderStatusObserver {

    @Override
    public void update(Long orderId, String status) {
        System.out.println("Send email: Order " + orderId + " status changed to " + status);
    }
}
