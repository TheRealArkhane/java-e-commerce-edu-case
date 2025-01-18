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
    private final UserRepository userRepository;

    public CartDTO calculateCart(Long userId, RequestCartItemDTO requestCartItemDTO) {
        int quantity = requestCartItemDTO.getQuantity();
        Long offerId = requestCartItemDTO.getOfferId();

        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new CartNotFoundException("Корзина пользователя с id: " + userId + " не найдена"));

        Offer offer = offerRepository.findById(offerId)
                .orElseThrow(() -> new OfferNotFoundException("Товар с id: " + offerId + " не найден"));
        if (offer.getIsDeleted()) throw new OfferIsDeletedException("Товар был ранее удален");
        if (!offer.getIsAvailable()) throw new OfferIsUnavailableException("Товар недоступен");

        Optional<CartItem> existingItem = cart.getItems().stream()
                .filter(item -> item.getOffer().getId().equals(offer.getId()))
                .findFirst();

        if (existingItem.isPresent()) {
            int existingItemQuantitySum = existingItem.get().getQuantity() + quantity;
            if (existingItemQuantitySum > offer.getStockQuantity()) {
                throw new IllegalArgumentException("Количество товара с id: "
                        + offer.getId()
                        +", добавляемого в корзину, превышает его кол-во на складе");
            }
            if (existingItemQuantitySum <= 0) {
                cart.removeItem(existingItem.get());
                cartItemService.deleteCartItem(existingItem.get().getId());
            } else {
                cartItemService.updateCartItem(existingItem.get().getId(), existingItemQuantitySum);
            }
        } else {
            if (quantity <= 0) {
                throw new IllegalArgumentException("Количество добавляемого должно быть > 0");
            }
            else if (quantity > offer.getStockQuantity()) {
                throw new IllegalArgumentException("Количество товара с id: "
                        + offer.getId()
                        +" добавляемого в корзину, превышает его кол-во на складе");
            }
            CartItem cartItem = cartItemService.createCartItem(offer.getId(), quantity);
            cart.addItem(cartItem);
        }

        cart.setTotalAmount(cart.calculateTotalAmount());
        cart.setTotalQuantity(cart.calculateTotalQuantity());
        cartRepository.save(cart);
        return CartUtils.convertCartToCartDTO(cart);
    }

    public CartDTO getCart() {
        Cart cart = findOrCreateCart();
        return CartUtils.convertCartToCartDTO(cart);
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


