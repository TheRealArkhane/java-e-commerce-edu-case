package com.javaeducase.ecommerce.repositories.order;

import com.javaeducase.ecommerce.entities.order.Delivery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeliveryRepository extends JpaRepository<Delivery, Long> {
}
