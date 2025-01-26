package com.javaeducase.ecommerce.service.cart;

import com.javaeducase.ecommerce.dto.cart.CartDTO;
import com.javaeducase.ecommerce.dto.cart.RequestCartItemDTO;
import com.javaeducase.ecommerce.entity.cart.Cart;
import com.javaeducase.ecommerce.entity.cart.CartItem;
import com.javaeducase.ecommerce.entity.product.Offer;
import com.javaeducase.ecommerce.entity.user.User;
import com.javaeducase.ecommerce.exception.cart.CartNotFoundException;
import com.javaeducase.ecommerce.exception.product.OfferIsDeletedException;
import com.javaeducase.ecommerce.exception.product.OfferIsUnavailableException;
import com.javaeducase.ecommerce.exception.product.OfferNotFoundException;
import com.javaeducase.ecommerce.repository.cart.CartRepository;
import com.javaeducase.ecommerce.repository.product.OfferRepository;
import com.javaeducase.ecommerce.repository.user.UserRepository;
import com.javaeducase.ecommerce.service.user.UserService;
import com.javaeducase.ecommerce.util.cart.CartUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final OfferRepository offerRepository;
    private final CartItemService cartItemService;
    private final UserService userService;
    private final UserRepository userRepository;

    public CartDTO calculateCart(Long userId, RequestCartItemDTO requestCartItemDTO) {
        log.info("Calculating cart for user with id: {}...", userId);
        int quantity = requestCartItemDTO.getQuantity();
        Long offerId = requestCartItemDTO.getOfferId();

        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new CartNotFoundException("User cart with id: " + userId + " not found"));

        Offer offer = offerRepository.findById(offerId)
                .orElseThrow(() -> new OfferNotFoundException("Product with id: " + offerId + " not found"));

        if (offer.getIsDeleted()) {
            throw new OfferIsDeletedException("The product was deleted");
        }
        if (!offer.getIsAvailable()) {
            throw new OfferIsUnavailableException("The product is unavailable");
        }

        Optional<CartItem> existingItem = cart.getItems().stream()
                .filter(item -> item.getOffer().getId().equals(offer.getId()))
                .findFirst();

        if (existingItem.isPresent()) {
            int existingItemQuantitySum = existingItem.get().getQuantity() + quantity;
            if (existingItemQuantitySum > offer.getStockQuantity()) {
                throw new IllegalArgumentException("Quantity of product with id: "
                        + offerId
                        + ", being added to the cart exceeds its stock availability");
            }
            if (existingItemQuantitySum <= 0) {
                log.info("Removing item with offer id: {} from cart...", offerId);
                cart.removeItem(existingItem.get());
                cartItemService.deleteCartItem(existingItem.get().getId());
                log.info("Item with offer id: {} successfully removed from cart...", offerId);
            } else {
                log.info("Updating item quantity for offer id: {} in cart...", offerId);
                cartItemService.updateCartItem(existingItem.get().getId(), existingItemQuantitySum);
                log.info("Item with offer id: {} was successfully updated", offerId);
            }
        } else {
            if (quantity <= 0) {
                throw new IllegalArgumentException("Quantity must be greater than 0");
            }
            if (quantity > offer.getStockQuantity()) {
                throw new IllegalArgumentException("Quantity of product with id: "
                        + offerId
                        + ", being added to the cart exceeds its stock availability");
            }
            log.info("Adding new item with offer id: {} to the cart...", offerId);
            CartItem cartItem = cartItemService.createCartItem(offer.getId(), quantity);
            cart.addItem(cartItem);
            log.info("Item with offer id: {} was successfully added to cart", offerId);
        }

        cart.setTotalAmount(cart.calculateTotalAmount());
        cart.setTotalQuantity(cart.calculateTotalQuantity());
        cartRepository.save(cart);
        log.info("Cart for user id: {} calculated successfully", userId);
        return CartUtils.convertCartToCartDTO(cart);
    }

    public CartDTO getCart() {
        Cart cart = findOrCreateCart();
        log.info("Fetched cart for user with id: {}", userService.getCurrentUser().getId());
        return CartUtils.convertCartToCartDTO(cart);
    }

    public void clearCart() {
        Cart cart = findOrCreateCart();
        log.info("Clearing cart for user with id: {}...", userService.getCurrentUser().getId());
        cart.getItems().clear();
        cart.setTotalAmount(0);
        cart.setTotalQuantity(0);
        cartRepository.save(cart);
        log.info("Cart cleared for user with id: {}", userService.getCurrentUser().getId());
    }

    private Cart findOrCreateCart() {
        return cartRepository.findByUserId(
                        userService.getCurrentUser().getId())
                .orElseGet(this::createNewCartForUser);
    }

    @Transactional
    protected Cart createNewCartForUser() {
        User currentUser = userService.getCurrentUser();
        if (currentUser.getCart() != null) {
            log.info("User with id: {} already have a cart", currentUser.getId());
            return currentUser.getCart();
        }
        log.info("Creating new cart for user with id: {}", currentUser.getId());
        Cart newCart = new Cart();
        newCart.setUser(currentUser);
        newCart.setItems(new ArrayList<>());
        newCart.setTotalAmount(0);
        newCart.setTotalQuantity(0);
        currentUser.setCart(newCart);
        userRepository.save(currentUser);
        log.info("New cart created for user with id: {}", currentUser.getId());
        return newCart;
    }
}



