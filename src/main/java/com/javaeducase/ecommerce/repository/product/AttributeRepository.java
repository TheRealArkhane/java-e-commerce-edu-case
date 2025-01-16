package com.javaeducase.ecommerce.repository.product;

import com.javaeducase.ecommerce.entity.product.Attribute;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AttributeRepository extends JpaRepository<Attribute, Long> {
    boolean existsByNameAndValue(String name, String value);
}
