package com.javaeducase.ecommerce.repository.product;

import com.javaeducase.ecommerce.entity.product.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
//    Optional<Category> findByName(String name);
}
