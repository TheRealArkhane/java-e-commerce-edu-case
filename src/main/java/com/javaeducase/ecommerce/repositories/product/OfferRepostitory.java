package com.javaeducase.ecommerce.repositories.product;

import com.javaeducase.ecommerce.entities.product.Offer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OfferRepostitory extends JpaRepository<Offer, Long> {
}
