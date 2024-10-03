package com.javaeducase.ecommerce.dto.cart;

import com.javaeducase.ecommerce.dto.product.OfferDTO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@ToString
public class CartItemDTO {
    private OfferDTO offer;
    private int quantity;

    public Integer getTotalPrice() {
        return offer.getPrice() * quantity;
    }
}
