package com.javaeducase.ecommerce.services.product.category;

import com.javaeducase.ecommerce.dto.product.CategoryDTO;
import com.javaeducase.ecommerce.entities.product.Category;
import com.javaeducase.ecommerce.entities.product.Product;
import com.javaeducase.ecommerce.exceptions.product.CategoryNotFoundException;
import com.javaeducase.ecommerce.repositories.product.CategoryRepository;
import com.javaeducase.ecommerce.repositories.product.ProductRepository;
import com.javaeducase.ecommerce.utils.product.CategoryUtils;
import com.javaeducase.ecommerce.utils.product.CommonAllProductLinkedUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminCategoryService {

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final CategoryService categoryService;
    private final CommonAllProductLinkedUtils commonAllProductLinkedUtils;
    private final CategoryUtils categoryUtils;

    @PreAuthorize("hasRole('ADMIN')")
    public CategoryDTO createCategory(String name, Long parentId) {
        Category parentCategory = null;

        if (parentId != null) {
            parentCategory = categoryRepository.findById(parentId)
                    .orElseThrow(() -> new CategoryNotFoundException("Родительская категория не найдена"));
        }

        Category newCategory = new Category();
        newCategory.setName(name);
        newCategory.setParent(parentCategory);

        Category savedCategory = categoryRepository.save(newCategory);

        if (parentCategory != null) {
            List<Category> childCategories = categoryRepository.findAllByParent(parentCategory);

            for (Category child : childCategories) {
                child.setParent(savedCategory);
                categoryRepository.save(child);
            }
        }

        return commonAllProductLinkedUtils.convertCategoryToCategoryDTO(savedCategory);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public CategoryDTO updateCategory(Long id, String newName) {
        Category category = categoryService.findCategoryById(id);
        category.setName(newName);
        Category updatedCategory = categoryRepository.save(category);
        return commonAllProductLinkedUtils.convertCategoryToCategoryDTO(updatedCategory);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public void deleteCategory(Long id) {
        Category category = categoryService.findCategoryById(id);

        List<Product> products = productRepository.findByCategory(category);
        for (Product product : products) {
            categoryUtils.markProductAndOffersAsDeleted(product);
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
}
