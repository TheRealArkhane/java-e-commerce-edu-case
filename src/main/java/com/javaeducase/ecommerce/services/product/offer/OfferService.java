package com.javaeducase.ecommerce.services.product.offer;

import com.javaeducase.ecommerce.dto.product.AttributeDTO;
import com.javaeducase.ecommerce.dto.product.OfferDTO;
import com.javaeducase.ecommerce.entities.product.Attribute;
import com.javaeducase.ecommerce.entities.product.Offer;
import com.javaeducase.ecommerce.exceptions.product.OfferNotFoundException;
import com.javaeducase.ecommerce.repositories.product.OfferRepository;
import com.javaeducase.ecommerce.utils.product.CommonAllProductLinkedUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OfferService {

    private final OfferRepository offerRepository;
    private final CommonAllProductLinkedUtils commonAllProductLinkedUtils;

    public OfferDTO getOfferById(Long offerId) {
        Offer offer = offerRepository.findById(offerId)
                .orElseThrow(() -> new OfferNotFoundException("Offer with id: " + offerId + " not found"));

        return commonAllProductLinkedUtils.convertOfferToOfferDTO(offer);
    }

    public List<AttributeDTO> getAttributesByOfferId(Long offerId) {
        List<Attribute> attributes = offerRepository.findById(offerId)
                .orElseThrow(() -> new OfferNotFoundException("Оффер с id: " + offerId + " не найден")).getAttributes();
        List<AttributeDTO> attributesDTO = new ArrayList<>();
        for (Attribute attribute : attributes) {
            attributesDTO.add(commonAllProductLinkedUtils.convertAttributeToAttributeDTO(attribute));
        }
        return attributesDTO;
    }
}
