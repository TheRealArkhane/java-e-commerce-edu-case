package com.javaeducase.ecommerce.dto.product;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class CategoryDTO {
    private Long id;
    private String name;
    @JsonBackReference
    private CategoryDTO parent;
    @JsonManagedReference
    private List<CategoryDTO> children;
}
