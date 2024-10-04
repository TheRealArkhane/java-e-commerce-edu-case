package com.javaeducase.ecommerce.repositories.order;

import com.javaeducase.ecommerce.entities.order.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
}
