package com.javaeducase.ecommerce.entities.order;

import com.javaeducase.ecommerce.entities.cart.Cart;
import com.javaeducase.ecommerce.entities.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@ToString
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    User user;

    @ManyToOne
    @JoinColumn(name = "cart_id", nullable = false)
    private Cart cart;

    @Column(name = "address")
    String address;

    @Enumerated(EnumType.STRING)
    @Column(name = "delivery_method", nullable = false)
    DeliveryMethod deliveryMethod;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method", nullable = false)
    private PaymentMethod paymentMethod;

    @Column(name = "delivery_price")
    int deliveryPrice;

    @Column(name = "order_create_date_time", nullable = false)
    LocalDateTime orderCreateDateTime;

    @Column(name = "final_price")
    int finalPrice = 0;

    @PrePersist
    protected void onCreate() {
        orderCreateDateTime = LocalDateTime.now();
        deliveryPrice = deliveryMethod != null ? deliveryMethod.getPrice() : 0;
    }
}
