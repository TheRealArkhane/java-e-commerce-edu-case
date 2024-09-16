package com.javaeducase.ecommerce.entities.order;

import lombok.Getter;

@Getter
public enum DeliveryMethod {
    PICKUP("Pickup",0),
    COURIER_DELIVERY("Courier Delivery",150);

    private final String description;
    private final int price;


    DeliveryMethod(String description, int price) {
        this.description = description;
        this.price = price;
    }

}
