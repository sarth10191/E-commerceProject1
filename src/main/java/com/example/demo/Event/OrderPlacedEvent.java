package com.example.demo.Event;

import org.springframework.context.ApplicationEvent;

public class OrderPlacedEvent extends ApplicationEvent {
    private final Long orderId;
    public OrderPlacedEvent(Object source, Long orderId) {
        super(source);
        this.orderId = orderId;
    }
}
