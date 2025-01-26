package com.javaeducase.ecommerce.service.product.attribute;

import com.javaeducase.ecommerce.dto.product.AttributeDTO;
import com.javaeducase.ecommerce.entity.product.Attribute;
import com.javaeducase.ecommerce.exception.product.AttributeNotFoundException;
import com.javaeducase.ecommerce.repository.product.AttributeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AttributeServiceTest {

    @Mock
    private AttributeRepository attributeRepository;

    @InjectMocks
    private AttributeService attributeService;


    @Test
    public void getAllAttributes_Success() {
        Attribute attribute1 = new Attribute();
        attribute1.setId(1L);
        attribute1.setName("Color");

        Attribute attribute2 = new Attribute();
        attribute2.setId(2L);
        attribute2.setName("Size");

        List<Attribute> attributes = Arrays.asList(attribute1, attribute2);
        when(attributeRepository.findAll()).thenReturn(attributes);

        List<AttributeDTO> result = attributeService.getAllAttributes();


        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Color", result.get(0).getName());
        assertEquals("Size", result.get(1).getName());
    }

    @Test
    public void getAttributeById_Success() {
        Attribute attribute = new Attribute();
        attribute.setId(1L);
        attribute.setName("Color");

        when(attributeRepository.findById(1L)).thenReturn(Optional.of(attribute));

        // Выполняем метод
        AttributeDTO result = attributeService.getAttributeById(1L);

        // Проверяем результат
        assertNotNull(result);
        assertEquals("Color", result.getName());
    }

    @Test
    public void getAttributeById_AttributeNotFound() {
        when(attributeRepository.findById(999L)).thenReturn(Optional.empty());

        AttributeNotFoundException ex = assertThrows(
                AttributeNotFoundException.class,
                () -> attributeService.getAttributeById(999L));
        assertEquals("Attribute with id: " + 999 + " not found", ex.getMessage());
    }
}
