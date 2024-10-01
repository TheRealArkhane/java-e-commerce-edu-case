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

import java.util.ArrayList;
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
        return categoryRepository.findAll().stream()
                .map(commonAllProductLinkedUtils::convertCategoryToCategoryDTO)
                .collect(Collectors.toList());
    }

    public CategoryDTO getCategory(Long id) {
        Category category = findCategoryById(id);
        return commonAllProductLinkedUtils.convertCategoryToCategoryDTO(category);
    }

    public List<CategoryDTO> getCategoryWithChildren(Long id) {
        List<Category> childrenCategories = categoryRepository.findAllByParentId(id);
        List<CategoryDTO> childrenCategoriesDTO = new ArrayList<>();
        for (Category category : childrenCategories) {
            childrenCategoriesDTO.add(commonAllProductLinkedUtils.convertCategoryToCategoryDTO(category));
        }
        return childrenCategoriesDTO;
    }

    public List<ProductDTO> getProductsByCategory(Long id) {
        Category category = findCategoryById(id);
        return productRepository.findByCategory(category).stream()
                .map(productUtils::convertProductToProductDTO)
                .collect(Collectors.toList());
    }

    public List<ProductDTO> getProductsByCategoryAndDescendants(Long id) {
        Category category = findCategoryById(id);
        List<Category> categories = categoryRepository.findAllByParent(category);
        categories.add(category);
        return productRepository.findByCategoryIn(categories).stream()
                .map(productUtils::convertProductToProductDTO)
                .collect(Collectors.toList());
    }

    public Category findCategoryById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException("Категория не найдена"));
    }
}
