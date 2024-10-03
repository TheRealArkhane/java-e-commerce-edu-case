package com.javaeducase.ecommerce.repositories.cart;

import com.javaeducase.ecommerce.entities.cart.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Integer> {
    Optional<Cart> findByUserId(Long user_id);
}
