package com.javaeducase.ecommerce.dto.order;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequestOrderDTO {
    private Long deliveryId;
    private Long paymentId;
    private String address;
}
