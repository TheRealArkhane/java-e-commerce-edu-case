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

    public CategoryDTO updateCategory(Long id, CategoryDTO categoryDTO) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException("��������� � id: " + id + " �� �������"));

        category.setName(categoryDTO.getName());
        categoryRepository.save(category);
        return CommonAllProductLinkedUtils.convertCategoryToCategoryDTO(category);
    }

    public CategoryDTO createCategory(CategoryDTO categoryDTO) {
        Category newCategory = new Category();
        newCategory.setName(categoryDTO.getName());

        if (categoryDTO.getParent() != null) {
            Category parentCategory = categoryRepository.findById(categoryDTO.getParent().getId())
                    .orElseThrow(() -> new CategoryNotFoundException("������������ ��������� � id: "
                            + categoryDTO.getParent().getId() + " �� �������"));
            newCategory.setParent(parentCategory);
            parentCategory.getChildren().add(newCategory); // ��������� ����� ��������� � ������ ��������
        }
        else {
            newCategory.setParent(null);
        }

        categoryRepository.save(newCategory);
        return CommonAllProductLinkedUtils.convertCategoryToCategoryDTO(newCategory);
    }

    public void deleteCategory(Long id) {

        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException("��������� � id: " + id + " �� �������"));

        if (!category.getChildren().isEmpty()) {
            throw new IllegalStateException("��������� �� ����� ���� �������, ��� ��� � �� ���� �������� ���������.");
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
