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
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public List<ProductDTO> getAllProducts() {
        log.info("Fetching all products...");
        List<ProductDTO> products = productRepository.findAll().stream()
                .map(ProductUtils::convertProductToProductDTO)
                .collect(Collectors.toList());
        log.info("Fetched {} products", products.size());
        return products;
    }

    public List<ProductDTO> getAllActiveProducts() {
        log.info("Fetching all active products...");
        List<ProductDTO> activeProducts = productRepository.findAll().stream()
                .filter(product -> !product.getIsDeleted())
                .map(ProductUtils::convertProductToProductDTO)
                .collect(Collectors.toList());
        log.info("Fetched {} active products", activeProducts.size());
        return activeProducts;
    }

    public ProductDTO getProductById(Long id) {
        log.info("Fetching product with id: {}", id);
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product with id: " + id + " not found"));
        if (product.getIsDeleted()) {
            throw new ProductIsDeletedException("Product is deleted");
        }
        log.info("Fetched product with id: {}", id);
        return ProductUtils.convertProductToProductDTO(product);
    }

    public List<OfferDTO> getAllOffersOfProduct(Long id) {
        log.info("Fetching offers for product with id: {}", id);
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product with id: " + id + " not found"));
        if (product.getIsDeleted()) {
            throw new ProductIsDeletedException("Product is deleted");
        }
        List<OfferDTO> offers = product.getOffers().stream()
                .map(CommonAllProductLinkedUtils::convertOfferToOfferDTO)
                .collect(Collectors.toList());
        log.info("Fetched {} offers for product with id: {}", offers.size(), id);
        return offers;
    }
}