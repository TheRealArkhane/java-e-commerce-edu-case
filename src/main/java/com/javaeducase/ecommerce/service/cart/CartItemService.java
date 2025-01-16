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
            throw new IllegalArgumentException("���������� ������ ���� ������ 0");
        }

        Offer offer = offerRepository.findById(offerId)
                .orElseThrow(() -> new OfferNotFoundException("����������� � id: " + offerId + " �� �������"));

        if (offer.getIsDeleted()) throw new OfferIsDeletedException("����������� ���� �������");
        if (!offer.getIsAvailable()) throw new OfferIsUnavailableException("����������� �� ������ ������ ����������");
        if (quantity > offer.getStockQuantity()) {
            throw new IllegalArgumentException("���������� �� ����� ���� ������, ��� �������� �� ������");
        }

        CartItem cartItem = new CartItem();
        cartItem.setOffer(offer);
        cartItem.setQuantity(quantity);
        return cartItemRepository.save(cartItem);
    }

    public CartItem updateCartItem(Long cartItemId, int newQuantity) {
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new CartItemNotFoundException("������� ������� � id: " + cartItemId + " �� ������"));

        Offer offer = cartItem.getOffer();

        if (newQuantity < 0) {
            throw new IllegalArgumentException("���������� �� ����� ���� �������������");
        } else if (newQuantity == 0) {
            deleteCartItem(cartItemId);
            return null;
        } else if (newQuantity > offer.getStockQuantity()) {
            throw new IllegalArgumentException("���������� �� ����� ���� ������, ��� �������� �� ������");
        }

        cartItem.setQuantity(newQuantity);
        return cartItemRepository.save(cartItem);
    }

    public void deleteCartItem(Long cartItemId) {
        cartItemRepository.deleteById(cartItemId);
    }
}



