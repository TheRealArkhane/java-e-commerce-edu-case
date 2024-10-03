package com.javaeducase.ecommerce.services.product.offer;

import com.javaeducase.ecommerce.dto.product.OfferDTO;
import com.javaeducase.ecommerce.entities.product.Attribute;
import com.javaeducase.ecommerce.entities.product.Offer;
import com.javaeducase.ecommerce.entities.product.Product;
import com.javaeducase.ecommerce.exceptions.product.AttributeNotFoundException;
import com.javaeducase.ecommerce.exceptions.product.OfferIsDeletedException;
import com.javaeducase.ecommerce.exceptions.product.OfferNotFoundException;
import com.javaeducase.ecommerce.exceptions.product.ProductNotFoundException;
import com.javaeducase.ecommerce.repositories.product.AttributeRepository;
import com.javaeducase.ecommerce.repositories.product.OfferRepository;
import com.javaeducase.ecommerce.repositories.product.ProductRepository;
import com.javaeducase.ecommerce.utils.product.CommonAllProductLinkedUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class AdminOfferService {

    private final OfferRepository offerRepository;
    private final ProductRepository productRepository;
    private final AttributeRepository attributeRepository;
    private final CommonAllProductLinkedUtils commonAllProductLinkedUtils;

    @PreAuthorize("hasRole('ADMIN')")
    public OfferDTO createProductOffer(Long productId, OfferDTO offerDTO) {
        Offer newOffer = new Offer();
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Товар с id: " + productId + " не найден"));
        newOffer.setProduct(product);
        if (offerDTO.getPrice() < 0) {
            throw new IllegalArgumentException("Цена не может быть отрицательной");
        }
        newOffer.setPrice(offerDTO.getPrice());

        if (offerDTO.getStockQuantity() < 0) {
            throw new IllegalArgumentException("Значение количества на складе не может быть отрицательным");
        } else if (offerDTO.getStockQuantity() > 0) {
            newOffer.setStockQuantity(offerDTO.getStockQuantity());
            newOffer.setIsAvailable(true);
        } else {
            newOffer.setIsAvailable(false);
            newOffer.setStockQuantity(offerDTO.getStockQuantity());
        }
        if (offerDTO.getAttributes() != null) {
            newOffer.setAttributes(offerDTO.getAttributes()
                    .stream()
                    .map(commonAllProductLinkedUtils::convertAttributeDTOToAttribute)
                    .toList());
        }
        else newOffer.setAttributes(new ArrayList<>());
        newOffer.setIsDeleted(false);
        offerRepository.save(newOffer);
        return commonAllProductLinkedUtils.convertOfferToOfferDTO(newOffer);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public OfferDTO updateOffer(Long offerId, OfferDTO offerDTO) {
        Offer updatingOffer = getOfferByIdCheckIsDeleted(offerId);

        if (offerDTO.getPrice() != null) {
            if (offerDTO.getPrice() < 0) {
                throw new IllegalArgumentException("Цена не может быть отрицательной");
            }
            updatingOffer.setPrice(offerDTO.getPrice());
        }

        if (offerDTO.getStockQuantity() != null) {
            if (offerDTO.getStockQuantity() < 0) {
                throw new IllegalArgumentException("Значение количества на складе не может быть отрицательным");
            }
            if (!updatingOffer.getIsAvailable() && offerDTO.getStockQuantity() > 0) {
                updatingOffer.setIsAvailable(true);
            } else if (updatingOffer.getIsAvailable() && offerDTO.getStockQuantity() == 0) {
                updatingOffer.setIsAvailable(false);
            }
            updatingOffer.setStockQuantity(offerDTO.getStockQuantity());
        }

        offerRepository.save(updatingOffer);
        return commonAllProductLinkedUtils.convertOfferToOfferDTO(updatingOffer);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public OfferDTO addAttributeToOffer(Long offerId, Long attributeId) {
        Offer offer = getOfferByIdCheckIsDeleted(offerId);
        Attribute attribute = attributeRepository.findById(attributeId)
                .orElseThrow(() -> new AttributeNotFoundException("Атрибут с id: " + attributeId + " не найден"));

        offer.getAttributes().add(attribute);
        Offer updatedOffer = offerRepository.save(offer);

        return commonAllProductLinkedUtils.convertOfferToOfferDTO(updatedOffer);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public OfferDTO deleteAttributeFromOffer(Long offerId, Long attributeId) {
        Offer offer = getOfferByIdCheckIsDeleted(offerId);
        Attribute attribute = attributeRepository.findById(attributeId)
                .orElseThrow(() -> new AttributeNotFoundException("Атрибут с id: " + attributeId + " не найден"));

        if (offer.getAttributes().remove(attribute)) {
            offerRepository.save(offer);
            return commonAllProductLinkedUtils.convertOfferToOfferDTO(offer);
        }
        else throw new AttributeNotFoundException("Атрибут с id: " + attributeId + " не найден в предложении с id: " + offerId);
    }

//    @PreAuthorize("hasRole('ADMIN')")
//    public OfferDTO removeAttributeFromOffer(Long offerId, Long attributeId) {
//        Offer offer = getOfferByIdCheckIsDeleted(offerId);
//        Attribute attribute = attributeRepository.findById(attributeId)
//                .orElseThrow(() -> new AttributeNotFoundException("Атрибут с id: " + attributeId + " не найден"));
//
//        if (!offer.getAttributes().remove(attribute)) {
//            throw new AttributeNotFoundException("Атрибут с id: " + attributeId + " не найден у оффера с id: " + offerId);
//        }
//
//        Offer updatedOffer = offerRepository.save(offer);
//        return commonAllProductLinkedUtils.convertOfferToOfferDTO(updatedOffer);
//    }

    @PreAuthorize("hasRole('ADMIN')")
    public void deleteOffer(Long offerId) {
        Offer offer = getOfferByIdCheckIsDeleted(offerId);
        offer.setStockQuantity(0);
        offer.setIsAvailable(false);
        offer.setIsDeleted(true);
        offerRepository.save(offer);
    }

    private Offer getOfferByIdCheckIsDeleted(Long offerId) {
        Offer offer = offerRepository.findById(offerId)
                .orElseThrow(() -> new OfferNotFoundException("Offer with id: " + offerId + " not found"));

        if (offer.getIsDeleted()) {
            throw new OfferIsDeletedException("Предложение с id: " + offer.getId() + " было удалено");
        }
        return offer;
    }
}
