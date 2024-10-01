package com.javaeducase.ecommerce.services.product.attribute;

import com.javaeducase.ecommerce.dto.product.AttributeDTO;
import com.javaeducase.ecommerce.entities.product.Attribute;
import com.javaeducase.ecommerce.exceptions.product.AttributeNotFoundException;
import com.javaeducase.ecommerce.repositories.product.AttributeRepository;
import com.javaeducase.ecommerce.utils.AttributeUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AttributeService {

    private final AttributeRepository attributeRepository;
    private final AttributeUtils attributeUtils;

    public List<AttributeDTO> getAttributesByOfferId(Long offerId) {
        return attributeRepository.findByOfferId(offerId).stream()
                .map(attributeUtils::convertAttributeToAttributeDTO)
                .collect(Collectors.toList());
    }

    public AttributeDTO getAttributeById(Long id) {
        Attribute attribute = attributeRepository.findById(id)
                .orElseThrow(() -> new AttributeNotFoundException("Атрибут с id: " + id + " не найден"));
        return attributeUtils.convertAttributeToAttributeDTO(attribute);
    }

    public AttributeDTO createAttribute(AttributeDTO attributeDTO) {
        Attribute attribute = attributeUtils.convertAttributeDTOToAttribute(attributeDTO);
        Attribute savedAttribute = attributeRepository.save(attribute);
        return attributeUtils.convertAttributeToAttributeDTO(savedAttribute);
    }
}
