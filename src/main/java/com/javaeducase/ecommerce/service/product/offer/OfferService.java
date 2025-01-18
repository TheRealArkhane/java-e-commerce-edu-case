package com.javaeducase.ecommerce.service.product.offer;

import com.javaeducase.ecommerce.dto.product.AttributeDTO;
import com.javaeducase.ecommerce.dto.product.OfferDTO;
import com.javaeducase.ecommerce.entity.product.Attribute;
import com.javaeducase.ecommerce.entity.product.Offer;
import com.javaeducase.ecommerce.exception.product.OfferNotFoundException;
import com.javaeducase.ecommerce.repository.product.OfferRepository;
import com.javaeducase.ecommerce.util.product.CommonAllProductLinkedUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class OfferService {

    private final OfferRepository offerRepository;

    public OfferDTO getOfferById(Long offerId) {
        log.info("Fetching offer with id: {}...", offerId);
        Offer offer = offerRepository.findById(offerId)
                .orElseThrow(() -> new OfferNotFoundException("Offer with id: " + offerId + " not found"));

        log.info("Offer with id: {} successfully fetched", offerId);
        return CommonAllProductLinkedUtils.convertOfferToOfferDTO(offer);
    }

    public List<AttributeDTO> getAttributesByOfferId(Long offerId) {
        log.info("Fetching attributes for offer with id: {}...", offerId);
        List<Attribute> attributes = offerRepository.findById(offerId)
                .orElseThrow(() -> new OfferNotFoundException("Offer with id: " + offerId + " not found"))
                .getAttributes();

        log.info("Found {} attributes for offer with id: {}", attributes.size(), offerId);
        List<AttributeDTO> attributesDTO = new ArrayList<>();
        for (Attribute attribute : attributes) {
            attributesDTO.add(CommonAllProductLinkedUtils.convertAttributeToAttributeDTO(attribute));
        }
        return attributesDTO;
    }
}

