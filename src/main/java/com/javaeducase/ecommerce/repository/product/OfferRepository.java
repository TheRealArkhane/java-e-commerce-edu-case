package com.javaeducase.ecommerce.repository.product;

import com.javaeducase.ecommerce.entity.product.Offer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OfferRepository extends JpaRepository<Offer, Long> {
}
