package com.javaeducase.ecommerce.entities.user;

import com.javaeducase.ecommerce.entities.cart.Cart;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@EqualsAndHashCode
@ToString
@Table(name = "app_users")
public class User {
    @Id
    @Column(name = "id", unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "password", nullable = false)
    @ToString.Exclude
    private String password;

    @Column(name = "role", nullable = false)
    private Role role;

    @Column(name = "is_deleted", nullable = false)
    private boolean isDeleted;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "cart_id", referencedColumnName = "id")
    private Cart cart;

    public User() {
        this.cart = new Cart(); // инициализация пустой корзины при создании пользователя
    }
}
