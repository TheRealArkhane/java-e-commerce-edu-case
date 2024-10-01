package com.javaeducase.ecommerce.utils;

import com.javaeducase.ecommerce.dto.product.AttributeDTO;
import com.javaeducase.ecommerce.entities.product.Attribute;
import org.springframework.stereotype.Component;

@Component
public class AttributeUtils {

    public AttributeDTO convertAttributeToAttributeDTO(Attribute attribute) {
        AttributeDTO attributeDTO = new AttributeDTO();
        attributeDTO.setName(attribute.getName());
        attributeDTO.setValue(attribute.getValue());
        return attributeDTO;
    }

    public Attribute convertAttributeDTOToAttribute(AttributeDTO attributeDTO) {
        Attribute attribute = new Attribute();
        attribute.setName(attributeDTO.getName());
        attribute.setValue(attributeDTO.getValue());
        return attribute;
    }
}
