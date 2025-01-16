package com.javaeducase.ecommerce.service.product.product;

import com.javaeducase.ecommerce.dto.product.ProductDTO;
import com.javaeducase.ecommerce.entity.product.Category;
import com.javaeducase.ecommerce.entity.product.Product;
import com.javaeducase.ecommerce.exception.product.ProductIsDeletedException;
import com.javaeducase.ecommerce.exception.product.ProductNotFoundException;
import com.javaeducase.ecommerce.repository.product.CategoryRepository;
import com.javaeducase.ecommerce.repository.product.OfferRepository;
import com.javaeducase.ecommerce.repository.product.ProductRepository;
import com.javaeducase.ecommerce.util.product.CommonAllProductLinkedUtils;
import com.javaeducase.ecommerce.util.product.ProductUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class AdminProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
//    private final OfferRepository offerRepository;


    public ProductDTO createProduct(ProductDTO productDTO) {
        Category category = categoryRepository.findById(productDTO.getCategory().getId())
                .orElseThrow(() -> new IllegalArgumentException("��������� �� �������"));

        Product product = new Product();
        product.setName(productDTO.getName());
        product.setDescription(productDTO.getDescription());
        product.setCategory(category);
        product.setIsDeleted(false);
        product.setOffers(new ArrayList<>());   // <--- �.�. ������ ������� � ���������� ��������� ��� @ManyToOne
                                                //      � ������ ���� �������� ��������� �������
        Product savedProduct = productRepository.save(product);
        return ProductUtils.convertProductToProductDTO(savedProduct);
    }

    public ProductDTO updateProduct(Long id, ProductDTO productDTO) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("����� � id: " + id + " �� ������"));

        if (product.getIsDeleted()) {
            throw new ProductIsDeletedException("����� ��� ����� ������");
        }

        Category category = categoryRepository.findById(productDTO.getCategory().getId())
                .orElseThrow(() -> new IllegalArgumentException("��������� �� �������"));

        product.setName(productDTO.getName());
        product.setDescription(productDTO.getDescription());
        product.setCategory(category);
        Product updatedProduct = productRepository.save(product);
        return ProductUtils.convertProductToProductDTO(updatedProduct);
    }

    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("����� � id: " + id + " �� ������"));

        if (product.getIsDeleted()) {
            throw new ProductIsDeletedException("����� ��� ����� ������");
        }

        product.setIsDeleted(true);
        if (product.getOffers() != null && !product.getOffers().isEmpty()) {
            product.getOffers().forEach(offer -> offer.setIsDeleted(true));
        }
        productRepository.save(product);
    }

//    public ProductDTO addOfferToProduct(Long productId, Long offerId) { //TODO: Replace with OfferDTO
//        Product product = productRepository.findById(productId)
//                .orElseThrow(() -> new ProductNotFoundException("����� � id: " + productId + " �� ������"));
//
//        if (product.getIsDeleted()) {
//            throw new ProductIsDeletedException("����� ��� ����� ������");
//        }
//
//        Offer offer = offerRepository.findById(offerId)
//                .orElseThrow(() -> new OfferNotFoundException("����������� � id: " + offerId + " �� �������"));
//
//        if (offer.getIsDeleted()) {
//            throw new OfferIsDeletedException("����������� � id: " + offer.getId() + " ���� ����� �������");
//        }
//        offer.setProduct(product);
//        offerRepository.save(offer);
//        return productUtils.convertProductToProductDTO(productRepository.save(product));
//    }
}