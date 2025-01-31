package com.javaeducase.ecommerce.repository.product;

import com.javaeducase.ecommerce.entity.product.Category;
import com.javaeducase.ecommerce.entity.product.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByCategoryId(Long categoryId);
}