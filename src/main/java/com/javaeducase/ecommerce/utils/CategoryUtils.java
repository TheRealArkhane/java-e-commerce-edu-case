package com.javaeducase.ecommerce.utils;

import com.javaeducase.ecommerce.dto.product.AttributeDTO;
import com.javaeducase.ecommerce.dto.product.CategoryDTO;
import com.javaeducase.ecommerce.dto.product.OfferDTO;
import com.javaeducase.ecommerce.dto.product.ProductDTO;
import com.javaeducase.ecommerce.entities.product.Attribute;
import com.javaeducase.ecommerce.entities.product.Category;
import com.javaeducase.ecommerce.entities.product.Offer;
import com.javaeducase.ecommerce.entities.product.Product;
import com.javaeducase.ecommerce.exceptions.product.CategoryNotFoundException;
import com.javaeducase.ecommerce.repositories.product.CategoryRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CategoryUtils {

    private final CategoryRepository categoryRepository;

    public CategoryUtils(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public Category findCategoryById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException("Категория не найдена"));
    }

    public void deleteCategoryAndDescendants(Category category) {
        List<Category> descendants = categoryRepository.findAllByParent(category);
        for (Category descendant : descendants) {
            deleteCategoryAndDescendants(descendant);
        }
        categoryRepository.delete(category);
    }

    public void markProductAndOffersAsDeleted(Product product) {
        product.setIsDeleted(true);
        for (Offer offer : product.getOffers()) {
            offer.setIsDeleted(true);
        }
    }

    public CategoryDTO convertCategoryToCategoryDTO(Category category) {
        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setId(category.getId());
        categoryDTO.setName(category.getName());

        if (category.getParent() != null) {
            categoryDTO.setParent(convertCategoryToCategoryDTO(category.getParent()));
        }

        List<CategoryDTO> childrenDTOs = category.getChildren().stream()
                .map(this::convertCategoryToCategoryDTO)
                .collect(Collectors.toList());
        categoryDTO.setChildren(childrenDTOs);

        return categoryDTO;
    }

    public CategoryDTO convertToCategoryDTOWithDescendants(Category category) {
        CategoryDTO categoryDTO = convertCategoryToCategoryDTO(category);
        List<CategoryDTO> childrenWithDescendants = category.getChildren().stream()
                .map(this::convertToCategoryDTOWithDescendants)
                .collect(Collectors.toList());
        categoryDTO.setChildren(childrenWithDescendants);
        return categoryDTO;
    }

    public ProductDTO convertProductToProductDTO(Product product) {
        ProductDTO productDTO = new ProductDTO();
        productDTO.setId(product.getId());
        productDTO.setName(product.getName());
        productDTO.setDescription(product.getDescription());
        productDTO.setIsDeleted(product.getIsDeleted());

        CategoryDTO categoryDTO = convertCategoryToCategoryDTO(product.getCategory());
        productDTO.setCategory(categoryDTO);

        List<OfferDTO> offerDTOs = product.getOffers().stream()
                .map(this::convertOfferToOfferDTO)
                .collect(Collectors.toList());
        productDTO.setOffers(offerDTOs);
        return productDTO;
    }

    public OfferDTO convertOfferToOfferDTO(Offer offer) {
        OfferDTO offerDTO = new OfferDTO();
        offerDTO.setId(offer.getId());
        offerDTO.setPrice(offer.getPrice());
        offerDTO.setStockQuantity(offer.getStockQuantity());
        offerDTO.setIsDeleted(offer.getIsDeleted());

        List<AttributeDTO> attributeDTOs = offer.getAttributes().stream()
                .map(this::convertAttributeToAttributeDTO)
                .collect(Collectors.toList());
        offerDTO.setAttributes(attributeDTOs);

        return offerDTO;
    }

    public AttributeDTO convertAttributeToAttributeDTO(Attribute attribute) {
        AttributeDTO attributeDTO = new AttributeDTO();
        attributeDTO.setName(attribute.getName());
        attributeDTO.setValue(attribute.getValue());
        return attributeDTO;
    }
}
