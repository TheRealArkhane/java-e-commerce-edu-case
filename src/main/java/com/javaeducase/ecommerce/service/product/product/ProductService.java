package com.javaeducase.ecommerce.service.product.product;

import com.javaeducase.ecommerce.dto.product.OfferDTO;
import com.javaeducase.ecommerce.dto.product.ProductDTO;
import com.javaeducase.ecommerce.entity.product.Product;
import com.javaeducase.ecommerce.exception.product.ProductIsDeletedException;
import com.javaeducase.ecommerce.exception.product.ProductNotFoundException;
import com.javaeducase.ecommerce.repository.product.ProductRepository;
import com.javaeducase.ecommerce.util.product.CommonAllProductLinkedUtils;
import com.javaeducase.ecommerce.util.product.ProductUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;


    public List<ProductDTO> getAllProducts() {
        return productRepository.findAll().stream()
                .map(ProductUtils::convertProductToProductDTO)
                .collect(Collectors.toList());
    }

    public List<ProductDTO> getAllActiveProducts() {
        return productRepository.findAll().stream()
                .filter(product -> !product.getIsDeleted())
                .map(ProductUtils::convertProductToProductDTO)
                .collect(Collectors.toList());
    }

    public ProductDTO getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Товар с id: " + id + " не найден"));
        if (product.getIsDeleted()) {
            throw new ProductIsDeletedException("Товар был ранее удален");
        }
        return ProductUtils.convertProductToProductDTO(product);
    }

    public List<OfferDTO> getAllOffersOfProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Товар с id: " + id + " не найден"));
        if (product.getIsDeleted()) {
            throw new ProductIsDeletedException("Товар был ранее удален");
        }
        return product.getOffers().stream()
                .map(CommonAllProductLinkedUtils::convertOfferToOfferDTO)
                .collect(Collectors.toList());
    }
}