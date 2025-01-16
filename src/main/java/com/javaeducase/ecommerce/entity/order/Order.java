package com.javaeducase.ecommerce.entity.order;

import com.javaeducase.ecommerce.entity.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderDetail> orderDetails = new ArrayList<>();

    @Column(name = "address", nullable = false)
    private String address;

    @Column(name = "pickup_location")
    private String pickupLocation;

    @ManyToOne
    @JoinColumn(name = "delivery_id", nullable = false)
    private Delivery delivery;

    @ManyToOne
    @JoinColumn(name = "payment_id", nullable = false)
    private Payment payment;

    @Column(name = "order_create_date_time", nullable = false)
    private LocalDateTime orderCreateDateTime;

    @Column(name = "total_quantity")
    private int totalQuantity;

    @Column(name = "total_amount")
    private int totalAmount;

    @PrePersist
    protected void onCreate() {
        this.orderCreateDateTime = LocalDateTime.now();
    }
}
