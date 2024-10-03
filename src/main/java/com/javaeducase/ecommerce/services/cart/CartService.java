package com.javaeducase.ecommerce.services.cart;

import com.javaeducase.ecommerce.dto.cart.CartDTO;
import com.javaeducase.ecommerce.dto.cart.CartItemDTO;
import com.javaeducase.ecommerce.dto.cart.RequestCartItemDTO;
import com.javaeducase.ecommerce.entities.cart.Cart;
import com.javaeducase.ecommerce.entities.cart.CartItem;
import com.javaeducase.ecommerce.entities.product.Offer;
import com.javaeducase.ecommerce.entities.user.User;
import com.javaeducase.ecommerce.exceptions.product.OfferIsDeletedException;
import com.javaeducase.ecommerce.exceptions.product.OfferIsUnavailableException;
import com.javaeducase.ecommerce.exceptions.product.OfferNotFoundException;
import com.javaeducase.ecommerce.repositories.cart.CartRepository;
import com.javaeducase.ecommerce.repositories.product.OfferRepository;
import com.javaeducase.ecommerce.repositories.user.UserRepository;
import com.javaeducase.ecommerce.services.user.UserService;
import com.javaeducase.ecommerce.utils.cart.CartUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final OfferRepository offerRepository;
    private final CartItemService cartItemService;
    private final UserService userService;
    private final CartUtils cartUtils;
    private final UserRepository userRepository;

    public CartDTO cartCalculate(RequestCartItemDTO requestCartItemDTO) {
        int quantity = requestCartItemDTO.getQuantity();
        Long offerId = requestCartItemDTO.getOfferId();

        if (quantity < 0) {
            throw new IllegalArgumentException("Quantity must be greater than 0");
        }

        Cart cart = findOrCreateCart();

        Offer offer = offerRepository.findById(offerId)
                .orElseThrow(() -> new OfferNotFoundException("Offer not found"));
        if (offer.getIsDeleted()) throw new OfferIsDeletedException("Offer is deleted");
        if (!offer.getIsAvailable()) throw new OfferIsUnavailableException("Offer is unavailable");

        Optional<CartItem> existingItem = cart.getItems().stream()
                .filter(item -> item.getOffer().getId().equals(offer.getId()))
                .findFirst();

        if (existingItem.isPresent()) {
            if (quantity > offer.getStockQuantity()) {
                throw new IllegalArgumentException("Quantity exceeds available stock");
            }
            if (quantity == 0) {
                cart.removeItem(existingItem.get());
                cartItemService.deleteCartItem(existingItem.get().getId());
            } else {
                cartItemService.updateCartItem(existingItem.get().getId(), quantity);
            }
        } else {
            if (quantity > offer.getStockQuantity()) {
                throw new IllegalArgumentException("Quantity exceeds available stock");
            }
            CartItem cartItem = cartItemService.createCartItem(offer.getId(), quantity);
            cart.addItem(cartItem);
        }

        cart.setTotalAmount(cart.getTotalAmount());
        cart.setTotalQuantity(cart.getTotalQuantity());
        cartRepository.save(cart);
        return cartUtils.convertCartToCartDTO(cart);
    }

    public CartDTO getCart() {
        Cart cart = findOrCreateCart();
        return cartUtils.convertCartToCartDTO(cart);
    }

    public void clearCart() {
        Cart cart = findOrCreateCart();
        cart.getItems().clear();
        cart.setTotalAmount(0);
        cart.setTotalQuantity(0);
        cartRepository.save(cart);
    }

    private Cart findOrCreateCart() {
        return cartRepository.findByUserId(
                userService.getCurrentUser().getId())
                .orElseGet(this::createNewCartForUser);
    }

    @Transactional
    public Cart createNewCartForUser() {
        User currentUser = userService.getCurrentUser();
        if (currentUser.getCart() != null) {
            return currentUser.getCart();
        }
        Cart newCart = new Cart();
        newCart.setUser(currentUser);
        newCart.setItems(new ArrayList<>());
        newCart.setTotalAmount(0);
        newCart.setTotalQuantity(0);
        currentUser.setCart(newCart);
        userRepository.save(currentUser);
        return newCart;
    }
}


