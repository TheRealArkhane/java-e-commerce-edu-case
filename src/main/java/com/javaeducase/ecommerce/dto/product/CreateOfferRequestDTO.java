package com.javaeducase.ecommerce.dto.product;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateOfferRequestDTO {
    Long productId;
    int price;
    int stockQuantity;
}
