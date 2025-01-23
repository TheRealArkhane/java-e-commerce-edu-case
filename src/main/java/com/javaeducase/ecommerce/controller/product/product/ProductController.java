package com.javaeducase.ecommerce.controller.product.product;

import com.javaeducase.ecommerce.dto.product.OfferDTO;
import com.javaeducase.ecommerce.dto.product.ProductDTO;
import com.javaeducase.ecommerce.handler.CustomErrorResponse;
import com.javaeducase.ecommerce.service.product.product.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;


    @Operation(summary = "Get all products",
            description = "Retrieve a list of all products, including deleted ones.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Successfully retrieved the list of all products",
                    content = {@Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ProductDTO.class))})
    })
    @GetMapping
    public ResponseEntity<List<ProductDTO>> getAllProducts() {
        return ResponseEntity.ok(productService.getAllProducts());
    }


    @Operation(summary = "Get all active products",
            description = "Retrieve a list of all active (not marked as isDeleted = true) products.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Successfully retrieved the list of active products",
                    content = {@Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ProductDTO.class))})
    })
    @GetMapping("/active")
    public ResponseEntity<List<ProductDTO>> getAllActiveProducts() {
        return ResponseEntity.ok(productService.getAllActiveProducts());
    }


    @Operation(summary = "Get product by ID",
            description = "Retrieve the details of a product by ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Successfully retrieved the product",
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
    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> getProductById(@PathVariable Long id) {
        return ResponseEntity.ok(productService.getProductById(id));
    }


    @Operation(summary = "Get all offers of a product",
            description = "Retrieve all offers associated with product by product ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Successfully retrieved the offers of the product",
                    content = {@Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = OfferDTO.class))}),
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
    @GetMapping("/{id}/offers")
    public ResponseEntity<List<OfferDTO>> getAllOffersOfProduct(@PathVariable Long id) {
        return ResponseEntity.ok(productService.getAllOffersOfProduct(id));
    }
}

