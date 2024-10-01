package com.javaeducase.ecommerce.services.product.attribute;

import com.javaeducase.ecommerce.dto.product.AttributeDTO;
import com.javaeducase.ecommerce.entities.product.Attribute;
import com.javaeducase.ecommerce.exceptions.product.AttributeNotFoundException;
import com.javaeducase.ecommerce.exceptions.product.DuplicateAttributeException;
import com.javaeducase.ecommerce.repositories.product.AttributeRepository;
import com.javaeducase.ecommerce.utils.product.CommonAllProductLinkedUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminAttributeService {

    private final AttributeRepository attributeRepository;
    private final CommonAllProductLinkedUtils commonAllProductLinkedUtils;

    @PreAuthorize("hasRole('ADMIN')")
    public AttributeDTO createAttribute(AttributeDTO attributeDTO) {
        Attribute attribute = commonAllProductLinkedUtils.convertAttributeDTOToAttribute(attributeDTO);
        Attribute savedAttribute = attributeRepository.save(attribute);
        return commonAllProductLinkedUtils.convertAttributeToAttributeDTO(savedAttribute);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public AttributeDTO updateAttribute(Long id, AttributeDTO attributeDTO) {
        Attribute attribute = attributeRepository.findById(id)
                .orElseThrow(() -> new AttributeNotFoundException("Атрибут с id: " + id + " не найден"));

        String newName = attributeDTO.getName();
        String newValue = attributeDTO.getValue();

        if (attributeRepository.existsByNameAndValue(newName, newValue)) {
            throw new DuplicateAttributeException("Атрибут с таким именем и значением уже существует.");
        }

        attribute.setName(newName);
        attribute.setValue(newValue);

        Attribute updatedAttribute = attributeRepository.save(attribute);
        return commonAllProductLinkedUtils.convertAttributeToAttributeDTO(updatedAttribute);
    }

}
