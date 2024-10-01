package com.javaeducase.ecommerce.utils.product;

import com.javaeducase.ecommerce.dto.product.*;
import com.javaeducase.ecommerce.entities.product.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ProductUtils {

    private final CommonAllProductLinkedUtils commonAllProductLinkedUtils;


    public ProductDTO convertProductToProductDTO(Product product) {
        ProductDTO productDTO = new ProductDTO();
        productDTO.setId(product.getId());
        productDTO.setName(product.getName());
        productDTO.setDescription(product.getDescription());
        productDTO.setIsDeleted(product.getIsDeleted());
        CategoryDTO categoryDTO = commonAllProductLinkedUtils.convertCategoryToCategoryDTO(product.getCategory());
        productDTO.setCategory(categoryDTO);
        List<OfferDTO> offerDTOs = product.getOffers().stream()
                .map(commonAllProductLinkedUtils::convertOfferToOfferDTO)
                .collect(Collectors.toList());
        productDTO.setOffers(offerDTOs);
        return productDTO;
    }
}