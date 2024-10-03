package com.javaeducase.ecommerce.controllers.product.product;

import com.javaeducase.ecommerce.dto.product.OfferDTO;
import com.javaeducase.ecommerce.dto.product.ProductDTO;
import com.javaeducase.ecommerce.services.product.offer.AdminOfferService;
import com.javaeducase.ecommerce.services.product.product.AdminProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/admin/products")
@RequiredArgsConstructor
public class AdminProductController {

    private final AdminProductService adminProductService;
    private final AdminOfferService adminOfferService;


    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProductDTO> createProduct(@RequestBody ProductDTO productDTO) {
        ProductDTO createdProduct = adminProductService.createProduct(productDTO);
        return new ResponseEntity<>(createdProduct, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProductDTO> updateProduct(@PathVariable Long id, @RequestBody ProductDTO productDTO) {
        ProductDTO updatedProduct = adminProductService.updateProduct(id, productDTO);
        return ResponseEntity.ok(updatedProduct);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, String>> deleteProduct(@PathVariable Long id) {
        adminProductService.deleteProduct(id);
        Map<String, String> responseBody = new HashMap<>();
        responseBody.put("message", "Товар успешно удален");
        return ResponseEntity.ok(responseBody);
    }

    @PostMapping("/{productId}/offers")
    public ResponseEntity<OfferDTO> createProductOffer(@PathVariable Long productId, @RequestBody OfferDTO offerDTO) {
        OfferDTO createdOffer = adminOfferService.createProductOffer(productId, offerDTO);
        return new ResponseEntity<>(createdOffer, HttpStatus.CREATED);
    }
}