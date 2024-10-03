package com.javaeducase.ecommerce.controllers.cart;

import com.javaeducase.ecommerce.dto.cart.CartDTO;
import com.javaeducase.ecommerce.dto.cart.RequestCartItemDTO;
import com.javaeducase.ecommerce.services.cart.CartService;
import com.javaeducase.ecommerce.services.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
        responseBody.put("status", "Корзина очищена");
        return ResponseEntity.ok(responseBody);
    }
}
