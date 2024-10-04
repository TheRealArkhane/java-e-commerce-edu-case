package com.javaeducase.ecommerce.controllers.order;

import com.javaeducase.ecommerce.entities.order.Delivery;
import com.javaeducase.ecommerce.entities.order.PickupLocation;
import com.javaeducase.ecommerce.services.order.DeliveryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/deliveries")
@RequiredArgsConstructor
public class DeliveryController {

    private final DeliveryService deliveryService;

    @GetMapping
    public ResponseEntity<List<Delivery>> getAllDeliveries() {
        return ResponseEntity.ok(deliveryService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Delivery> getDeliveryById(@PathVariable Long id) {
        return ResponseEntity.ok(deliveryService.findById(id));
    }

    @GetMapping("/pickup_locations")
    public ResponseEntity<List<PickupLocation>> getPickupLocations() {
        return ResponseEntity.ok(deliveryService.getPickupLocations());
    }
}
