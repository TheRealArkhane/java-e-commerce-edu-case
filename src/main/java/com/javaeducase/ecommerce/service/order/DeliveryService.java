package com.javaeducase.ecommerce.service.order;

import com.javaeducase.ecommerce.entity.order.Delivery;
import com.javaeducase.ecommerce.entity.order.PickupLocation;
import com.javaeducase.ecommerce.exception.order.DeliveryNotFoundException;
import com.javaeducase.ecommerce.repository.order.DeliveryRepository;
import com.javaeducase.ecommerce.repository.order.PickupLocationRepository;
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
                .orElseThrow(() -> new DeliveryNotFoundException("Не найден способ доставки с id: " + id));
    }

    public List<PickupLocation> getPickupLocations() {
        return pickupLocationRepository.findAll();
    }
}
