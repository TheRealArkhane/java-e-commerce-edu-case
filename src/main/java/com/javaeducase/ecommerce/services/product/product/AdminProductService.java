package com.javaeducase.ecommerce.services.product.product;

import com.javaeducase.ecommerce.dto.product.ProductDTO;
import com.javaeducase.ecommerce.entities.product.Category;
import com.javaeducase.ecommerce.entities.product.Offer;
import com.javaeducase.ecommerce.entities.product.Product;
import com.javaeducase.ecommerce.exceptions.product.OfferIsDeletedException;
import com.javaeducase.ecommerce.exceptions.product.OfferNotFoundException;
import com.javaeducase.ecommerce.exceptions.product.ProductIsDeletedException;
import com.javaeducase.ecommerce.exceptions.product.ProductNotFoundException;
import com.javaeducase.ecommerce.repositories.product.CategoryRepository;
import com.javaeducase.ecommerce.repositories.product.OfferRepository;
import com.javaeducase.ecommerce.repositories.product.ProductRepository;
import com.javaeducase.ecommerce.utils.product.CommonAllProductLinkedUtils;
import com.javaeducase.ecommerce.utils.product.ProductUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class AdminProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final OfferRepository offerRepository;
    private final ProductUtils productUtils;
    private final CommonAllProductLinkedUtils commonAllProductLinkedUtils;


    public ProductDTO createProduct(ProductDTO productDTO) {
        Category category = categoryRepository.findById(productDTO.getCategory().getId())
                .orElseThrow(() -> new IllegalArgumentException("Категория не найдена"));

        Product product = new Product();
        product.setName(productDTO.getName());
        product.setDescription(productDTO.getDescription());
        product.setCategory(category);
        product.setIsDeleted(false);
        product.setOffers(new ArrayList<>());   // <--- т.к. офферы связаны с конкретным продуктом как @ManyToOne
                                                //      и должны быть заведены отдельным методом
        Product savedProduct = productRepository.save(product);
        return productUtils.convertProductToProductDTO(savedProduct);
    }

    public ProductDTO updateProduct(Long id, ProductDTO productDTO) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Товар с id: " + id + " не найден"));

        if (product.getIsDeleted()) {
            throw new ProductIsDeletedException("Товар был ранее удален");
        }

        Category category = categoryRepository.findById(productDTO.getCategory().getId())
                .orElseThrow(() -> new IllegalArgumentException("Категория не найдена"));

        product.setName(productDTO.getName());
        product.setDescription(productDTO.getDescription());
        product.setCategory(category);
        Product updatedProduct = productRepository.save(product);
        return productUtils.convertProductToProductDTO(updatedProduct);
    }

    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Товар с id: " + id + " не найден"));

        if (product.getIsDeleted()) {
            throw new ProductIsDeletedException("Товар был ранее удален");
        }

        product.setIsDeleted(true);
        if (product.getOffers() != null && !product.getOffers().isEmpty()) {
            product.getOffers().forEach(offer -> offer.setIsDeleted(true));
        }
        productRepository.save(product);
    }

//    public ProductDTO addOfferToProduct(Long productId, Long offerId) { //TODO: Replace with OfferDTO
//        Product product = productRepository.findById(productId)
//                .orElseThrow(() -> new ProductNotFoundException("Товар с id: " + productId + " не найден"));
//
//        if (product.getIsDeleted()) {
//            throw new ProductIsDeletedException("Товар был ранее удален");
//        }
//
//        Offer offer = offerRepository.findById(offerId)
//                .orElseThrow(() -> new OfferNotFoundException("Предложение с id: " + offerId + " не найдено"));
//
//        if (offer.getIsDeleted()) {
//            throw new OfferIsDeletedException("Предложение с id: " + offer.getId() + " было ранее удалено");
//        }
//        offer.setProduct(product);
//        offerRepository.save(offer);
//        return productUtils.convertProductToProductDTO(productRepository.save(product));
//    }
}