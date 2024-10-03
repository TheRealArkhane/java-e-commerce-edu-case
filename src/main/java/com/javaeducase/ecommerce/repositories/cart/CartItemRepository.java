package com.javaeducase.ecommerce.repositories.cart;

import com.javaeducase.ecommerce.entities.cart.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {

}
