package com.javaeducase.ecommerce.util.product;

import com.javaeducase.ecommerce.dto.product.AttributeDTO;
import com.javaeducase.ecommerce.dto.product.CategoryDTO;
import com.javaeducase.ecommerce.dto.product.OfferDTO;
import com.javaeducase.ecommerce.entity.product.Attribute;
import com.javaeducase.ecommerce.entity.product.Category;
import com.javaeducase.ecommerce.entity.product.Offer;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class CommonAllProductLinkedUtils {

    public static AttributeDTO convertAttributeToAttributeDTO(Attribute attribute) {
        AttributeDTO attributeDTO = new AttributeDTO();
        attributeDTO.setId(attribute.getId());
        attributeDTO.setName(attribute.getName());
        attributeDTO.setValue(attribute.getValue());
        return attributeDTO;
    }

    public static Attribute convertAttributeDTOToAttribute(AttributeDTO attributeDTO) {
        Attribute attribute = new Attribute();
        attribute.setId(attributeDTO.getId());
        attribute.setName(attributeDTO.getName());
        attribute.setValue(attributeDTO.getValue());
        return attribute;
    }

    public static CategoryDTO convertCategoryToCategoryDTO(Category category) {
        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setId(category.getId());
        categoryDTO.setName(category.getName());
        if (category.getParent() != null) {
            categoryDTO.setParentId(category.getParent().getId());
        }
        return categoryDTO;
    }

    public static OfferDTO convertOfferToOfferDTO(Offer offer) {
        OfferDTO offerDTO = new OfferDTO();
        offerDTO.setId(offer.getId());
        offerDTO.setPrice(offer.getPrice());
        offerDTO.setStockQuantity(offer.getStockQuantity());
        offerDTO.setIsDeleted(offer.getIsDeleted());
        offerDTO.setIsAvailable(offer.getIsAvailable());
        if (!offer.getAttributes().isEmpty()) {
            offerDTO.setAttributes(offer.getAttributes().stream()
                    .map(CommonAllProductLinkedUtils::convertAttributeToAttributeDTO)
                    .collect(Collectors.toList()));
        }
        else offerDTO.setAttributes(new ArrayList<>());
        return offerDTO;
    }
}
