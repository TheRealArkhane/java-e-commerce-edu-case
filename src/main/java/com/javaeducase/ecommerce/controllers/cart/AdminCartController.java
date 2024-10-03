package com.javaeducase.ecommerce.controllers.cart;

import com.javaeducase.ecommerce.dto.cart.CartDTO;
import com.javaeducase.ecommerce.dto.cart.RequestCartItemDTO;
import com.javaeducase.ecommerce.services.cart.AdminCartService;
import com.javaeducase.ecommerce.services.cart.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/admin/cart")
@RequiredArgsConstructor
public class AdminCartController {

    private final CartService cartService;
    private final AdminCartService adminCartService;

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CartDTO> getUserCartById(@PathVariable Long id) {
        return ResponseEntity.ok(adminCartService.getUserCart(id));
    }

    @PostMapping("/{id}/calculate")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CartDTO> updateUserCart(@PathVariable Long id,
                                                  @RequestBody RequestCartItemDTO requestCartItemDTO) {
        CartDTO cartDTO = cartService.calculateCart(id, requestCartItemDTO);
        return ResponseEntity.ok(cartDTO);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, String>> deleteUserCart(@PathVariable Long id) {
        adminCartService.clearUserCart(id);
        Map<String, String> responseBody = new HashMap<>();
        responseBody.put("message", "Корзина пользователя с id: " + id + " очищена");
        return ResponseEntity.ok(responseBody);
    }
}
