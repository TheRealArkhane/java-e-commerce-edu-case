package com.javaeducase.ecommerce.service.product.category;

import com.javaeducase.ecommerce.dto.product.CategoryDTO;
import com.javaeducase.ecommerce.entity.product.Category;
import com.javaeducase.ecommerce.entity.product.Product;
import com.javaeducase.ecommerce.exception.product.CategoryNotFoundException;
import com.javaeducase.ecommerce.repository.product.CategoryRepository;
import com.javaeducase.ecommerce.repository.product.ProductRepository;
import com.javaeducase.ecommerce.util.product.CommonAllProductLinkedUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminCategoryService {

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final CommonAllProductLinkedUtils commonAllProductLinkedUtils;

    public CategoryDTO updateCategory(Long id, CategoryDTO categoryDTO) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException("Категория с id: " + id + " не найдена"));

        category.setName(categoryDTO.getName());
        categoryRepository.save(category);
        return commonAllProductLinkedUtils.convertCategoryToCategoryDTO(category);
    }

    public CategoryDTO createCategory(CategoryDTO categoryDTO) {
        Category newCategory = new Category();
        newCategory.setName(categoryDTO.getName());

        if (categoryDTO.getParent() != null) {
            Category parentCategory = categoryRepository.findById(categoryDTO.getParent().getId())
                    .orElseThrow(() -> new CategoryNotFoundException("Родительская категория с id: "
                            + categoryDTO.getParent().getId() + " не найдена"));
            newCategory.setParent(parentCategory);
            parentCategory.getChildren().add(newCategory); // Добавляем новую категорию в список дочерних
        }
        else {
            newCategory.setParent(null);
        }

        categoryRepository.save(newCategory);
        return commonAllProductLinkedUtils.convertCategoryToCategoryDTO(newCategory);
    }

    public void deleteCategory(Long id) {

        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException("Категория с id: " + id + " не найдена"));

        if (!category.getChildren().isEmpty()) {
            throw new IllegalStateException("Категория не может быть удалена, так как у неё есть дочерние категории.");
        }

        List<Product> productsInCategory = productRepository.findByCategoryId(id);

        if (!productsInCategory.isEmpty()) {
            for (Product product : productsInCategory) {
                product.setIsDeleted(true);
            }
        }
        productRepository.saveAll(productsInCategory);
        categoryRepository.delete(category);
    }
}
