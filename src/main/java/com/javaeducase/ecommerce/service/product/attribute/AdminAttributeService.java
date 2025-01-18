package com.javaeducase.ecommerce.service.product.attribute;

import com.javaeducase.ecommerce.dto.product.AttributeDTO;
import com.javaeducase.ecommerce.entity.product.Attribute;
import com.javaeducase.ecommerce.exception.product.AttributeNotFoundException;
import com.javaeducase.ecommerce.exception.product.DuplicateAttributeException;
import com.javaeducase.ecommerce.repository.product.AttributeRepository;
import com.javaeducase.ecommerce.util.product.CommonAllProductLinkedUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminAttributeService {

    private final AttributeRepository attributeRepository;

    public AttributeDTO createAttribute(AttributeDTO attributeDTO) {
        log.info("Creating attribute with name: {} and value: {}...", attributeDTO.getName(), attributeDTO.getValue());
        Attribute attribute = CommonAllProductLinkedUtils.convertAttributeDTOToAttribute(attributeDTO);
        Attribute savedAttribute = attributeRepository.save(attribute);
        log.info("Àttribute with new name: {} and value: {} successfully created",
                attributeDTO.getName(), attributeDTO.getValue());
        return CommonAllProductLinkedUtils.convertAttributeToAttributeDTO(savedAttribute);
    }

    public AttributeDTO updateAttribute(Long id, AttributeDTO attributeDTO) {
        log.info("Updating attribute with id: {}...", id);
        Attribute attribute = attributeRepository.findById(id)
                .orElseThrow(() -> new AttributeNotFoundException("Attribute with id: " + id + " not found"));

        String newName = attributeDTO.getName();
        String newValue = attributeDTO.getValue();

        if (attributeRepository.existsByNameAndValue(newName, newValue)) {
            throw new DuplicateAttributeException("Attribute with name: " + newName + " already exists");
        }

        log.info("Updating attribute with new name: {} and value: {}", newName, newValue);
        attribute.setName(newName);
        attribute.setValue(newValue);

        Attribute updatedAttribute = attributeRepository.save(attribute);
        log.info("Attribute with id: {} successfully updated", id);
        return CommonAllProductLinkedUtils.convertAttributeToAttributeDTO(updatedAttribute);
    }
}
