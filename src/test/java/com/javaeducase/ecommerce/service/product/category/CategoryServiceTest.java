package com.javaeducase.ecommerce.service.product.category;

import com.javaeducase.ecommerce.dto.product.CategoryDTO;
import com.javaeducase.ecommerce.dto.product.ProductDTO;
import com.javaeducase.ecommerce.entity.product.Category;
import com.javaeducase.ecommerce.entity.product.Product;
import com.javaeducase.ecommerce.exception.product.CategoryNotFoundException;
import com.javaeducase.ecommerce.repository.product.CategoryRepository;
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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    @InjectMocks
    private CategoryService categoryService;

    @Mock
    private CategoryRepository categoryRepository;

    private Category category;
    private Category childCategory;
    private Product product;

    @BeforeEach
    void setUp() {
        category = new Category();
        category.setId(1L);
        category.setName("Electronics");

        childCategory = new Category();
        childCategory.setId(2L);
        childCategory.setName("Smartphones");
        childCategory.setParent(category);

        product = new Product();
        product.setId(1L);
        product.setName("iPhone 14");
        product.setCategory(childCategory);
        product.setOffers(new ArrayList<>());

        childCategory.setProducts(List.of(product));
        category.setChildren(List.of(childCategory));
    }

    @Test
    void getAllCategories_Success() {
        when(categoryRepository.findAll()).thenReturn(List.of(category, childCategory));

        List<CategoryDTO> result = categoryService.getAllCategories();

        assertEquals(2, result.size());
        verify(categoryRepository).findAll();
    }

    @Test
    void getCategoryById_Success() {
        when(categoryRepository.findById(category.getId())).thenReturn(Optional.of(category));

        CategoryDTO result = categoryService.getCategoryById(1L);

        assertNotNull(result);
        assertEquals("Electronics", result.getName());
        verify(categoryRepository).findById(1L);
    }

    @Test
    void getCategoryById_CategoryNotFound() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(CategoryNotFoundException.class, () -> categoryService.getCategoryById(1L));
        verify(categoryRepository).findById(1L);
    }

    @Test
    void getCategoryChildren_Success() {
        when(categoryRepository.findById(category.getId())).thenReturn(Optional.of(category));

        List<CategoryDTO> result = categoryService.getCategoryChildren(1L);

        assertEquals(1, result.size());
        assertEquals("Smartphones", result.get(0).getName());
        verify(categoryRepository).findById(1L);
    }

    @Test
    void getCategoryProducts_Success() {
        when(categoryRepository.findById(childCategory.getId())).thenReturn(Optional.of(childCategory));

        List<ProductDTO> result = categoryService.getCategoryProducts(2L);

        assertEquals(1, result.size());
        assertEquals("iPhone 14", result.get(0).getName());
        verify(categoryRepository).findById(2L);
    }
}
