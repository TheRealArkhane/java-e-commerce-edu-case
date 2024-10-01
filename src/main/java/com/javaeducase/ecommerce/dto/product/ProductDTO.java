package com.javaeducase.ecommerce.dto.product;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class ProductDTO {
    private Long id;
    private String name;
    private String description;

    @JsonProperty("isDeleted")
    private Boolean isDeleted;

    private CategoryDTO category;
    private List<OfferDTO> offers;
}