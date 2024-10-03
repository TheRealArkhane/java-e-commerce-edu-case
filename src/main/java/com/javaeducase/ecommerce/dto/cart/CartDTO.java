package com.javaeducase.ecommerce.dto.cart;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class CartDTO {
    private List<CartItemDTO> items;
    private int totalAmount;
    private int totalQuantity;
}
