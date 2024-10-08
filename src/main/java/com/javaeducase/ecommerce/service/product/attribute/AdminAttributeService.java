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
    private final CommonAllProductLinkedUtils commonAllProductLinkedUtils;

    public AttributeDTO createAttribute(AttributeDTO attributeDTO) {
        Attribute attribute = commonAllProductLinkedUtils.convertAttributeDTOToAttribute(attributeDTO);
        Attribute savedAttribute = attributeRepository.save(attribute);
        return commonAllProductLinkedUtils.convertAttributeToAttributeDTO(savedAttribute);
    }

    public AttributeDTO updateAttribute(Long id, AttributeDTO attributeDTO) {
        Attribute attribute = attributeRepository.findById(id)
                .orElseThrow(() -> new AttributeNotFoundException("������� � id: " + id + " �� ������"));

        String newName = attributeDTO.getName();
        String newValue = attributeDTO.getValue();

        if (attributeRepository.existsByNameAndValue(newName, newValue)) {
            throw new DuplicateAttributeException("������� � ����� ������ � ��������� ��� ����������.");
        }

        attribute.setName(newName);
        attribute.setValue(newValue);

        Attribute updatedAttribute = attributeRepository.save(attribute);
        return commonAllProductLinkedUtils.convertAttributeToAttributeDTO(updatedAttribute);
    }

}
