package com.javaeducase.ecommerce.service.product.offer;

import com.javaeducase.ecommerce.dto.product.AttributeRequestDTO;
import com.javaeducase.ecommerce.dto.product.CreateOfferRequestDTO;
import com.javaeducase.ecommerce.dto.product.OfferDTO;
import com.javaeducase.ecommerce.entity.product.Attribute;
import com.javaeducase.ecommerce.entity.product.Offer;
import com.javaeducase.ecommerce.entity.product.Product;
import com.javaeducase.ecommerce.exception.product.AttributeNotFoundException;
import com.javaeducase.ecommerce.exception.product.OfferIsDeletedException;
import com.javaeducase.ecommerce.exception.product.OfferNotFoundException;
import com.javaeducase.ecommerce.exception.product.ProductNotFoundException;
import com.javaeducase.ecommerce.repository.product.AttributeRepository;
import com.javaeducase.ecommerce.repository.product.OfferRepository;
import com.javaeducase.ecommerce.repository.product.ProductRepository;
import com.javaeducase.ecommerce.util.product.CommonAllProductLinkedUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminOfferService {

    private final OfferRepository offerRepository;
    private final ProductRepository productRepository;
    private final AttributeRepository attributeRepository;

    public OfferDTO createProductOffer(CreateOfferRequestDTO createOfferRequestDTO) {
        log.info("Creating new offer for product with id: {}...", createOfferRequestDTO.getProductId());
        Offer newOffer = new Offer();
        Long productId = createOfferRequestDTO.getProductId();
        int price = createOfferRequestDTO.getPrice();
        int stockQuantity = createOfferRequestDTO.getStockQuantity();
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Offer with id: " + productId + " not found"));
        newOffer.setProduct(product);

        if (price < 0) {
            throw new IllegalArgumentException("Price cannot be negative");
        }
        newOffer.setPrice(price);

        if (stockQuantity < 0) {
            throw new IllegalArgumentException("Stock quantity cannot be negative");
        } else if (stockQuantity > 0) {
            newOffer.setStockQuantity(stockQuantity);
            newOffer.setIsAvailable(true);
        } else {
            newOffer.setIsAvailable(false);
            newOffer.setStockQuantity(0);
        }

        newOffer.setAttributes(new ArrayList<>());
        newOffer.setIsDeleted(false);
        offerRepository.save(newOffer);

        log.info("Offer created successfully for product with id: {}", productId);
        return CommonAllProductLinkedUtils.convertOfferToOfferDTO(newOffer);
    }

    public OfferDTO updateOffer(Long offerId, OfferDTO offerDTO) {
        log.info("Updating offer with id: {}...", offerId);
        Offer updatingOffer = getOfferByIdCheckIsDeleted(offerId);

        if (offerDTO.getPrice() != null) {
            if (offerDTO.getPrice() < 0) {
                throw new IllegalArgumentException("Price cannot be negative");
            }
            updatingOffer.setPrice(offerDTO.getPrice());
        }

        if (offerDTO.getStockQuantity() != null) {
            if (offerDTO.getStockQuantity() < 0) {
                throw new IllegalArgumentException("Stock quantity cannot be negative");
            }
            if (!updatingOffer.getIsAvailable() && offerDTO.getStockQuantity() > 0) {
                updatingOffer.setIsAvailable(true);
            } else if (updatingOffer.getIsAvailable() && offerDTO.getStockQuantity() == 0) {
                updatingOffer.setIsAvailable(false);
            }
            updatingOffer.setStockQuantity(offerDTO.getStockQuantity());
        }

        offerRepository.save(updatingOffer);
        log.info("Offer with id: {} updated successfully", offerId);
        return CommonAllProductLinkedUtils.convertOfferToOfferDTO(updatingOffer);
    }

    public OfferDTO addAttributeToOffer(AttributeRequestDTO attributeRequestDTO) {
        log.info("Adding attribute to offer with id: {}...", attributeRequestDTO.getOfferId());
        Long offerId = attributeRequestDTO.getOfferId();
        Long attributeId = attributeRequestDTO.getAttributeId();

        Offer offer = getOfferByIdCheckIsDeleted(offerId);
        Attribute attribute = attributeRepository.findById(attributeId)
                .orElseThrow(() -> new AttributeNotFoundException("Attribute with id: " + attributeId + " not found"));

        offer.getAttributes().add(attribute);
        Offer updatedOffer = offerRepository.save(offer);

        log.info("Attribute with id: {} added to offer with id: {}", attributeId, offerId);
        return CommonAllProductLinkedUtils.convertOfferToOfferDTO(updatedOffer);
    }

    public OfferDTO deleteAttributeFromOffer(AttributeRequestDTO attributeRequestDTO) {
        log.info("Deleting attribute from offer with id: {}...", attributeRequestDTO.getOfferId());
        Long offerId = attributeRequestDTO.getOfferId();
        Long attributeId = attributeRequestDTO.getAttributeId();

        Offer offer = getOfferByIdCheckIsDeleted(offerId);
        Attribute attribute = attributeRepository.findById(attributeId)
                .orElseThrow(() -> new AttributeNotFoundException("Attribute with id: " + attributeId + " not found"));

        if (offer.getAttributes().remove(attribute)) {
            offerRepository.save(offer);
            log.info("Attribute with id: {} removed from offer with id: {}", attributeId, offerId);
            return CommonAllProductLinkedUtils.convertOfferToOfferDTO(offer);
        } else {
            throw new AttributeNotFoundException("Attribute with id: "
                    + attributeId
                    + " does not exist in offer with id: "
                    + offerId);
        }
    }

    public void deleteOffer(Long offerId) {
        log.info("Deleting offer with id: {}...", offerId);
        Offer offer = getOfferByIdCheckIsDeleted(offerId);
        offer.setStockQuantity(0);
        offer.setIsAvailable(false);
        offer.setIsDeleted(true);
        offerRepository.save(offer);
        log.info("Offer with id: {} deleted successfully", offerId);
    }

    private Offer getOfferByIdCheckIsDeleted(Long offerId) {
        log.info("Fetching offer with id: {}...", offerId);
        Offer offer = offerRepository.findById(offerId)
                .orElseThrow(() -> new OfferNotFoundException("Offer with id: " + offerId + " not found"));

        if (offer.getIsDeleted()) {
            throw new OfferIsDeletedException("Offer with id: " + offer.getId() + " was deleted");
        }
        return offer;
    }
}

