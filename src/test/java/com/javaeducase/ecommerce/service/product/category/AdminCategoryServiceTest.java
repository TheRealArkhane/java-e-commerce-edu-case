package com.javaeducase.ecommerce.service.product.category;

import com.javaeducase.ecommerce.dto.product.CategoryDTO;
import com.javaeducase.ecommerce.dto.product.CreateCategoryRequestDTO;
import com.javaeducase.ecommerce.entity.product.Category;
import com.javaeducase.ecommerce.entity.product.Product;
import com.javaeducase.ecommerce.repository.product.CategoryRepository;
import com.javaeducase.ecommerce.repository.product.ProductRepository;
import com.javaeducase.ecommerce.service.product.product.AdminProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdminCategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private AdminProductService adminProductService;

    @InjectMocks
    private AdminCategoryService adminCategoryService;

    private Category parentCategory;
    private Category childCategory;
    private Product product;

    @BeforeEach
    void setUp() {
        parentCategory = new Category();
        parentCategory.setId(1L);
        parentCategory.setName("Electronics");
        parentCategory.setChildren(new ArrayList<>());

        childCategory = new Category();
        childCategory.setId(2L);
        childCategory.setName("Smartphones");
        childCategory.setParent(parentCategory);
        childCategory.setChildren(new ArrayList<>());
        parentCategory.getChildren().add(childCategory);

        product = new Product();
        product.setId(1L);
        product.setName("iPhone 14");
        product.setOffers(new ArrayList<>());
        product.setCategory(childCategory);
    }

    @Test
    void updateCategory_Success() {
        CategoryDTO updatedCategoryDTO = new CategoryDTO();
        updatedCategoryDTO.setName("Updated Electronics");

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(parentCategory));
        when(categoryRepository.save(any(Category.class))).thenReturn(parentCategory);

        CategoryDTO result = adminCategoryService.updateCategory(1L, updatedCategoryDTO);

        assertNotNull(result);
        assertEquals("Updated Electronics", result.getName());
        verify(categoryRepository, times(1)).findById(1L);
        verify(categoryRepository, times(1)).save(parentCategory);
    }

    @Test
    void createCategory_Success() {
        CreateCategoryRequestDTO createCategoryRequestDTO = new CreateCategoryRequestDTO();
        createCategoryRequestDTO.setName("Laptops");
        createCategoryRequestDTO.setParentId(parentCategory.getId()); // Parent category ID set
        Category newCategory = new Category();
        newCategory.setId(3L);
        newCategory.setName("Laptops");
        newCategory.setParent(parentCategory);
        parentCategory.getChildren().add(newCategory);

        when(categoryRepository.findById(parentCategory.getId())).thenReturn(Optional.of(parentCategory));
        when(categoryRepository.save(any(Category.class))).thenReturn(newCategory);

        CategoryDTO result = adminCategoryService.createCategory(createCategoryRequestDTO);

        assertNotNull(result);
        assertEquals("Laptops", result.getName());
        assertEquals(parentCategory.getId(), result.getParentId());  // Check parentId
        verify(categoryRepository).save(any(Category.class));
    }

    @Test
    void createCategory_Success_CreateRootCategory() {
        CreateCategoryRequestDTO createCategoryRequestDTO = new CreateCategoryRequestDTO();
        createCategoryRequestDTO.setName("RootCategory");
        createCategoryRequestDTO.setParentId(null); // Create root category (no parent)

        Category rootCategory = new Category();
        rootCategory.setId(4L);
        rootCategory.setName("RootCategory");

        when(categoryRepository.save(any(Category.class))).thenReturn(rootCategory);

        CategoryDTO result = adminCategoryService.createCategory(createCategoryRequestDTO);

        assertNotNull(result);
        assertEquals("RootCategory", result.getName());
        assertNull(result.getParent());  // Check that parentId is null for root
        verify(categoryRepository, times(1)).save(any(Category.class));
    }

    @Test
    void deleteCategory_Success() {
        when(categoryRepository.findById(2L)).thenReturn(Optional.of(childCategory));
        when(productRepository.findByCategoryId(2L)).thenReturn(List.of(product));

        adminCategoryService.deleteCategory(2L);

        verify(categoryRepository).findById(2L);
        verify(categoryRepository).delete(childCategory);
    }

    @Test
    void deleteCategory_HaveChildren() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(parentCategory));

        Exception exception = assertThrows(IllegalStateException.class, () -> adminCategoryService.deleteCategory(1L));
        assertEquals("Category cannot be deleted because it has children subcategories", exception.getMessage());
        verify(categoryRepository, times(1)).findById(1L);
        verify(categoryRepository, never()).delete(any(Category.class));
    }
}


