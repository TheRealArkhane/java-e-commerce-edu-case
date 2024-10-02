package com.javaeducase.ecommerce.controllers.product.attribute;

import com.javaeducase.ecommerce.dto.product.AttributeDTO;
import com.javaeducase.ecommerce.repositories.product.AttributeRepository;
import com.javaeducase.ecommerce.services.product.attribute.AttributeService;
import com.javaeducase.ecommerce.utils.product.CommonAllProductLinkedUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/attributes")
@RequiredArgsConstructor
public class AttributeController {

    private final AttributeService attributeService;

    @GetMapping("/{id}")
    public ResponseEntity<AttributeDTO> getAttributeById(@PathVariable Long id) {
        return ResponseEntity.ok(attributeService.getAttributeById(id));
    }

}
