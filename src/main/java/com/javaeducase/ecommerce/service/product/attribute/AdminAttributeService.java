package com.javaeducase.ecommerce.service.product.attribute;

import com.javaeducase.ecommerce.dto.product.AttributeDTO;
import com.javaeducase.ecommerce.entity.product.Attribute;
import com.javaeducase.ecommerce.exception.product.AttributeNotFoundException;
import com.javaeducase.ecommerce.exception.product.DuplicateAttributeException;
import com.javaeducase.ecommerce.repository.product.AttributeRepository;
import com.javaeducase.ecommerce.util.product.CommonAllProductLinkedUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminAttributeService {

    private final AttributeRepository attributeRepository;

    public AttributeDTO createAttribute(AttributeDTO attributeDTO) {
        Attribute attribute = CommonAllProductLinkedUtils.convertAttributeDTOToAttribute(attributeDTO);
        Attribute savedAttribute = attributeRepository.save(attribute);
        return CommonAllProductLinkedUtils.convertAttributeToAttributeDTO(savedAttribute);
    }

    public AttributeDTO updateAttribute(Long id, AttributeDTO attributeDTO) {
        Attribute attribute = attributeRepository.findById(id)
                .orElseThrow(() -> new AttributeNotFoundException("Attribute with id: " + id + " not found"));

        String newName = attributeDTO.getName();
        String newValue = attributeDTO.getValue();

        if (attributeRepository.existsByNameAndValue(newName, newValue)) {
            throw new DuplicateAttributeException("Attribute with name: " + newName + " already exists");
        }

        attribute.setName(newName);
        attribute.setValue(newValue);

        Attribute updatedAttribute = attributeRepository.save(attribute);
        return CommonAllProductLinkedUtils.convertAttributeToAttributeDTO(updatedAttribute);
    }

}
