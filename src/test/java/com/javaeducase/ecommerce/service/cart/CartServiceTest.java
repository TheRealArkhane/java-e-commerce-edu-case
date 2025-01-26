package com.javaeducase.ecommerce.service.cart;

import com.javaeducase.ecommerce.dto.cart.CartDTO;
import com.javaeducase.ecommerce.dto.cart.RequestCartItemDTO;
import com.javaeducase.ecommerce.entity.cart.Cart;
import com.javaeducase.ecommerce.entity.cart.CartItem;
import com.javaeducase.ecommerce.entity.product.Offer;
import com.javaeducase.ecommerce.entity.user.User;
import com.javaeducase.ecommerce.repository.cart.CartRepository;
import com.javaeducase.ecommerce.repository.product.OfferRepository;
import com.javaeducase.ecommerce.repository.user.UserRepository;
import com.javaeducase.ecommerce.service.user.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CartServiceTest {

    @Mock
    private CartRepository cartRepository;

    @Mock
    private OfferRepository offerRepository;

    @Mock
    private CartItemService cartItemService;

    @Mock
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CartService cartService;

    private User user;
    private Cart cart;
    private Offer offer;
    private CartItem cartItem;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setCart(new Cart());

        cart = new Cart();
        cart.setId(1L);
        cart.setUser(user);
        cart.setItems(new ArrayList<>());
        cart.setTotalAmount(0);
        cart.setTotalQuantity(0);

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
        cartItem.setQuantity(2);
    }

    @Test
    void getCart_Success() {
        when(userService.getCurrentUser()).thenReturn(user);
        when(cartRepository.findByUserId(user.getId())).thenReturn(Optional.of(cart));

        CartDTO result = cartService.getCart();

        assertNotNull(result);
        verify(cartRepository).findByUserId(user.getId());
    }

    @Test
    void clearCart_Success() {
        when(userService.getCurrentUser()).thenReturn(user);
        when(cartRepository.findByUserId(user.getId())).thenReturn(Optional.of(cart));

        cartService.clearCart();

        assertEquals(0, cart.getTotalAmount());
        assertEquals(0, cart.getTotalQuantity());
        assertTrue(cart.getItems().isEmpty());
        verify(cartRepository).save(cart);
    }

    @Test
    void calculateCart_AddNewItem_Success() {
        RequestCartItemDTO requestCartItemDTO = new RequestCartItemDTO();
        requestCartItemDTO.setOfferId(offer.getId());
        requestCartItemDTO.setQuantity(3);

        when(cartRepository.findByUserId(user.getId())).thenReturn(Optional.of(cart));
        when(offerRepository.findById(offer.getId())).thenReturn(Optional.of(offer));
        when(cartItemService.createCartItem(offer.getId(), 3)).thenReturn(cartItem);

        CartDTO result = cartService.calculateCart(user.getId(), requestCartItemDTO);

        assertNotNull(result);
        assertEquals(1, cart.getItems().size());
        verify(cartItemService).createCartItem(offer.getId(), 3);
        verify(cartRepository).save(cart);
    }

    @Test
    void calculateCart_AddNewItem_StockQuantityExceeds() {
        RequestCartItemDTO requestCartItemDTO = new RequestCartItemDTO();
        requestCartItemDTO.setOfferId(offer.getId());
        requestCartItemDTO.setQuantity(15);

        when(cartRepository.findByUserId(user.getId())).thenReturn(Optional.of(cart));
        when(offerRepository.findById(offer.getId())).thenReturn(Optional.of(offer));

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> cartService.calculateCart(user.getId(), requestCartItemDTO)
        );

        assertEquals("Quantity of product with id: 1, " +
                "being added to the cart exceeds its stock availability", exception.getMessage());
    }

    @Test
    void calculateCart_AddNewItem_NegativeQuantity() {
        RequestCartItemDTO requestCartItemDTO = new RequestCartItemDTO();
        requestCartItemDTO.setOfferId(offer.getId());
        requestCartItemDTO.setQuantity(-5);

        when(cartRepository.findByUserId(user.getId())).thenReturn(Optional.of(cart));
        when(offerRepository.findById(offer.getId())).thenReturn(Optional.of(offer));

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> cartService.calculateCart(user.getId(), requestCartItemDTO)
        );

        assertEquals("Quantity must be greater than 0", exception.getMessage());
    }

    @Test
    void calculateCart_AddExistingItem_Success() {
        cart.addItem(cartItem);
        RequestCartItemDTO requestCartItemDTO = new RequestCartItemDTO();
        requestCartItemDTO.setOfferId(offer.getId());
        requestCartItemDTO.setQuantity(3);

        when(cartRepository.findByUserId(user.getId())).thenReturn(Optional.of(cart));
        when(offerRepository.findById(offer.getId())).thenReturn(Optional.of(offer));
        when(cartItemService.updateCartItem(cartItem.getId(), 5)).thenReturn(cartItem);

        CartDTO result = cartService.calculateCart(user.getId(), requestCartItemDTO);

        assertNotNull(result);
        assertEquals(1, cart.getItems().size());
        verify(cartItemService).updateCartItem(cartItem.getId(), 5);
        verify(cartRepository).save(cart);
    }

    @Test
    void calculateCart_AddExistingItem_StockQuantityExceeds() {
        cart.addItem(cartItem);
        RequestCartItemDTO requestCartItemDTO = new RequestCartItemDTO();
        requestCartItemDTO.setOfferId(offer.getId());
        requestCartItemDTO.setQuantity(9);

        when(cartRepository.findByUserId(user.getId())).thenReturn(Optional.of(cart));
        when(offerRepository.findById(offer.getId())).thenReturn(Optional.of(offer));

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> cartService.calculateCart(user.getId(), requestCartItemDTO)
        );

        assertEquals("Quantity of product with id: 1, " +
                "being added to the cart exceeds its stock availability", exception.getMessage());
    }

    @Test
    void calculateCart_AddExistingItem_NegativeQuantity() {
        RequestCartItemDTO requestCartItemDTO = new RequestCartItemDTO();
        requestCartItemDTO.setOfferId(offer.getId());
        requestCartItemDTO.setQuantity(-3);

        cart.addItem(cartItem);

        when(cartRepository.findByUserId(user.getId())).thenReturn(Optional.of(cart));
        when(offerRepository.findById(offer.getId())).thenReturn(Optional.of(offer));
        doNothing().when(cartItemService).deleteCartItem(cartItem.getId());

        CartDTO result = cartService.calculateCart(user.getId(), requestCartItemDTO);

        assertNotNull(result);
        assertTrue(result.getItems().isEmpty());
        verify(cartItemService, times(1)).deleteCartItem(cartItem.getId());
        verify(cartRepository, times(1)).save(cart);
    }
}

