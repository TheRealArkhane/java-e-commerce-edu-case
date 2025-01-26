package com.javaeducase.ecommerce.service.product.offer;

import com.javaeducase.ecommerce.dto.product.AttributeDTO;
import com.javaeducase.ecommerce.dto.product.OfferDTO;
import com.javaeducase.ecommerce.entity.product.Attribute;
import com.javaeducase.ecommerce.entity.product.Offer;
import com.javaeducase.ecommerce.exception.product.OfferNotFoundException;
import com.javaeducase.ecommerce.repository.product.OfferRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OfferServiceTest {

    @Mock
    private OfferRepository offerRepository;

    @InjectMocks
    private OfferService offerService;

    private Offer offer;
    private Attribute attribute1;
    private Attribute attribute2;

    @BeforeEach
    void setUp() {
        offer = new Offer();
        offer.setId(1L);
        offer.setPrice(100);
        offer.setStockQuantity(10);
        offer.setIsAvailable(true);
        offer.setIsDeleted(false);

        attribute1 = new Attribute();
        attribute1.setId(1L);
        attribute1.setName("Color");
        attribute1.setValue("Red");

        attribute2 = new Attribute();
        attribute2.setId(2L);
        attribute2.setName("Size");
        attribute2.setValue("M");

        offer.setAttributes(List.of(attribute1, attribute2));
    }

    @Test
    void getOfferById_Success() {
        when(offerRepository.findById(1L)).thenReturn(Optional.of(offer));

        OfferDTO result = offerService.getOfferById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(100, result.getPrice());
        verify(offerRepository).findById(1L);
    }

    @Test
    void getOfferById_OfferNotFound() {
        when(offerRepository.findById(1L)).thenReturn(Optional.empty());

        OfferNotFoundException exception = Assertions.assertThrows(
                OfferNotFoundException.class,
                () -> offerService.getOfferById(1L)
        );
        assertEquals("Offer with id: 1 not found", exception.getMessage());
        verify(offerRepository).findById(1L);
    }

    @Test
    void getAttributesByOfferId_Success() {
        when(offerRepository.findById(1L)).thenReturn(Optional.of(offer));

        List<AttributeDTO> result = offerService.getAttributesByOfferId(1L);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Color", result.get(0).getName());
        assertEquals("Red", result.get(0).getValue());
        assertEquals("Size", result.get(1).getName());
        assertEquals("M", result.get(1).getValue());
        verify(offerRepository).findById(1L);
    }
}

