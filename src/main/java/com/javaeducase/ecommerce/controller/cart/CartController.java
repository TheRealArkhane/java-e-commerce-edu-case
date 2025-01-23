package com.javaeducase.ecommerce.controller.cart;

import com.javaeducase.ecommerce.dto.cart.CartDTO;
import com.javaeducase.ecommerce.dto.cart.RequestCartItemDTO;
import com.javaeducase.ecommerce.handler.CustomErrorResponse;
import com.javaeducase.ecommerce.service.cart.CartService;
import com.javaeducase.ecommerce.service.user.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Tag(name = "Basic Cart (Customer)",
        description = "Methods for interacting with current user's Cart")
@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;
    private final UserService userService;


    @Operation(summary = "Get the current user's cart",
            description = "Retrieve the shopping cart for the logged-in user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the cart",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = CartDTO.class))}),
            @ApiResponse(responseCode = "404", description = "Cart not found for the user",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = CustomErrorResponse.class))})
    })
    @GetMapping
    public ResponseEntity<CartDTO> getCurrentUserCart() {
        return ResponseEntity.ok(cartService.getCart());
    }


    @Operation(summary = "Update the current user's cart",
            description = "Update the cart for the logged-in user by adding or removing items.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully updated the cart",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = CartDTO.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid quantity or product",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = CustomErrorResponse.class))}),
            @ApiResponse(responseCode = "404", description = "Product or cart item not found",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = CustomErrorResponse.class))})
    })
    @PostMapping("/calculate")
    public ResponseEntity<CartDTO> updateCurrentUserCart(@RequestBody RequestCartItemDTO requestCartItemDTO) {
        Long userId = userService.getCurrentUser().getId();
        CartDTO cartDTO = cartService.calculateCart(userId, requestCartItemDTO);
        return ResponseEntity.ok(cartDTO);
    }


    @Operation(summary = "Clear the current user's cart",
            description = "Remove all items from the logged-in user's cart.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Successfully cleared the cart",
                    content = {@Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Map.class))}),
            @ApiResponse(responseCode = "404", description = "Cart not found for the user",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = CustomErrorResponse.class))})
    })
    @DeleteMapping
    public ResponseEntity<Map<String, String>> clearCurrentUserCart() {
        cartService.clearCart();
        Map<String, String> responseBody = new HashMap<>();
        responseBody.put("status", "Cart is cleaned");
        return ResponseEntity.ok(responseBody);
    }
}
