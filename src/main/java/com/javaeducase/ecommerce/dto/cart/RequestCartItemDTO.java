package com.javaeducase.ecommerce.dto.cart;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class RequestCartItemDTO {
    private Long offerId;
    private int quantity;
}