package com.javaeducase.ecommerce.service.cart;

import com.javaeducase.ecommerce.entity.cart.CartItem;
import com.javaeducase.ecommerce.entity.product.Offer;
import com.javaeducase.ecommerce.repository.cart.CartItemRepository;
import com.javaeducase.ecommerce.repository.product.OfferRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CartItemServiceTest {

    @Mock
    private CartItemRepository cartItemRepository;

    @Mock
    private OfferRepository offerRepository;

    @InjectMocks
    private CartItemService cartItemService;

    private Offer offer;
    private CartItem cartItem;

    @BeforeEach
    void setUp() {
        offer = new Offer();
        offer.setId(1L);
        offer.setPrice(100);
        offer.setStockQuantity(10);
        offer.setAttributes(new ArrayList<>());
        offer.setIsAvailable(true);
        offer.setIsDeleted(false);

        cartItem = new CartItem();
        cartItem.setId(1L);
        cartItem.setOffer(offer);
        cartItem.setQuantity(5);
    }

    @Test
    void createCartItem_Success() {
        int quantity = 5;
        when(offerRepository.findById(offer.getId())).thenReturn(Optional.of(offer));
        when(cartItemRepository.save(any(CartItem.class))).thenReturn(cartItem);

        CartItem result = cartItemService.createCartItem(offer.getId(), quantity);

        assertNotNull(result);
        assertEquals(quantity, result.getQuantity());
        assertEquals(offer.getId(), result.getOffer().getId());
        verify(cartItemRepository).save(any(CartItem.class));
    }

    @Test
    void createCartItem_NegativeQuantity() {
        int quantity = -5;

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> cartItemService.createCartItem(offer.getId(), quantity)
        );

        assertEquals("Quantity must be greater than 0", exception.getMessage());
        verifyNoInteractions(cartItemRepository);
    }

    @Test
    void updateCartItem_Success() {
        int newQuantity = 8;
        when(cartItemRepository.findById(cartItem.getId())).thenReturn(Optional.of(cartItem));
        when(cartItemRepository.save(any(CartItem.class))).thenReturn(cartItem);

        CartItem result = cartItemService.updateCartItem(cartItem.getId(), newQuantity);

        assertNotNull(result);
        assertEquals(newQuantity, result.getQuantity());
        verify(cartItemRepository).save(cartItem);
    }

    @Test
    void updateCartItem_NegativeQuantity() {
        int newQuantity = -1;
        when(cartItemRepository.findById(cartItem.getId())).thenReturn(Optional.of(cartItem));

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> cartItemService.updateCartItem(cartItem.getId(), newQuantity)
        );

        assertEquals("Quantity cannot be negative", exception.getMessage());
    }

    @Test
    void updateCartItem_ExceedsStockQuantity() {
        int newQuantity = 15; // Exceeds stock quantity
        when(cartItemRepository.findById(cartItem.getId())).thenReturn(Optional.of(cartItem));

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> cartItemService.updateCartItem(cartItem.getId(), newQuantity)
        );

        assertEquals("Quantity cannot exceed available stock", exception.getMessage());
    }

    @Test
    void deleteCartItem_Success() {
        when(cartItemRepository.existsById(cartItem.getId())).thenReturn(true);
        doNothing().when(cartItemRepository).deleteById(cartItem.getId());

        cartItemService.deleteCartItem(cartItem.getId());

        verify(cartItemRepository).deleteById(cartItem.getId());
    }
}

