package com.javaeducase.ecommerce.service.product.attribute;


import com.javaeducase.ecommerce.dto.product.AttributeDTO;
import com.javaeducase.ecommerce.entity.product.Attribute;
import com.javaeducase.ecommerce.exception.product.AttributeNotFoundException;
import com.javaeducase.ecommerce.exception.product.DuplicateAttributeException;
import com.javaeducase.ecommerce.repository.product.AttributeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AdminAttributeServiceTest {

    @Mock
    private AttributeRepository attributeRepository;

    @InjectMocks
    private AdminAttributeService adminAttributeService;

    private Attribute attribute;

    @BeforeEach
    void setUp() {
        attribute = new Attribute();
        attribute.setId(1L);
        attribute.setName("Color");
        attribute.setValue("Red");
    }

    @Test
    void createAttribute_Success() {
        AttributeDTO attributeDTO = new AttributeDTO();
        attributeDTO.setName("Color");
        attributeDTO.setValue("Red");

        Attribute newAttribute = new Attribute();
        newAttribute.setId(2L);
        newAttribute.setName("Color");
        newAttribute.setValue("Red");

        when(attributeRepository.save(any(Attribute.class))).thenReturn(newAttribute);

        AttributeDTO result = adminAttributeService.createAttribute(attributeDTO);

        assertNotNull(result);
        assertEquals("Color", result.getName());
        assertEquals("Red", result.getValue());
        verify(attributeRepository).save(any(Attribute.class));
    }

    @Test
    void updateAttribute_Success() {
        AttributeDTO attributeDTO = new AttributeDTO();
        attributeDTO.setName("Size");
        attributeDTO.setValue("Large");

        when(attributeRepository.findById(attribute.getId())).thenReturn(Optional.of(attribute));
        when(attributeRepository.save(any(Attribute.class))).thenReturn(attribute);

        AttributeDTO result = adminAttributeService.updateAttribute(attribute.getId(), attributeDTO);

        assertNotNull(result);
        assertEquals("Size", result.getName());
        assertEquals("Large", result.getValue());
        verify(attributeRepository).save(any(Attribute.class));
    }

    @Test
    void updateAttribute_AttributeNotFound() {
        AttributeDTO attributeDTO = new AttributeDTO();
        attributeDTO.setName("Size");
        attributeDTO.setValue("Large");

        when(attributeRepository.findById(attribute.getId())).thenReturn(Optional.empty());

        AttributeNotFoundException exception = assertThrows(
                AttributeNotFoundException.class,
                () -> adminAttributeService.updateAttribute(attribute.getId(), attributeDTO)
        );

        assertEquals("Attribute with id: 1 not found", exception.getMessage());
    }

    @Test
    void updateAttribute_AttributeAlreadyExists() {
        AttributeDTO attributeDTO = new AttributeDTO();
        attributeDTO.setName("Color");
        attributeDTO.setValue("Red");

        when(attributeRepository.findById(attribute.getId())).thenReturn(Optional.of(attribute));
        when(attributeRepository.existsByNameAndValue("Color", "Red")).thenReturn(true);

        DuplicateAttributeException exception = assertThrows(
                DuplicateAttributeException.class,
                () -> adminAttributeService.updateAttribute(attribute.getId(), attributeDTO)
        );

        assertEquals("Attribute with name: Color already exists", exception.getMessage());
    }
}

