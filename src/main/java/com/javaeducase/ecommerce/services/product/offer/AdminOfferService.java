package com.javaeducase.ecommerce.services.product.offer;

import com.javaeducase.ecommerce.dto.product.OfferDTO;
import com.javaeducase.ecommerce.entities.product.Attribute;
import com.javaeducase.ecommerce.entities.product.Offer;
import com.javaeducase.ecommerce.entities.product.Product;
import com.javaeducase.ecommerce.exceptions.product.AttributeNotFoundException;
import com.javaeducase.ecommerce.exceptions.product.OfferNotFoundException;
import com.javaeducase.ecommerce.exceptions.product.ProductNotFoundException;
import com.javaeducase.ecommerce.repositories.product.AttributeRepository;
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
    private final AttributeRepository attributeRepository;
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
    public OfferDTO addAttributeToOffer(Long offerId, Long attributeId) {
        Offer offer = offerRepository.findById(offerId)
                .orElseThrow(() -> new OfferNotFoundException("Оффер с id: " + offerId + " не найден"));

        Attribute attribute = attributeRepository.findById(attributeId)
                .orElseThrow(() -> new AttributeNotFoundException("Атрибут с id: " + attributeId + " не найден"));

        offer.getAttributes().add(attribute);
        Offer updatedOffer = offerRepository.save(offer);

        return commonAllProductLinkedUtils.convertOfferToOfferDTO(updatedOffer);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public OfferDTO removeAttributeFromOffer(Long offerId, Long attributeId) {
        Offer offer = offerRepository.findById(offerId)
                .orElseThrow(() -> new OfferNotFoundException("Оффер с id: " + offerId + " не найден"));

        Attribute attribute = attributeRepository.findById(attributeId)
                .orElseThrow(() -> new AttributeNotFoundException("Атрибут с id: " + attributeId + " не найден"));

        if (!offer.getAttributes().remove(attribute)) {
            throw new AttributeNotFoundException("Атрибут с id: " + attributeId + " не найден у оффера с id: " + offerId);
        }

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
