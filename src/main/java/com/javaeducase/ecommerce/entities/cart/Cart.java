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
    @Column(name = "id")
    private Long id;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CartItem> items = new ArrayList<>(); ;

    @Column(name = "total_amount")
    private int totalAmount;

    @Column(name = "total_quantity")
    private int totalQuantity;

    @OneToOne(mappedBy = "cart", cascade = CascadeType.ALL)
    private User user;


    public Integer getTotalAmount() {
        return items.stream().mapToInt(CartItem::getTotalPrice).sum();
    }

    public Integer getTotalQuantity() {
        return items.stream().mapToInt(CartItem::getQuantity).sum();
    }

    public void addItem(CartItem item) {
        items.add(item);
    }

    public void removeItem(CartItem item) {
        items.remove(item);
    }
}
