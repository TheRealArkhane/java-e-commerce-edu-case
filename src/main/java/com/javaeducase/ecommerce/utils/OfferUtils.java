package com.javaeducase.ecommerce.utils;

import com.javaeducase.ecommerce.dto.product.AttributeDTO;
import com.javaeducase.ecommerce.dto.product.OfferDTO;
import com.javaeducase.ecommerce.entities.product.Attribute;
import com.javaeducase.ecommerce.entities.product.Offer;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class OfferUtils {

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

    public Offer convertOfferDTOToOffer(OfferDTO offerDTO) {
        Offer offer = new Offer();
        offer.setPrice(offerDTO.getPrice());
        offer.setStockQuantity(offerDTO.getStockQuantity());
        offer.setIsDeleted(false); // Default is not deleted when adding a new offer
        offer.setAttributes(offerDTO.getAttributes().stream()
                .map(this::convertAttributeDTOToAttribute)
                .collect(Collectors.toList()));
        return offer;
    }

    private AttributeDTO convertAttributeToAttributeDTO(Attribute attribute) {
        AttributeDTO attributeDTO = new AttributeDTO();
        attributeDTO.setName(attribute.getName());
        attributeDTO.setValue(attribute.getValue());
        return attributeDTO;
    }

    private Attribute convertAttributeDTOToAttribute(AttributeDTO attributeDTO) {
        Attribute attribute = new Attribute();
        attribute.setName(attributeDTO.getName());
        attribute.setValue(attributeDTO.getValue());
        return attribute;
    }
}
