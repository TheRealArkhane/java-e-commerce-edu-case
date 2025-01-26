package com.javaeducase.ecommerce.dto.product;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateCategoryRequestDTO {
    private String name;
    private Long parentId;
}
