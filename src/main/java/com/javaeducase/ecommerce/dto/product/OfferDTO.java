package com.javaeducase.ecommerce.dto.product;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OfferDTO {
    private Long id;
    private Integer price;
    private Integer stockQuantity;

    @JsonProperty("isAvailable")
    private Boolean isAvailable;

    @JsonProperty("isDeleted")
    private Boolean isDeleted;

    private List<AttributeDTO> attributes;
}
