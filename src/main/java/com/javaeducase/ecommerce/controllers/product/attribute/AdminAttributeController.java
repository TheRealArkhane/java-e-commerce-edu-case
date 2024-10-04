package com.javaeducase.ecommerce.controllers.product.attribute;

import com.javaeducase.ecommerce.dto.product.AttributeDTO;
import com.javaeducase.ecommerce.services.product.attribute.AdminAttributeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/attributes")
@RequiredArgsConstructor
public class AdminAttributeController {

    private final AdminAttributeService adminAttributeService;

    @PostMapping
    public ResponseEntity<AttributeDTO> createAttribute(@RequestBody AttributeDTO attributeDTO) {
        AttributeDTO createdAttribute = adminAttributeService.createAttribute(attributeDTO);
        return ResponseEntity.ok(createdAttribute);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AttributeDTO> updateAttribute(
            @PathVariable Long id,
            @RequestBody AttributeDTO attributeDTO) {
        AttributeDTO updatedAttribute = adminAttributeService.updateAttribute(id, attributeDTO);
        return ResponseEntity.ok(updatedAttribute);
    }
}
