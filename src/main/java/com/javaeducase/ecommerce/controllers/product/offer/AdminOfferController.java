package com.javaeducase.ecommerce.controllers.product.offer;

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


    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<OfferDTO> updateOffer(@PathVariable Long id, @RequestBody OfferDTO offerDTO) {
        return ResponseEntity.ok(adminOfferService.updateOffer(id, offerDTO));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, String>> deleteOffer(@PathVariable Long id) {
        adminOfferService.deleteOffer(id);
        Map <String, String> responseBody = new HashMap<>();
        responseBody.put("message", "Предложение успешно удалено");
        return ResponseEntity.ok(responseBody);
    }

    @PostMapping("/{id}/attribute")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<OfferDTO> addAttribute(@PathVariable Long id, @RequestParam Long attributeId) {
        adminOfferService.addAttributeToOffer(id, attributeId);
        return ResponseEntity.ok(offerService.getOfferById(id));
    }

    @DeleteMapping("/{id}/attribute")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<OfferDTO> removeAttribute(@PathVariable Long id, @RequestParam Long attributeId) {
        return ResponseEntity.ok(adminOfferService.deleteAttributeFromOffer(id, attributeId));
    }

}
