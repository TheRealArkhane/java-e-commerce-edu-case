package com.javaeducase.ecommerce.service.product.category;

import com.javaeducase.ecommerce.dto.product.CategoryDTO;
import com.javaeducase.ecommerce.dto.product.CreateCategoryRequestDTO;
import com.javaeducase.ecommerce.entity.product.Category;
import com.javaeducase.ecommerce.entity.product.Product;
import com.javaeducase.ecommerce.exception.product.CategoryNotFoundException;
import com.javaeducase.ecommerce.repository.product.CategoryRepository;
import com.javaeducase.ecommerce.repository.product.ProductRepository;
import com.javaeducase.ecommerce.service.product.product.AdminProductService;
import com.javaeducase.ecommerce.util.product.CommonAllProductLinkedUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminCategoryService {

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;

    private final AdminProductService adminProductService;

    public CategoryDTO updateCategory(Long id, CategoryDTO categoryDTO) {
        log.info("Updating category with id: {}...", id);
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException("Category with id: " + id + " not found"));

        category.setName(categoryDTO.getName());
        categoryRepository.save(category);
        log.info("Category with id: {} updated successfully", id);
        return CommonAllProductLinkedUtils.convertCategoryToCategoryDTO(category);
    }

    public CategoryDTO createCategory(CreateCategoryRequestDTO createCategoryRequestDTO) {
        log.info("Creating new category with name: {}...", createCategoryRequestDTO.getName());

//        Category existingCategory = categoryRepository.findByName(createCategoryRequestDTO.getName()).orElse(null);
//        if (existingCategory != null) {
//            log.warn("Category with name: {} already exists", createCategoryRequestDTO.getName());
//            throw new IllegalArgumentException("Category with this name already exists");
//        }

        Category newCategory = new Category();
        newCategory.setName(createCategoryRequestDTO.getName());

        if (createCategoryRequestDTO.getParentId() != null) {
            Category parentCategory = categoryRepository.findById(createCategoryRequestDTO.getParentId())
                    .orElseThrow(() -> {
                        log.warn("Parent category with id: {} not found", createCategoryRequestDTO.getParentId());
                        return new CategoryNotFoundException("Parent category with id: "
                                + createCategoryRequestDTO.getParentId() + " not found");
                    });

            newCategory.setParent(parentCategory);
            parentCategory.getChildren().add(newCategory);
            log.info("Parent category with id: {} found and new category added as a child", parentCategory.getId());
        } else {
            newCategory.setParent(null);
            log.info("No parent category provided, setting category as root");
        }

        categoryRepository.save(newCategory);
        log.info("New category with name: {} created successfully", newCategory.getName());
        return CommonAllProductLinkedUtils.convertCategoryToCategoryDTO(newCategory);
    }

    public void deleteCategory(Long id) {
        log.info("Attempting to delete category with id: {}...", id);
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException("Category with id: " + id + " not found"));

        if (!category.getChildren().isEmpty()) {
            throw new IllegalStateException("Category cannot be deleted because it has children subcategories");
        }

        List<Product> productsInCategory = productRepository.findByCategoryId(id);

        if (!productsInCategory.isEmpty()) {
            log.info("Deleting associated products for category with id: {}...", id);
            for (Product product : productsInCategory) {
                adminProductService.deleteProduct(product.getId());
            }
            productRepository.saveAll(productsInCategory);
        }

        categoryRepository.delete(category);
        log.info("Category with id: {} deleted successfully", id);
    }
}

