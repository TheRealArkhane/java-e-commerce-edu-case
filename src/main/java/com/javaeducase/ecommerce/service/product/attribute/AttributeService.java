package com.javaeducase.ecommerce.service.product.attribute;

import com.javaeducase.ecommerce.dto.product.AttributeDTO;
import com.javaeducase.ecommerce.entity.product.Attribute;
import com.javaeducase.ecommerce.exception.product.AttributeNotFoundException;
import com.javaeducase.ecommerce.repository.product.AttributeRepository;
import com.javaeducase.ecommerce.repository.product.OfferRepository;
import com.javaeducase.ecommerce.util.product.CommonAllProductLinkedUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AttributeService {

    private final AttributeRepository attributeRepository;

    public List<AttributeDTO> getAllAttributes() {
        return attributeRepository.findAll()
                .stream()
                .map(CommonAllProductLinkedUtils::convertAttributeToAttributeDTO)
                .collect(Collectors.toList());
    }

    public AttributeDTO getAttributeById(Long id) {
        Attribute attribute = attributeRepository.findById(id)
                .orElseThrow(() -> new AttributeNotFoundException("Атрибут с id: " + id + " не найден"));
        return CommonAllProductLinkedUtils.convertAttributeToAttributeDTO(attribute);
    }
}
