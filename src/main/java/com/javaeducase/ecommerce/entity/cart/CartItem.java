package com.javaeducase.ecommerce.entity.cart;

import com.javaeducase.ecommerce.entity.product.Offer;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString
@Table(name = "cart_items")
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "offer_id", nullable = false)
    private Offer offer;

    @Column(nullable = false)
    private int quantity;

    public Integer getTotalPrice() {
        return offer.getPrice() * quantity;
    }
}
