package com.javaeducase.ecommerce.controller.product.product;

import com.javaeducase.ecommerce.dto.product.ProductDTO;
import com.javaeducase.ecommerce.handler.CustomErrorResponse;
import com.javaeducase.ecommerce.service.product.product.AdminProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Tag(name = "Admin Products (Administrator)",
        description = "Methods for managing Products")
@RestController
@RequestMapping("/admin/products")
@RequiredArgsConstructor
public class AdminProductController {

    private final AdminProductService adminProductService;


    @Operation(summary = "Create a new product",
            description = "Creates a new product and saves it to the db.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201",
                    description = "Product successfully created",
                    content = {@Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ProductDTO.class))}),
            @ApiResponse(responseCode = "400",
                    description = "Invalid input data",
                    content = {@Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CustomErrorResponse.class))})
    })
    @PostMapping("/create")
    public ResponseEntity<ProductDTO> createProduct(@RequestBody ProductDTO productDTO) {
        ProductDTO createdProduct = adminProductService.createProduct(productDTO);
        return new ResponseEntity<>(createdProduct, HttpStatus.CREATED);
    }


    @Operation(summary = "Update an existing product",
            description = "Updates an existing product's details by ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Product successfully updated",
                    content = {@Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ProductDTO.class))}),
            @ApiResponse(responseCode = "404",
                    description = "Product not found",
                    content = {@Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CustomErrorResponse.class))}),
            @ApiResponse(responseCode = "409",
                    description = "Product is deleted",
                    content = {@Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CustomErrorResponse.class))})
    })
    @PutMapping("/{id}")
    public ResponseEntity<ProductDTO> updateProduct(@PathVariable Long id, @RequestBody ProductDTO productDTO) {
        ProductDTO updatedProduct = adminProductService.updateProduct(id, productDTO);
        return ResponseEntity.ok(updatedProduct);
    }


    @Operation(summary = "Delete a product",
            description = "Marks a product as deleted(isDeleted = true) " +
                    "and also marks all its associated offers as deleted.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Product successfully deleted",
                    content = {@Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Map.class))}),
            @ApiResponse(responseCode = "404",
                    description = "Product not found",
                    content = {@Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CustomErrorResponse.class))}),
            @ApiResponse(responseCode = "409",
                    description = "Product is already deleted",
                    content = {@Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CustomErrorResponse.class))})
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteProduct(@PathVariable Long id) {
        adminProductService.deleteProduct(id);
        Map<String, String> responseBody = new HashMap<>();
        responseBody.put("message", "Product successfully deleted");
        return ResponseEntity.ok(responseBody);
    }
}