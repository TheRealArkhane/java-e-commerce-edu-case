package com.javaeducase.ecommerce.dto.product;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class AttributeDTO {
    private Long id;
    private String name;
    private String value;
}
