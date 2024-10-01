package com.javaeducase.ecommerce.services.product.offer;

import com.javaeducase.ecommerce.dto.product.OfferDTO;
import com.javaeducase.ecommerce.entities.product.Offer;
import com.javaeducase.ecommerce.entities.product.Product;
import com.javaeducase.ecommerce.exceptions.product.OfferNotFoundException;
import com.javaeducase.ecommerce.exceptions.product.ProductNotFoundException;
import com.javaeducase.ecommerce.repositories.product.OfferRepository;
import com.javaeducase.ecommerce.repositories.product.ProductRepository;
import com.javaeducase.ecommerce.utils.product.CommonAllProductLinkedUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminOfferService {

    private final OfferRepository offerRepository;
    private final ProductRepository productRepository;
    private final CommonAllProductLinkedUtils commonAllProductLinkedUtils;

    @PreAuthorize("hasRole('ADMIN')")
    public OfferDTO addOfferToProduct(Long productId, OfferDTO offerDTO) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product with id: " + productId + " not found"));

        Offer offer = commonAllProductLinkedUtils.convertOfferDTOToOffer(offerDTO);
        offer.setProduct(product);
        Offer savedOffer = offerRepository.save(offer);
        return commonAllProductLinkedUtils.convertOfferToOfferDTO(savedOffer);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public OfferDTO updateOffer(Long offerId, OfferDTO offerDTO) {
        Offer offer = offerRepository.findById(offerId)
                .orElseThrow(() -> new OfferNotFoundException("Offer with id: " + offerId + " not found"));

        offer.setPrice(offerDTO.getPrice());
        offer.setStockQuantity(offerDTO.getStockQuantity());
        Offer updatedOffer = offerRepository.save(offer);
        return commonAllProductLinkedUtils.convertOfferToOfferDTO(updatedOffer);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public void deleteOffer(Long offerId) {
        Offer offer = offerRepository.findById(offerId)
                .orElseThrow(() -> new OfferNotFoundException("Offer with id: " + offerId + " not found"));

        offer.setIsDeleted(true);
        offerRepository.save(offer);
    }
}
