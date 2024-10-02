package com.javaeducase.ecommerce.services.product.category;

import com.javaeducase.ecommerce.dto.product.CategoryDTO;
import com.javaeducase.ecommerce.dto.product.ProductDTO;
import com.javaeducase.ecommerce.entities.product.Category;
import com.javaeducase.ecommerce.exceptions.product.CategoryNotFoundException;
import com.javaeducase.ecommerce.repositories.product.CategoryRepository;
import com.javaeducase.ecommerce.repositories.product.ProductRepository;
import com.javaeducase.ecommerce.utils.product.CommonAllProductLinkedUtils;
import com.javaeducase.ecommerce.utils.product.ProductUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final CommonAllProductLinkedUtils commonAllProductLinkedUtils;
    private final ProductUtils productUtils;

    public List<CategoryDTO> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        return categories.stream()
                .map(commonAllProductLinkedUtils::convertCategoryToCategoryDTO)
                .collect(Collectors.toList());
    }

    public CategoryDTO getCategoryById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException("Категория с id: " + id + " не найдена"));
        return commonAllProductLinkedUtils.convertCategoryToCategoryDTO(category);
    }

    public List<CategoryDTO> getCategoryChildren(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException("Категория с id: " + id + " не найдена"));
        return category.getChildren().stream()
                .map(commonAllProductLinkedUtils::convertCategoryToCategoryDTO)
                .collect(Collectors.toList());
    }

    public List<ProductDTO> getCategoryProducts(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException("Категория с id: " + id + " не найдена"));
        return category.getProducts().stream()
                .map(productUtils::convertProductToProductDTO)
                .collect(Collectors.toList());
    }
}
