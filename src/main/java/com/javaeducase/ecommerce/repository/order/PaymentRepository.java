package com.javaeducase.ecommerce.repository.order;

import com.javaeducase.ecommerce.entity.order.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
}
