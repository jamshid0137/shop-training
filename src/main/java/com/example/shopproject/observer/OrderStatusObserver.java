package com.example.shopproject.observer;



public interface OrderStatusObserver {
    void update(Long orderId, String status);
}
