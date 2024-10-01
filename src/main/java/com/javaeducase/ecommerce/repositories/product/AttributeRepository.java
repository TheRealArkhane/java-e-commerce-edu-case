package com.javaeducase.ecommerce.repositories.product;

import com.javaeducase.ecommerce.entities.product.Attribute;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AttributeRepository extends JpaRepository<Attribute, Long> {
    List<Attribute> findByOfferId(Long offerId);
    boolean existsByNameAndValue(String name, String value);
}
