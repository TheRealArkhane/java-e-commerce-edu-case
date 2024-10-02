package com.javaeducase.ecommerce.dto.product;

import com.fasterxml.jackson.annotation.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class CategoryDTO {
    private Long id;
    private String name;

    @JsonIgnore
    private CategoryDTO parent;
}
