package com.javaeducase.ecommerce.services.product.category;

import com.javaeducase.ecommerce.dto.product.CategoryDTO;
import com.javaeducase.ecommerce.dto.product.ProductDTO;
import com.javaeducase.ecommerce.entities.product.Category;
import com.javaeducase.ecommerce.repositories.product.CategoryRepository;
import com.javaeducase.ecommerce.repositories.product.ProductRepository;
import com.javaeducase.ecommerce.utils.CategoryUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final CategoryUtils categoryUtils;

    public List<CategoryDTO> getAllCategories() {
        return categoryRepository.findAll().stream()
                .map(categoryUtils::convertCategoryToCategoryDTO)
                .collect(Collectors.toList());
    }

    public CategoryDTO getCategory(Long id) {
        Category category = categoryUtils.findCategoryById(id);
        return categoryUtils.convertCategoryToCategoryDTO(category);
    }

    public CategoryDTO getCategoryWithDescendants(Long id) {
        Category category = categoryUtils.findCategoryById(id);
        return categoryUtils.convertToCategoryDTOWithDescendants(category);
    }

    public List<ProductDTO> getProductsByCategory(Long id) {
        Category category = categoryUtils.findCategoryById(id);
        return productRepository.findByCategory(category).stream()
                .map(categoryUtils::convertProductToProductDTO)
                .collect(Collectors.toList());
    }

    public List<ProductDTO> getProductsByCategoryAndDescendants(Long id) {
        Category category = categoryUtils.findCategoryById(id);
        List<Category> categories = categoryRepository.findAllByParent(category);
        categories.add(category);
        return productRepository.findByCategoryIn(categories).stream()
                .map(categoryUtils::convertProductToProductDTO)
                .collect(Collectors.toList());
    }
}
