package com.javaeducase.ecommerce.entities.order;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "deliveries")
public class Delivery {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "delivery_price", nullable = false)
    private int deliveryPrice;

    @OneToMany(mappedBy = "delivery", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Payment> payments;

    @OneToMany(mappedBy = "delivery", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<PickupLocation> pickupLocations;
}