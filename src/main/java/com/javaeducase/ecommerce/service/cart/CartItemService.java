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
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CartItemService {

    private final CartItemRepository cartItemRepository;
    private final OfferRepository offerRepository;

    public CartItem createCartItem(Long offerId, int quantity) {

        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than 0");
        }

        Offer offer = offerRepository.findById(offerId)
                .orElseThrow(() -> new OfferNotFoundException("Offer with id: " + offerId + " not found"));

        if (offer.getIsDeleted()) throw new OfferIsDeletedException("Offer has been deleted");
        if (!offer.getIsAvailable()) throw new OfferIsUnavailableException("Offer is unavailable");
        if (quantity > offer.getStockQuantity()) {
            throw new IllegalArgumentException("Quantity cannot exceed available stock");
        }

        CartItem cartItem = new CartItem();
        cartItem.setOffer(offer);
        cartItem.setQuantity(quantity);
        return cartItemRepository.save(cartItem);
    }

    public CartItem updateCartItem(Long cartItemId, int newQuantity) {
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new CartItemNotFoundException("Cart item with id: " + cartItemId + " not found"));

        Offer offer = cartItem.getOffer();

        if (newQuantity < 0) {
            throw new IllegalArgumentException("Quantity cannot be negative");
        } else if (newQuantity == 0) {
            deleteCartItem(cartItemId);
            return null;
        } else if (newQuantity > offer.getStockQuantity()) {
            throw new IllegalArgumentException("Quantity cannot exceed available stock");
        }

        cartItem.setQuantity(newQuantity);
        return cartItemRepository.save(cartItem);
    }

    public void deleteCartItem(Long cartItemId) {
        cartItemRepository.deleteById(cartItemId);
    }
}



