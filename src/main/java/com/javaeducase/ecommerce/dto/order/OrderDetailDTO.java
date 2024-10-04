package com.javaeducase.ecommerce.dto.order;

import com.javaeducase.ecommerce.dto.product.OfferDTO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderDetailDTO {
    private OfferDTO offer;
    private int quantity;
    private int totalOfferAmount;
}
