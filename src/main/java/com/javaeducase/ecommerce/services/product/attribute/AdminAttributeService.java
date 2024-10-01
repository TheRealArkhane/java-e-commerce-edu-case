package com.javaeducase.ecommerce.services.product.attribute;

import com.javaeducase.ecommerce.dto.product.AttributeDTO;
import com.javaeducase.ecommerce.entities.product.Attribute;
import com.javaeducase.ecommerce.repositories.product.AttributeRepository;
import com.javaeducase.ecommerce.utils.AttributeUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminAttributeService {

    private final AttributeRepository attributeRepository;
    private final AttributeUtils attributeUtils;

    @PreAuthorize("hasRole('ADMIN')")
    public AttributeDTO createAttribute(AttributeDTO attributeDTO) {
        Attribute attribute = attributeUtils.convertAttributeDTOToAttribute(attributeDTO);
        Attribute savedAttribute = attributeRepository.save(attribute);
        return attributeUtils.convertAttributeToAttributeDTO(savedAttribute);
    }
}
