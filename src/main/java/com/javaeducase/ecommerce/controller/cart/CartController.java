package com.javaeducase.ecommerce.controller.cart;

import com.javaeducase.ecommerce.dto.cart.CartDTO;
import com.javaeducase.ecommerce.dto.cart.RequestCartItemDTO;
import com.javaeducase.ecommerce.service.cart.CartService;
import com.javaeducase.ecommerce.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;
    private final UserService userService;

    @GetMapping
    public ResponseEntity<CartDTO> getCurrentUserCart() {
       return ResponseEntity.ok(cartService.getCart());
    }

    @PostMapping("/calculate")
    public ResponseEntity<CartDTO> updateCurrentUserCart(@RequestBody RequestCartItemDTO requestCartItemDTO) {
        Long userId = userService.getCurrentUser().getId();
        CartDTO cartDTO = cartService.calculateCart(userId, requestCartItemDTO);
        return ResponseEntity.ok(cartDTO);
    }

    @DeleteMapping
    public ResponseEntity<Map<String, String>> clearCurrentUserCart() {
        cartService.clearCart();
        Map<String, String> responseBody = new HashMap<>();
        responseBody.put("status", "Cart is cleaned");
        return ResponseEntity.ok(responseBody);
    }
}
