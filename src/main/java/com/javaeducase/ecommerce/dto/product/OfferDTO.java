package com.javaeducase.ecommerce.dto.product;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class OfferDTO {
    private Long id;
    private Integer price;
    private Integer stockQuantity;

    @JsonProperty("isDeleted")
    private Boolean isDeleted;

    private List<AttributeDTO> attributes;
}
