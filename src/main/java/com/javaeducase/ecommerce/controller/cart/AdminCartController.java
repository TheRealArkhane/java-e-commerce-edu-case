package com.javaeducase.ecommerce.controller.cart;

import com.javaeducase.ecommerce.dto.cart.CartDTO;
import com.javaeducase.ecommerce.dto.cart.RequestCartItemDTO;
import com.javaeducase.ecommerce.handler.CustomErrorResponse;
import com.javaeducase.ecommerce.service.cart.AdminCartService;
import com.javaeducase.ecommerce.service.cart.CartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/admin/cart")
@RequiredArgsConstructor
public class AdminCartController {

    private final CartService cartService;
    private final AdminCartService adminCartService;


    @Operation(summary = "Get a user's cart by ID",
            description = "Retrieve the shopping cart for a specific user by their ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Successfully retrieved the cart",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = CartDTO.class))}),
            @ApiResponse(responseCode = "404", description = "User's cart not found",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = CustomErrorResponse.class))})
    })
    @GetMapping("/{id}")
    public ResponseEntity<CartDTO> getUserCartById(@PathVariable Long id) {
        return ResponseEntity.ok(adminCartService.getUserCart(id));
    }


    @Operation(summary = "Update a user's cart by ID",
            description = "Update the cart for a specific user by their ID by adding or removing items.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully updated the user's cart",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = CartDTO.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid quantity or product",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = CustomErrorResponse.class))}),
            @ApiResponse(responseCode = "404", description = "Product or cart item not found",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = CustomErrorResponse.class))})
    })
    @PostMapping("/{id}/calculate")
    public ResponseEntity<CartDTO> updateUserCart(@PathVariable Long id,
                                                  @RequestBody RequestCartItemDTO requestCartItemDTO) {
        CartDTO cartDTO = cartService.calculateCart(id, requestCartItemDTO);
        return ResponseEntity.ok(cartDTO);
    }


    @Operation(summary = "Clear a user's cart by ID",
            description = "Clear all items from a user's cart by their ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Successfully cleared the user's cart",
                    content = {@Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Map.class))}),
            @ApiResponse(responseCode = "404",
                    description = "User's cart not found",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = CustomErrorResponse.class))})
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteUserCart(@PathVariable Long id) {
        adminCartService.clearUserCart(id);
        Map<String, String> responseBody = new HashMap<>();
        responseBody.put("message", "Cart of user with id: " + id + " is cleaned");
        return ResponseEntity.ok(responseBody);
    }
}
