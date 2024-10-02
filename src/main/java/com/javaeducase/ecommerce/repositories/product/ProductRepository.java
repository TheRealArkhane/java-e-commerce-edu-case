package com.javaeducase.ecommerce.repositories.product;

import com.javaeducase.ecommerce.entities.product.Category;
import com.javaeducase.ecommerce.entities.product.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByCategory(Category category);
    List<Product> findByCategoryIn(List<Category> categories);
    List<Product> findByCategoryId(Long categoryId);
}