package com.javaeducase.ecommerce.dto.order;

import com.javaeducase.ecommerce.dto.cart.CartDTO;
import com.javaeducase.ecommerce.entities.order.Delivery;
import com.javaeducase.ecommerce.entities.order.Payment;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class OrderDTO {
    private Long id;
    private CartDTO cart;
    private String address;
    private Delivery delivery;
    private Payment payment;
    private LocalDateTime orderCreateDateTime;
    private int totalAmount;
}
