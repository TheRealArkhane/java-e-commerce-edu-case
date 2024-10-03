package com.javaeducase.ecommerce.services.cart;

import com.javaeducase.ecommerce.dto.cart.CartDTO;
import com.javaeducase.ecommerce.entities.cart.Cart;
import com.javaeducase.ecommerce.exceptions.cart.CartNotFoundException;
import com.javaeducase.ecommerce.repositories.cart.CartRepository;
import com.javaeducase.ecommerce.utils.cart.CartUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminCartService {

    private final CartRepository cartRepository;
    private final CartUtils cartUtils;

    public CartDTO getUserCart(Long userId) {
        Cart cart = cartRepository.findByUserId(userId).orElseThrow(() -> new CartNotFoundException("Cart not found"));
        return cartUtils.convertCartToCartDTO(cart);
    }

    public void clearUserCart(Long userId) {
        Cart cart = cartRepository.findByUserId(userId).orElseThrow(() -> new CartNotFoundException("Cart not found"));
        cart.getItems().clear();
        cart.setTotalQuantity(0);
        cart.setTotalAmount(0);
        cartRepository.save(cart);
    }
}
