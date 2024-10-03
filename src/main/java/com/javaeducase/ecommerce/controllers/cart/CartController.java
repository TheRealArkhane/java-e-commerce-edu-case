package com.javaeducase.ecommerce.controllers.cart;

import com.javaeducase.ecommerce.dto.cart.CartDTO;
import com.javaeducase.ecommerce.dto.cart.RequestCartItemDTO;
import com.javaeducase.ecommerce.services.cart.CartService;
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

    @GetMapping
    public ResponseEntity<CartDTO> getCurrentUserCart() {
       return ResponseEntity.ok(cartService.getCart());
    }

    @PostMapping("/calculate")
    public ResponseEntity<CartDTO> calculateCart(@RequestBody RequestCartItemDTO requestCartItemDTO) {
        CartDTO updatedCart = cartService.cartCalculate(requestCartItemDTO);
        return new ResponseEntity<>(updatedCart, HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity<Map<String, String>> clearCurrentUserCart() {
        cartService.clearCart();
        Map<String, String> responseBody = new HashMap<>();
        responseBody.put("status", "Корзина очищена");
        return ResponseEntity.ok(responseBody);
    }
}
