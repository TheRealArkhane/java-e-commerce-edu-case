package com.javaeducase.ecommerce.dto.order;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DeliveryDTO {
    private Long id;
    private String name;
    private int deliveryPrice;
}
