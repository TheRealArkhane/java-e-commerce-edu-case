package com.javaeducase.ecommerce.service.product.offer;

import com.javaeducase.ecommerce.dto.product.AttributeRequestDTO;
import com.javaeducase.ecommerce.dto.product.CreateOfferRequestDTO;
import com.javaeducase.ecommerce.dto.product.OfferDTO;
import com.javaeducase.ecommerce.entity.product.Attribute;
import com.javaeducase.ecommerce.entity.product.Category;
import com.javaeducase.ecommerce.entity.product.Offer;
import com.javaeducase.ecommerce.entity.product.Product;
import com.javaeducase.ecommerce.exception.product.OfferIsDeletedException;
import com.javaeducase.ecommerce.repository.product.AttributeRepository;
import com.javaeducase.ecommerce.repository.product.OfferRepository;
import com.javaeducase.ecommerce.repository.product.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AdminOfferServiceTest {

    @Mock
    private OfferRepository offerRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private AttributeRepository attributeRepository;

    @InjectMocks
    private AdminOfferService adminOfferService;

    private Product product;
    private Offer offer;
    private Attribute attribute;

    @BeforeEach
    void setUp() {
        product = new Product();
        product.setId(1L);
        product.setName("Product 1");
        product.setDescription("Test Product");
        product.setCategory(new Category());
        product.setIsDeleted(false);

        offer = new Offer();
        offer.setId(1L);
        offer.setPrice(100);
        offer.setStockQuantity(10);
        offer.setIsAvailable(true);
        offer.setIsDeleted(false);
        offer.setProduct(product);
        offer.setAttributes(new ArrayList<Attribute>());

        attribute = new Attribute();
        attribute.setId(1L);
        attribute.setName("Color");
        attribute.setValue("Red");

        offer.getAttributes().add(attribute);
        product.setOffers(List.of(offer));
    }

    @Test
    void createProductOffer_Success() {
        CreateOfferRequestDTO requestDTO = new CreateOfferRequestDTO();
        requestDTO.setProductId(product.getId());
        requestDTO.setPrice(200);
        requestDTO.setStockQuantity(20);

        Offer newOffer = new Offer();
        newOffer.setId(2L);
        newOffer.setPrice(200);
        newOffer.setStockQuantity(20);
        newOffer.setIsAvailable(true);
        newOffer.setIsDeleted(false);
        newOffer.setProduct(product);

        when(productRepository.findById(product.getId())).thenReturn(Optional.of(product));
        when(offerRepository.save(any(Offer.class))).thenReturn(newOffer);

        OfferDTO result = adminOfferService.createProductOffer(requestDTO);

        assertNotNull(result);
        assertEquals(200, result.getPrice());
        assertEquals(20, result.getStockQuantity());
        assertTrue(result.getIsAvailable());
        assertFalse(result.getIsDeleted());
        verify(offerRepository).save(any(Offer.class));
    }

    @Test
    void createProductOffer_NegativePrice() {
        CreateOfferRequestDTO requestDTO = new CreateOfferRequestDTO();
        requestDTO.setProductId(product.getId());
        requestDTO.setPrice(-200);
        requestDTO.setStockQuantity(20);

        when(productRepository.findById(product.getId())).thenReturn(Optional.of(product));

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> adminOfferService.createProductOffer(requestDTO)
        );

        assertEquals("Price cannot be negative", exception.getMessage());
    }

    @Test
    void createProductOffer_NegativeStockQuantity() {
        CreateOfferRequestDTO requestDTO = new CreateOfferRequestDTO();
        requestDTO.setProductId(product.getId());
        requestDTO.setPrice(200);
        requestDTO.setStockQuantity(-20);

        when(productRepository.findById(product.getId())).thenReturn(Optional.of(product));

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> adminOfferService.createProductOffer(requestDTO)
        );

        assertEquals("Stock quantity cannot be negative", exception.getMessage());
    }

    @Test
    void updateOffer_Success() {
        OfferDTO offerDTO = new OfferDTO();
        offerDTO.setPrice(200);
        offerDTO.setStockQuantity(20);

        when(offerRepository.findById(offer.getId())).thenReturn(Optional.of(offer));
        when(offerRepository.save(any(Offer.class))).thenReturn(offer);

        OfferDTO result = adminOfferService.updateOffer(offer.getId(), offerDTO);

        assertNotNull(result);
        assertEquals(200, result.getPrice());
        assertEquals(20, result.getStockQuantity());
        verify(offerRepository).save(any(Offer.class));
    }

    @Test
    void updateOffer_OfferIsDeleted() {
        offer.setIsDeleted(true);
        when(offerRepository.findById(offer.getId())).thenReturn(Optional.of(offer));

        OfferIsDeletedException exception = assertThrows(
                OfferIsDeletedException.class,
                () -> adminOfferService.updateOffer(offer.getId(), new OfferDTO())
        );

        assertEquals("Offer with id: 1 was deleted", exception.getMessage());
    }

    @Test
    void updateOffer_NegativePrice() {
        OfferDTO offerDTO = new OfferDTO();
        offerDTO.setPrice(-50);

        when(offerRepository.findById(1L)).thenReturn(Optional.of(offer));

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> adminOfferService.updateOffer(1L, offerDTO)
        );

        assertEquals("Price cannot be negative", exception.getMessage());
    }

    @Test
    void updateOffer_NegativeStockQuantity() {
        OfferDTO offerDTO = new OfferDTO();
        offerDTO.setStockQuantity(-10);

        Mockito.when(offerRepository.findById(offer.getId())).thenReturn(Optional.of(offer));

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> adminOfferService.updateOffer(1L, offerDTO)
        );

        assertEquals("Stock quantity cannot be negative", exception.getMessage());
    }

    @Test
    void addAttributeToOffer_Success() {
        AttributeRequestDTO requestDTO = new AttributeRequestDTO();
        requestDTO.setOfferId(offer.getId());
        requestDTO.setAttributeId(attribute.getId());

        when(offerRepository.findById(offer.getId())).thenReturn(Optional.of(offer));
        when(attributeRepository.findById(attribute.getId())).thenReturn(Optional.of(attribute));
        when(offerRepository.save(any(Offer.class))).thenReturn(offer);

        OfferDTO result = adminOfferService.addAttributeToOffer(requestDTO);

        assertNotNull(result);
        assertEquals(offer.getId(), result.getId());
        verify(offerRepository).save(any(Offer.class));
    }

    @Test
    void deleteAttributeFromOffer_Success() {
        AttributeRequestDTO requestDTO = new AttributeRequestDTO();
        requestDTO.setOfferId(offer.getId());
        requestDTO.setAttributeId(attribute.getId());

        when(offerRepository.findById(offer.getId())).thenReturn(Optional.of(offer));
        when(attributeRepository.findById(attribute.getId())).thenReturn(Optional.of(attribute));
        when(offerRepository.save(any(Offer.class))).thenReturn(offer);

        OfferDTO result = adminOfferService.deleteAttributeFromOffer(requestDTO);

        assertNotNull(result);
        assertEquals(offer.getId(), result.getId());
        verify(offerRepository).save(any(Offer.class));
    }

    @Test
    void deleteOffer_Success() {
        when(offerRepository.findById(offer.getId())).thenReturn(Optional.of(offer));
        when(offerRepository.save(any(Offer.class))).thenReturn(offer);

        adminOfferService.deleteOffer(offer.getId());

        assertTrue(offer.getIsDeleted());
        assertFalse(offer.getIsAvailable());
        assertEquals(0, offer.getStockQuantity());
        verify(offerRepository).save(any(Offer.class));
    }
}

