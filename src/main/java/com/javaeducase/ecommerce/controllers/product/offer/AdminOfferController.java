package com.javaeducase.ecommerce.controllers.product.offer;

import com.javaeducase.ecommerce.dto.product.AttributeRequestDTO;
import com.javaeducase.ecommerce.dto.product.CreateOfferRequestDTO;
import com.javaeducase.ecommerce.dto.product.OfferDTO;
import com.javaeducase.ecommerce.services.product.offer.AdminOfferService;
import com.javaeducase.ecommerce.services.product.offer.OfferService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/admin/offers")
@RequiredArgsConstructor
public class AdminOfferController {

    private final AdminOfferService adminOfferService;
    private final OfferService offerService;


    @PostMapping("/create")
    public ResponseEntity<OfferDTO> createProductOffer(@RequestBody CreateOfferRequestDTO createOfferRequestDTO) {
        OfferDTO createdOffer = adminOfferService.createProductOffer(createOfferRequestDTO);
        return new ResponseEntity<>(createdOffer, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<OfferDTO> updateOffer(@PathVariable Long id, @RequestBody OfferDTO offerDTO) {
        return ResponseEntity.ok(adminOfferService.updateOffer(id, offerDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteOffer(@PathVariable Long id) {
        adminOfferService.deleteOffer(id);
        Map <String, String> responseBody = new HashMap<>();
        responseBody.put("message", "Предложение успешно удалено");
        return ResponseEntity.ok(responseBody);
    }

    @PostMapping("/add_attribute_to_offer")
    public ResponseEntity<OfferDTO> addAttribute(@RequestBody AttributeRequestDTO attributeRequestDTO) {
        adminOfferService.addAttributeToOffer(attributeRequestDTO);
        return ResponseEntity.ok(offerService.getOfferById(attributeRequestDTO.getOfferId()));
    }

    @DeleteMapping("/remove_attribute_from_offer")
    public ResponseEntity<OfferDTO> removeAttribute(@RequestBody AttributeRequestDTO attributeRequestDTO) {
        return ResponseEntity.ok(adminOfferService.deleteAttributeFromOffer(attributeRequestDTO));
    }

}
