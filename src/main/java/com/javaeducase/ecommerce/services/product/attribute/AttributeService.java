package com.javaeducase.ecommerce.services.product.attribute;

import com.javaeducase.ecommerce.dto.product.AttributeDTO;
import com.javaeducase.ecommerce.entities.product.Attribute;
import com.javaeducase.ecommerce.exceptions.product.AttributeNotFoundException;
import com.javaeducase.ecommerce.exceptions.product.OfferNotFoundException;
import com.javaeducase.ecommerce.repositories.product.AttributeRepository;
import com.javaeducase.ecommerce.repositories.product.OfferRepository;
import com.javaeducase.ecommerce.utils.product.CommonAllProductLinkedUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AttributeService {

    private final AttributeRepository attributeRepository;
    private final OfferRepository offerRepository;
    private final CommonAllProductLinkedUtils commonAllProductLinkedUtils;

    public List<AttributeDTO> getAllAttributes() {
        return attributeRepository.findAll()
                .stream()
                .map(commonAllProductLinkedUtils::convertAttributeToAttributeDTO)
                .collect(Collectors.toList());
    }

    public AttributeDTO getAttributeById(Long id) {
        Attribute attribute = attributeRepository.findById(id)
                .orElseThrow(() -> new AttributeNotFoundException("Атрибут с id: " + id + " не найден"));
        return commonAllProductLinkedUtils.convertAttributeToAttributeDTO(attribute);
    }
}
