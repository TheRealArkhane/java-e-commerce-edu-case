package com.javaeducase.ecommerce.service.product.category;

import com.javaeducase.ecommerce.dto.product.CategoryDTO;
import com.javaeducase.ecommerce.dto.product.ProductDTO;
import com.javaeducase.ecommerce.entity.product.Category;
import com.javaeducase.ecommerce.exception.product.CategoryNotFoundException;
import com.javaeducase.ecommerce.repository.product.CategoryRepository;
import com.javaeducase.ecommerce.repository.product.ProductRepository;
import com.javaeducase.ecommerce.util.product.CommonAllProductLinkedUtils;
import com.javaeducase.ecommerce.util.product.ProductUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public List<CategoryDTO> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        return categories.stream()
                .map(CommonAllProductLinkedUtils::convertCategoryToCategoryDTO)
                .collect(Collectors.toList());
    }

    public CategoryDTO getCategoryById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException("Category with id: " + id + " not found"));
        return CommonAllProductLinkedUtils.convertCategoryToCategoryDTO(category);
    }

    public List<CategoryDTO> getCategoryChildren(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException("Category with id: " + id + " not found"));
        return category.getChildren().stream()
                .map(CommonAllProductLinkedUtils::convertCategoryToCategoryDTO)
                .collect(Collectors.toList());
    }

    public List<ProductDTO> getCategoryProducts(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException("Category with id: " + id + " not found"));
        return category.getProducts().stream()
                .map(ProductUtils::convertProductToProductDTO)
                .collect(Collectors.toList());
    }
}
