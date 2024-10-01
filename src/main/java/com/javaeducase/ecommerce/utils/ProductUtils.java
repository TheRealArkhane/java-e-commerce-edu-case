package com.javaeducase.ecommerce.utils;

import com.javaeducase.ecommerce.dto.product.*;
import com.javaeducase.ecommerce.entities.product.*;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ProductUtils {

    public ProductDTO convertProductToProductDTO(Product product) {
        ProductDTO productDTO = new ProductDTO();
        productDTO.setId(product.getId());
        productDTO.setName(product.getName());
        productDTO.setDescription(product.getDescription());
        productDTO.setIsDeleted(product.getIsDeleted());
        CategoryDTO categoryDTO = convertCategoryToCategoryDTO(product.getCategory());
        productDTO.setCategory(categoryDTO);
        List<OfferDTO> offerDTOs = product.getOffers().stream()
                .map(this::convertOfferToOfferDTO)
                .collect(Collectors.toList());
        productDTO.setOffers(offerDTOs);
        return productDTO;
    }

    public OfferDTO convertOfferToOfferDTO(Offer offer) {
        OfferDTO offerDTO = new OfferDTO();
        offerDTO.setId(offer.getId());
        offerDTO.setPrice(offer.getPrice());
        offerDTO.setStockQuantity(offer.getStockQuantity());
        offerDTO.setIsDeleted(offer.getIsDeleted());
        offerDTO.setAttributes(offer.getAttributes().stream()
                .map(this::convertAttributeToAttributeDTO)
                .collect(Collectors.toList()));
        return offerDTO;
    }

    public AttributeDTO convertAttributeToAttributeDTO(Attribute attribute) {
        AttributeDTO attributeDTO = new AttributeDTO();
        attributeDTO.setName(attribute.getName());
        attributeDTO.setValue(attribute.getValue());
        return attributeDTO;
    }

    public CategoryDTO convertCategoryToCategoryDTO(Category category) {
        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setId(category.getId());
        categoryDTO.setName(category.getName());
        if (category.getParent() != null) {
            categoryDTO.setParent(convertCategoryToCategoryDTO(category.getParent()));
        }
        List<CategoryDTO> childrenDTOs = category.getChildren().stream()
                .map(this::convertCategoryToCategoryDTO)
                .collect(Collectors.toList());
        categoryDTO.setChildren(childrenDTOs);
        return categoryDTO;
    }
}