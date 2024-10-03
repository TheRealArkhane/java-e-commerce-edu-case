package com.javaeducase.ecommerce.utils.product;

import com.javaeducase.ecommerce.dto.product.AttributeDTO;
import com.javaeducase.ecommerce.dto.product.CategoryDTO;
import com.javaeducase.ecommerce.dto.product.OfferDTO;
import com.javaeducase.ecommerce.entities.product.Attribute;
import com.javaeducase.ecommerce.entities.product.Category;
import com.javaeducase.ecommerce.entities.product.Offer;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CommonAllProductLinkedUtils {

    public AttributeDTO convertAttributeToAttributeDTO(Attribute attribute) {
        AttributeDTO attributeDTO = new AttributeDTO();
        attributeDTO.setId(attribute.getId());
        attributeDTO.setName(attribute.getName());
        attributeDTO.setValue(attribute.getValue());
        return attributeDTO;
    }

    public Attribute convertAttributeDTOToAttribute(AttributeDTO attributeDTO) {
        Attribute attribute = new Attribute();
        attribute.setId(attributeDTO.getId());
        attribute.setName(attributeDTO.getName());
        attribute.setValue(attributeDTO.getValue());
        return attribute;
    }

    public CategoryDTO convertCategoryToCategoryDTO(Category category) {
        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setId(category.getId());
        categoryDTO.setName(category.getName());
//        if (category.getParent() != null) {
//            categoryDTO.setParent(convertCategoryToCategoryDTO(category.getParent()));
//        }
//        List<CategoryDTO> childrenDTOs = category.getChildren().stream()
//                .map(this::convertCategoryToCategoryDTO)
//                .collect(Collectors.toList());
//        categoryDTO.setChildren(childrenDTOs);
        return categoryDTO;
    }

    public OfferDTO convertOfferToOfferDTO(Offer offer) {
        OfferDTO offerDTO = new OfferDTO();
        offerDTO.setId(offer.getId());
        offerDTO.setPrice(offer.getPrice());
        offerDTO.setStockQuantity(offer.getStockQuantity());
        offerDTO.setIsDeleted(offer.getIsDeleted());
        offerDTO.setIsAvailable(offer.getIsAvailable());
        offerDTO.setAttributes(offer.getAttributes().stream()
                .map(this::convertAttributeToAttributeDTO)
                .collect(Collectors.toList()));
        return offerDTO;
    }

//    public Offer convertOfferDTOToOffer(OfferDTO offerDTO) {
//        Offer offer = new Offer();
//        offer.setPrice(offerDTO.getPrice());
//        offer.setStockQuantity(offerDTO.getStockQuantity());
//        offer.setIsDeleted(offerDTO.getIsDeleted());
//        offer.setIsAvailable(offerDTO.getIsAvailable());
//        offer.setAttributes(offerDTO.getAttributes().stream()
//                .map(this::convertAttributeDTOToAttribute)
//                .collect(Collectors.toList()));
//        return offer;
//    }
}
