package com.javaeducase.ecommerce.utils.product;

import com.javaeducase.ecommerce.dto.product.CategoryDTO;
import com.javaeducase.ecommerce.entities.product.Category;
import com.javaeducase.ecommerce.entities.product.Offer;
import com.javaeducase.ecommerce.entities.product.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CategoryUtils {

    private final CommonAllProductLinkedUtils commonAllProductLinkedUtils;


    public void markProductAndOffersAsDeleted(Product product) {
        product.setIsDeleted(true);
        for (Offer offer : product.getOffers()) {
            offer.setIsDeleted(true);
        }
    }


    public CategoryDTO convertToCategoryDTOWithDescendants(Category category) {
        CategoryDTO categoryDTO = commonAllProductLinkedUtils.convertCategoryToCategoryDTO(category);
        List<CategoryDTO> childrenWithDescendants = category.getChildren().stream()
                .map(this::convertToCategoryDTOWithDescendants)
                .toList();
//        categoryDTO.setChildren(childrenWithDescendants);
        return categoryDTO;
    }
}
