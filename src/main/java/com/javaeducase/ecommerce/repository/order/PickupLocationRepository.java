package com.javaeducase.ecommerce.repository.order;

import com.javaeducase.ecommerce.entity.order.PickupLocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PickupLocationRepository extends JpaRepository<PickupLocation, Long> {
    Optional<PickupLocation> findByAddress(String address);

    @Query("SELECT address FROM PickupLocation")
    List<String> findAllAddress();
}
