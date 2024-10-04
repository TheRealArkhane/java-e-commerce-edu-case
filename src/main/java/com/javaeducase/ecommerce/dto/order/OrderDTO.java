package com.javaeducase.ecommerce.dto.order;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class OrderDTO {
    private Long id;
    @JsonProperty("orderDetails")
    private List<OrderDetailDTO> orderDetailDTO;
    private String address;
    private DeliveryDTO delivery;
    private PaymentDTO payment;
    private LocalDateTime orderCreateDateTime;
    private int totalQuantity;
    private int totalAmount;
}
