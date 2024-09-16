package com.javaeducase.ecommerce.entities.cart;

import com.javaeducase.ecommerce.entities.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@ToString
@Table(name = "carts")
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CartItem> items = new ArrayList<>();

    private int totalPrice = 0;

    private int totalQuantity = 0;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
