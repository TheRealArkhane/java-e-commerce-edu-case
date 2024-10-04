package com.javaeducase.ecommerce.entities.order;

import com.javaeducase.ecommerce.entities.cart.Cart;
import com.javaeducase.ecommerce.entities.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "cart_id", nullable = false)
    private Cart cart;

    @Column(name = "address", nullable = false)
    private String address;

    @ManyToOne
    @JoinColumn(name = "delivery_id", nullable = false)
    private Delivery delivery;

    @ManyToOne
    @JoinColumn(name = "payment_id", nullable = false)
    private Payment payment;

    @Column(name = "order_create_date_time", nullable = false)
    private LocalDateTime orderCreateDateTime;

    @Column(name = "total_amount", nullable = false)
    private int totalAmount;

    @PrePersist
    protected void onCreate() {
        this.orderCreateDateTime = LocalDateTime.now();
    }
}
