package com.javaeducase.ecommerce.service.product.category;

import com.javaeducase.ecommerce.dto.product.CategoryDTO;
import com.javaeducase.ecommerce.dto.product.ProductDTO;
import com.javaeducase.ecommerce.entity.product.Category;
import com.javaeducase.ecommerce.exception.product.CategoryNotFoundException;
import com.javaeducase.ecommerce.repository.product.CategoryRepository;
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
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public List<CategoryDTO> getAllCategories() {
        log.info("Fetching all categories...");
        List<Category> categories = categoryRepository.findAll();
        log.info("Fetched {} categories", categories.size());
        return categories.stream()
                .map(CommonAllProductLinkedUtils::convertCategoryToCategoryDTO)
                .collect(Collectors.toList());
    }

    public CategoryDTO getCategoryById(Long id) {
        log.info("Fetching category with id: {}...", id);
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException("Category with id: " + id + " not found"));
        log.info("Fetched category with id: {}", id);
        return CommonAllProductLinkedUtils.convertCategoryToCategoryDTO(category);
    }

    public List<CategoryDTO> getCategoryChildren(Long id) {
        log.info("Fetching children for category with id: {}...", id);
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException("Category with id: " + id + " not found"));
        List<CategoryDTO> childrenDTO = category.getChildren().stream()
                .map(CommonAllProductLinkedUtils::convertCategoryToCategoryDTO)
                .collect(Collectors.toList());
        log.info("Retrieved {} children for category with id: {}", childrenDTO.size(), id);
        return childrenDTO;
    }

    public List<ProductDTO> getCategoryProducts(Long id) {
        log.info("Fetching products for category with id: {}...", id);
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException("Category with id: " + id + " not found"));
        List<ProductDTO> productsDTO = category.getProducts().stream()
                .map(ProductUtils::convertProductToProductDTO)
                .collect(Collectors.toList());
        log.info("Retrieved {} products for category with id: {}", productsDTO.size(), id);
        return productsDTO;
    }
}

