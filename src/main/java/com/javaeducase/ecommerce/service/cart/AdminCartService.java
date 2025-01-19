package com.javaeducase.ecommerce.service.cart;

import com.javaeducase.ecommerce.dto.cart.CartDTO;
import com.javaeducase.ecommerce.entity.cart.Cart;
import com.javaeducase.ecommerce.exception.cart.CartNotFoundException;
import com.javaeducase.ecommerce.repository.cart.CartRepository;
import com.javaeducase.ecommerce.util.cart.CartUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminCartService {

    private final CartRepository cartRepository;

    public CartDTO getUserCart(Long userId) {
        log.info("Fetching cart for user with id: {}...", userId);
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new CartNotFoundException("Cart of user with id: " + userId + " not found"));
        log.info("Cart fetched successfully for user with id: {}", userId);
        return CartUtils.convertCartToCartDTO(cart);
    }

    public void clearUserCart(Long userId) {
        log.info("Clearing cart for user with id: {}...", userId);
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new CartNotFoundException("Cart of user with id: " + userId + " not found"));
        cart.getItems().clear();
        cart.setTotalQuantity(0);
        cart.setTotalAmount(0);
        cartRepository.save(cart);
        log.info("Cart cleared successfully for user with id: {}", userId);
    }
}

