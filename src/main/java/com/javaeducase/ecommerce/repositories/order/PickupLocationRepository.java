package com.javaeducase.ecommerce.repositories.order;

import com.javaeducase.ecommerce.entities.order.PickupLocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PickupLocationRepository extends JpaRepository<PickupLocation, Long> {
    List<PickupLocation> findAll();
    Optional<PickupLocation> findByAddress(String address);
}