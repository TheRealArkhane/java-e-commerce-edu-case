package com.javaeducase.ecommerce.service.order;

import com.javaeducase.ecommerce.entity.order.Payment;
import com.javaeducase.ecommerce.exception.order.PaymentNotFoundException;
import com.javaeducase.ecommerce.repository.order.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;


    public List<Payment> getPayments() {
        return paymentRepository.findAll();
    }

    public Payment getPaymentById(Long id) {
        return paymentRepository.findById(id)
                .orElseThrow(() -> new PaymentNotFoundException("Payment with id: " + id + " not found"));
    }
}
