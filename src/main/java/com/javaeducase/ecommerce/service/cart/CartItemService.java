package com.javaeducase.ecommerce.service.cart;

import com.javaeducase.ecommerce.entity.cart.CartItem;
import com.javaeducase.ecommerce.entity.product.Offer;
import com.javaeducase.ecommerce.exception.cart.CartItemNotFoundException;
import com.javaeducase.ecommerce.exception.product.OfferIsDeletedException;
import com.javaeducase.ecommerce.exception.product.OfferIsUnavailableException;
import com.javaeducase.ecommerce.exception.product.OfferNotFoundException;
import com.javaeducase.ecommerce.repository.cart.CartItemRepository;
import com.javaeducase.ecommerce.repository.product.OfferRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CartItemService {

    private final CartItemRepository cartItemRepository;
    private final OfferRepository offerRepository;

    public CartItem createCartItem(Long offerId, int quantity) {
        log.info("Creating cart item for offer id: {} with quantity: {}...", offerId, quantity);

        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than 0");
        }

        Offer offer = offerRepository.findById(offerId)
                .orElseThrow(() -> new OfferNotFoundException("Offer with id: " + offerId + " not found"));

        if (offer.getIsDeleted()) {
            throw new OfferIsDeletedException("Offer has been deleted");
        }
        if (!offer.getIsAvailable()) {
            throw new OfferIsUnavailableException("Offer is unavailable");
        }
        if (quantity > offer.getStockQuantity()) {
            throw new IllegalArgumentException("Quantity cannot exceed available stock");
        }

        CartItem cartItem = new CartItem();
        cartItem.setOffer(offer);
        cartItem.setQuantity(quantity);
        CartItem savedCartItem = cartItemRepository.save(cartItem);
        log.info("Cart item created successfully for offer id: {} with quantity: {}", offerId, quantity);
        return savedCartItem;
    }

    public CartItem updateCartItem(Long cartItemId, int newQuantity) {
        log.info("Updating cart item with id: {} to new quantity: {}...", cartItemId, newQuantity);

        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new CartItemNotFoundException("Cart item with id: " + cartItemId + " not found"));

        Offer offer = cartItem.getOffer();

        if (newQuantity < 0) {
            throw new IllegalArgumentException("Quantity cannot be negative");
        } else if (newQuantity == 0) {
            log.info("New quantity is 0. Deleting cart item with id: {}...", cartItemId);
            deleteCartItem(cartItemId);
            return null;
        } else if (newQuantity > offer.getStockQuantity()) {
            throw new IllegalArgumentException("Quantity cannot exceed available stock");
        }

        cartItem.setQuantity(newQuantity);
        CartItem updatedCartItem = cartItemRepository.save(cartItem);
        log.info("Cart item with id: {} updated successfully to new quantity: {}", cartItemId, newQuantity);
        return updatedCartItem;
    }

    public void deleteCartItem(Long cartItemId) {
        log.info("Deleting cart item with id: {}...", cartItemId);
        if (!cartItemRepository.existsById(cartItemId)) {
            throw new CartItemNotFoundException("Cart item with id: " + cartItemId + " not found");
        }
        cartItemRepository.deleteById(cartItemId);
        log.info("Cart item with id: {} deleted successfully", cartItemId);
    }
}




