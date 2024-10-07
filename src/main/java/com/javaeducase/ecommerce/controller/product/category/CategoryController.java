package com.javaeducase.ecommerce.controller.product.category;

import com.javaeducase.ecommerce.dto.product.CategoryDTO;
import com.javaeducase.ecommerce.dto.product.ProductDTO;
import com.javaeducase.ecommerce.service.product.category.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping
    public ResponseEntity<List<CategoryDTO>> getAllCategories() {
        List<CategoryDTO> categories = categoryService.getAllCategories();
        return ResponseEntity.ok(categories);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryDTO> getCategoryById(@PathVariable Long id) {
        CategoryDTO category = categoryService.getCategoryById(id);
        return ResponseEntity.ok(category);
    }

    @GetMapping("/{id}/children")
    public ResponseEntity<List<CategoryDTO>> getCategoryChildren(@PathVariable Long id) {
        List<CategoryDTO> children = categoryService.getCategoryChildren(id);
        return ResponseEntity.ok(children);
    }

    @GetMapping("/{id}/products")
    public ResponseEntity<List<ProductDTO>> getCategoryProducts(@PathVariable Long id) {
        List<ProductDTO> products = categoryService.getCategoryProducts(id);
        return ResponseEntity.ok(products);
    }
}