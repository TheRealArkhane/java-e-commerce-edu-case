package com.javaeducase.ecommerce.service.product.attribute;

import com.javaeducase.ecommerce.dto.product.AttributeDTO;
import com.javaeducase.ecommerce.entity.product.Attribute;
import com.javaeducase.ecommerce.exception.product.AttributeNotFoundException;
import com.javaeducase.ecommerce.repository.product.AttributeRepository;
import com.javaeducase.ecommerce.util.product.CommonAllProductLinkedUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AttributeService {

    private final AttributeRepository attributeRepository;

    public List<AttributeDTO> getAllAttributes() {
        log.info("Fetching all attributes...");
        List<AttributeDTO> attributes = attributeRepository.findAll()
                .stream()
                .map(CommonAllProductLinkedUtils::convertAttributeToAttributeDTO)
                .collect(Collectors.toList());
        log.info("Fetched {} attributes", attributes.size());
        return attributes;
    }

    public AttributeDTO getAttributeById(Long id) {
        log.info("Fetching attribute by id: {}...", id);
        Attribute attribute = attributeRepository.findById(id)
                .orElseThrow(() -> new AttributeNotFoundException("Attribute with id: " + id + " not found"));
        log.info("Fetched attribute: {}", attribute);
        return CommonAllProductLinkedUtils.convertAttributeToAttributeDTO(attribute);
    }
}
