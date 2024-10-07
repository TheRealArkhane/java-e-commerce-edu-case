package com.javaeducase.ecommerce.util.cart;

import com.javaeducase.ecommerce.dto.cart.CartDTO;
import com.javaeducase.ecommerce.dto.cart.CartItemDTO;
import com.javaeducase.ecommerce.entity.cart.Cart;
import com.javaeducase.ecommerce.entity.cart.CartItem;
import com.javaeducase.ecommerce.util.product.CommonAllProductLinkedUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CartUtils {

    private final CommonAllProductLinkedUtils commonAllProductLinkedUtils;


    public CartDTO convertCartToCartDTO(Cart cart) {
        CartDTO cartDTO = new CartDTO();
        cartDTO.setItems(cart.getItems().stream().map(this::convertCartItemToCartItemDTO).collect(Collectors.toList()));
        cartDTO.setTotalAmount(cart.getTotalAmount());
        cartDTO.setTotalQuantity(cart.getTotalQuantity());
        return cartDTO;
    }


    private CartItemDTO convertCartItemToCartItemDTO(CartItem cartItem) {
        CartItemDTO cartItemDTO = new CartItemDTO();
        cartItemDTO.setOffer(commonAllProductLinkedUtils.convertOfferToOfferDTO(cartItem.getOffer()));
        cartItemDTO.setQuantity(cartItem.getQuantity());
        return cartItemDTO;
    }
}
