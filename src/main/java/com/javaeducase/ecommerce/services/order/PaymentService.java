package com.javaeducase.ecommerce.services.order;

import com.javaeducase.ecommerce.entities.order.Payment;
import com.javaeducase.ecommerce.exceptions.order.PaymentNotFoundException;
import com.javaeducase.ecommerce.repositories.order.PaymentRepository;
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
        return paymentRepository.findById(id).orElseThrow(() -> new PaymentNotFoundException("Способ оплаты не найден"));
    }
}
