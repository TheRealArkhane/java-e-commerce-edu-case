package com.javaeducase.ecommerce.controllers.product.offer;

import com.javaeducase.ecommerce.dto.product.AttributeDTO;
import com.javaeducase.ecommerce.dto.product.OfferDTO;
import com.javaeducase.ecommerce.services.product.offer.OfferService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/offers")
@RequiredArgsConstructor
public class OfferController {

    private final OfferService offerService;

    @GetMapping("/{id}")
    public ResponseEntity<OfferDTO> getOfferById(@PathVariable Long id) {
        return ResponseEntity.ok(offerService.getOfferById(id));
    }

    @GetMapping("/{id}/attributes")
    public ResponseEntity<List<AttributeDTO>> getOfferAttributes(@PathVariable Long id) {
        return ResponseEntity.ok(offerService.getAttributesByOfferId(id));
    }
}
