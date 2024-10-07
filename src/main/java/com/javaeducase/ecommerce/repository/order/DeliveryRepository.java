package com.javaeducase.ecommerce.repository.order;

import com.javaeducase.ecommerce.entity.order.Delivery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeliveryRepository extends JpaRepository<Delivery, Long> {
}
