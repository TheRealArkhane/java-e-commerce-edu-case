package com.javaeducase.ecommerce.service.order;

import com.javaeducase.ecommerce.entity.order.Payment;
import com.javaeducase.ecommerce.exception.order.PaymentNotFoundException;
import com.javaeducase.ecommerce.repository.order.PaymentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;

    public List<Payment> getPayments() {
        log.info("Fetching all payments...");
        List<Payment> payments = paymentRepository.findAll();
        log.info("Successfully fetched {} payments", payments.size());
        return payments;
    }

    public Payment getPaymentById(Long id) {
        return paymentRepository.findById(id)
                .orElseThrow(() -> new PaymentNotFoundException("Payment with id: " + id + " not found"));
    }
}

