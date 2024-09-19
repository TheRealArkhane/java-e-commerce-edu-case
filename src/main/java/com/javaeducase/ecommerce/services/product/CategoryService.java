package com.javaeducase.ecommerce.services.product;

import com.javaeducase.ecommerce.dto.product.AttributeDTO;
import com.javaeducase.ecommerce.dto.product.OfferDTO;
import com.javaeducase.ecommerce.dto.product.ProductDTO;
import com.javaeducase.ecommerce.dto.product.CategoryDTO;
import com.javaeducase.ecommerce.entities.product.Attribute;
import com.javaeducase.ecommerce.entities.product.Category;
import com.javaeducase.ecommerce.entities.product.Offer;
import com.javaeducase.ecommerce.entities.product.Product;
import com.javaeducase.ecommerce.exceptions.product.CategoryNotFoundException;
import com.javaeducase.ecommerce.repositories.product.CategoryRepository;
import com.javaeducase.ecommerce.repositories.product.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;


    public List<CategoryDTO> getAllCategories() {
        return categoryRepository.findAll().stream()
                .map(this::convertCategoryToCategoryDTO)
                .collect(Collectors.toList());
    }

    public CategoryDTO getCategoryWithDescendants(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException("Категория не найдена"));
        return convertToCategoryDTOWithDescendants(category);
    }

    public List<ProductDTO> getProductsByCategory(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException("Категория не найдена"));
        return productRepository.findByCategory(category).stream()
                .map(this::convertProductToProductDTO)
                .collect(Collectors.toList());
    }

    public List<ProductDTO> getProductsByCategoryAndDescendants(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException("Категория не найдена"));
        List<Category> categories = categoryRepository.findAllByParent(category);
        categories.add(category);
        return productRepository.findByCategoryIn(categories).stream()
                .map(this::convertProductToProductDTO)
                .collect(Collectors.toList());
    }

    @PreAuthorize("hasRole('ADMIN')")
    public CategoryDTO updateCategoryName(Long id, String newName) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException("Категория не найдена"));
        category.setName(newName);
        Category updatedCategory = categoryRepository.save(category);
        return convertCategoryToCategoryDTO(updatedCategory);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public void deleteCategory(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException("Категория не найдена"));

        List<Product> products = productRepository.findByCategory(category);
        for (Product product : products) {
            for (Offer offer : product.getOffers()) {
                offer.setIsDeleted(true);
            }
            product.setIsDeleted(true);
            productRepository.save(product);
        }

        deleteCategoryAndDescendants(category);
    }

    private void deleteCategoryAndDescendants(Category category) {
        List<Category> descendants = categoryRepository.findAllByParent(category);
        for (Category descendant : descendants) {
            deleteCategoryAndDescendants(descendant);
        }
        categoryRepository.delete(category);
    }

    private CategoryDTO convertToCategoryDTOWithDescendants(Category category) {
        CategoryDTO categoryDTO = convertCategoryToCategoryDTO(category);
        List<CategoryDTO> childrenWithDescendants = category.getChildren().stream()
                .map(this::convertToCategoryDTOWithDescendants)
                .collect(Collectors.toList());
        categoryDTO.setChildren(childrenWithDescendants);
        return categoryDTO;
    }

    private ProductDTO convertProductToProductDTO(Product product) {
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

    private CategoryDTO convertCategoryToCategoryDTO(Category category) {
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

    private OfferDTO convertOfferToOfferDTO(Offer offer) {
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

    private AttributeDTO convertAttributeToAttributeDTO(Attribute attribute) {
        AttributeDTO attributeDTO = new AttributeDTO();
        attributeDTO.setName(attribute.getName());
        attributeDTO.setValue(attribute.getValue());
        return attributeDTO;
    }
}
