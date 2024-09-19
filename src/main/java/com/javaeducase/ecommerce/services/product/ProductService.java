package com.javaeducase.ecommerce.services.product;

import com.javaeducase.ecommerce.dto.product.AttributeDTO;
import com.javaeducase.ecommerce.dto.product.CategoryDTO;
import com.javaeducase.ecommerce.dto.product.OfferDTO;
import com.javaeducase.ecommerce.dto.product.ProductDTO;
import com.javaeducase.ecommerce.entities.product.Attribute;
import com.javaeducase.ecommerce.entities.product.Category;
import com.javaeducase.ecommerce.entities.product.Offer;
import com.javaeducase.ecommerce.entities.product.Product;
import com.javaeducase.ecommerce.exceptions.product.ProductNotFoundException;
import com.javaeducase.ecommerce.exceptions.product.ProductIsDeletedException;
import com.javaeducase.ecommerce.repositories.product.CategoryRepository;
import com.javaeducase.ecommerce.repositories.product.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public List<ProductDTO> getAllProducts() {
        return productRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<ProductDTO> getAllActiveProducts() {
        return productRepository.findAll().stream()
                .filter(product -> !product.getIsDeleted())
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @PreAuthorize("hasRole('ADMIN')")
    public List<ProductDTO> getAllDeletedProducts() {
        return productRepository.findAll().stream()
                .filter(Product::getIsDeleted)
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public ProductDTO getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Товар с id: " + id + " не найден"));
        if (product.getIsDeleted()) {
            throw new ProductIsDeletedException("Товар был ранее удален");
        }
        return convertToDTO(product);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public ProductDTO createProduct(ProductDTO productDTO) {
        Category category = categoryRepository.findById(productDTO.getCategory().getId())
                .orElseThrow(() -> new IllegalArgumentException("Категория не найдена"));

        Product product = new Product();
        product.setName(productDTO.getName());
        product.setDescription(productDTO.getDescription());
        product.setCategory(category);
        product.setIsDeleted(false);
        Product savedProduct = productRepository.save(product);
        return convertToDTO(savedProduct);
    }

    @PreAuthorize("hasRole('ADMIN')")
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
        return convertToDTO(updatedProduct);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Товар с id: " + id + " не найден"));

        if (product.getIsDeleted()) {
            throw new ProductIsDeletedException("Товар был ранее удален");
        }
        product.setIsDeleted(true);
        if (product.getOffers() != null && !product.getOffers().isEmpty()) {
            for (Offer offer : product.getOffers()) {
                offer.setIsDeleted(true);
            }
        }
        productRepository.save(product);
    }

    public ProductDTO convertToDTO(Product product) {
        ProductDTO productDTO = new ProductDTO();
        productDTO.setId(product.getId());
        productDTO.setName(product.getName());
        productDTO.setDescription(product.getDescription());
        productDTO.setIsDeleted(product.getIsDeleted());

        CategoryDTO categoryDTO = categoryService.convertToDTO(product.getCategory());
        productDTO.setCategory(categoryDTO);

        List<OfferDTO> offerDTOs = product.getOffers().stream()
                .map(this::convertOfferToDTO)
                .collect(Collectors.toList());
        productDTO.setOffers(offerDTOs);

        return productDTO;
    }


    private OfferDTO convertOfferToDTO(Offer offer) {
        OfferDTO offerDTO = new OfferDTO();
        offerDTO.setId(offer.getId());
        offerDTO.setPrice(offer.getPrice());
        offerDTO.setStockQuantity(offer.getStockQuantity());
        offerDTO.setIsDeleted(offer.getIsDeleted());
        offerDTO.setAttributes(offer.getAttributes().stream()
                .map(this::convertAttributeToDTO)
                .collect(Collectors.toList()));
        return offerDTO;
    }

    private AttributeDTO convertAttributeToDTO(Attribute attribute) {
        AttributeDTO attributeDTO = new AttributeDTO();
        attributeDTO.setName(attribute.getName());
        attributeDTO.setValue(attribute.getValue());
        return attributeDTO;
    }
}
