package com.javaeducase.ecommerce.util.product;

import com.javaeducase.ecommerce.dto.product.*;
import com.javaeducase.ecommerce.entity.product.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ProductUtils {

    public static ProductDTO convertProductToProductDTO(Product product) {
        ProductDTO productDTO = new ProductDTO();
        productDTO.setId(product.getId());
        productDTO.setName(product.getName());
        productDTO.setDescription(product.getDescription());
        productDTO.setIsDeleted(product.getIsDeleted());
        CategoryDTO categoryDTO = CommonAllProductLinkedUtils.convertCategoryToCategoryDTO(product.getCategory());
        productDTO.setCategory(categoryDTO);
        if (!product.getOffers().isEmpty()) {
            List<OfferDTO> offerDTOs = product.getOffers().stream()
                    .map(CommonAllProductLinkedUtils::convertOfferToOfferDTO)
                    .collect(Collectors.toList());
            productDTO.setOffers(offerDTOs);
        }
        else productDTO.setOffers(new ArrayList<>());
        return productDTO;
    }
}