package com.javaeducase.ecommerce.service.order;

import com.javaeducase.ecommerce.entity.order.Delivery;
import com.javaeducase.ecommerce.entity.order.PickupLocation;
import com.javaeducase.ecommerce.exception.order.DeliveryNotFoundException;
import com.javaeducase.ecommerce.repository.order.DeliveryRepository;
import com.javaeducase.ecommerce.repository.order.PickupLocationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class DeliveryService {

    private final DeliveryRepository deliveryRepository;
    private final PickupLocationRepository pickupLocationRepository;

    public List<Delivery> getDeliveries() {
        log.info("Fetching all deliveries...");
        List<Delivery> deliveries = deliveryRepository.findAll();
        log.info("Successfully fetched {} deliveries", deliveries.size());
        return deliveries;
    }

    public Delivery getDeliveryById(Long id) {
        return deliveryRepository.findById(id)
                .orElseThrow(() -> new DeliveryNotFoundException("Delivery with id: " + id + " not found"));
    }

    public List<PickupLocation> getPickupLocations() {
        log.info("Fetching all pickup locations...");
        List<PickupLocation> pickupLocations = pickupLocationRepository.findAll();
        log.info("Successfully fetched {} pickup locations", pickupLocations.size());
        return pickupLocations;
    }
}

