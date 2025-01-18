package com.javaeducase.ecommerce.service.product.offer;

import com.javaeducase.ecommerce.dto.product.AttributeDTO;
import com.javaeducase.ecommerce.dto.product.OfferDTO;
import com.javaeducase.ecommerce.entity.product.Attribute;
import com.javaeducase.ecommerce.entity.product.Offer;
import com.javaeducase.ecommerce.exception.product.OfferNotFoundException;
import com.javaeducase.ecommerce.repository.product.OfferRepository;
import com.javaeducase.ecommerce.util.product.CommonAllProductLinkedUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OfferService {

    private final OfferRepository offerRepository;

    public OfferDTO getOfferById(Long offerId) {
        Offer offer = offerRepository.findById(offerId)
                .orElseThrow(() -> new OfferNotFoundException("Offer with id: " + offerId + " not found"));

        return CommonAllProductLinkedUtils.convertOfferToOfferDTO(offer);
    }

    public List<AttributeDTO> getAttributesByOfferId(Long offerId) {
        List<Attribute> attributes = offerRepository.findById(offerId)
                .orElseThrow(() -> new OfferNotFoundException("Offer with id: " + offerId + " not found"))
                .getAttributes();
        List<AttributeDTO> attributesDTO = new ArrayList<>();
        for (Attribute attribute : attributes) {
            attributesDTO.add(CommonAllProductLinkedUtils.convertAttributeToAttributeDTO(attribute));
        }
        return attributesDTO;
    }
}
