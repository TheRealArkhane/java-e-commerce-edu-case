package com.javaeducase.ecommerce.services.order;

import com.javaeducase.ecommerce.entities.order.Delivery;
import com.javaeducase.ecommerce.entities.order.PickupLocation;
import com.javaeducase.ecommerce.repositories.order.DeliveryRepository;
import com.javaeducase.ecommerce.repositories.order.PickupLocationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DeliveryService {

    private final DeliveryRepository deliveryRepository;
    private final PickupLocationRepository pickupLocationRepository;

    public List<Delivery> getAll() {
        return deliveryRepository.findAll();
    }

    public Delivery findById(Long id) {
        return deliveryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No delivery found with id: " + id));
    }

    public List<PickupLocation> getPickupLocations() {
        return pickupLocationRepository.findAll();
    }
}
