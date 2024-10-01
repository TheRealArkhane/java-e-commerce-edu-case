package com.javaeducase.ecommerce.services.product.product;

import com.javaeducase.ecommerce.dto.product.OfferDTO;
import com.javaeducase.ecommerce.dto.product.ProductDTO;
import com.javaeducase.ecommerce.entities.product.Offer;
import com.javaeducase.ecommerce.entities.product.Product;
import com.javaeducase.ecommerce.exceptions.product.ProductNotFoundException;
import com.javaeducase.ecommerce.exceptions.product.ProductIsDeletedException;
import com.javaeducase.ecommerce.repositories.product.ProductRepository;
import com.javaeducase.ecommerce.utils.ProductUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductUtils productUtils;

    public List<ProductDTO> getAllProducts() {
        return productRepository.findAll().stream()
                .map(productUtils::convertProductToProductDTO)
                .collect(Collectors.toList());
    }

    public List<ProductDTO> getAllActiveProducts() {
        return productRepository.findAll().stream()
                .filter(product -> !product.getIsDeleted())
                .map(productUtils::convertProductToProductDTO)
                .collect(Collectors.toList());
    }

    public ProductDTO getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Товар с id: " + id + " не найден"));
        if (product.getIsDeleted()) {
            throw new ProductIsDeletedException("Товар был ранее удален");
        }
        return productUtils.convertProductToProductDTO(product);
    }

    public ProductDTO getProductWithAvailableOffers(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Товар с id: " + productId + " не найден"));

        if (product.getOffers() == null || product.getOffers().isEmpty()) {
            ProductDTO productDTO = productUtils.convertProductToProductDTO(product);
            productDTO.setOffers(Collections.emptyList());
            return productDTO;
        }

        List<Offer> availableOffers = product.getOffers().stream()
                .filter(offer -> offer.getStockQuantity() != 0)
                .toList();

        ProductDTO productDTO = productUtils.convertProductToProductDTO(product);

        if (availableOffers.isEmpty()) {
            productDTO.setOffers(Collections.emptyList());
        } else {
            List<OfferDTO> offerDTOs = availableOffers.stream()
                    .map(productUtils::convertOfferToOfferDTO)
                    .collect(Collectors.toList());
            productDTO.setOffers(offerDTOs);
        }
        return productDTO;
    }
}