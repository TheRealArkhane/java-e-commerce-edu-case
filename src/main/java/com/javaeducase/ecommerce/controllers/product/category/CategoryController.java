package com.javaeducase.ecommerce.controllers.product.category;

import com.javaeducase.ecommerce.dto.product.CategoryDTO;
import com.javaeducase.ecommerce.dto.product.ProductDTO;
import com.javaeducase.ecommerce.services.product.category.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping("/all")
    public ResponseEntity<List<CategoryDTO>> findAll() {
        List<CategoryDTO> categories = categoryService.getAllCategories();
        return ResponseEntity.ok(categories);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryDTO> getCategory(@PathVariable Long id) {
        return ResponseEntity.ok(categoryService.getCategory(id));
    }

    @GetMapping("/{id}/with_descendants")
    public ResponseEntity<List<CategoryDTO>> getCategoryWithDescendants(@PathVariable Long id) {
        return ResponseEntity.ok(categoryService.getCategoryWithChildren(id));
    }

    @GetMapping("/{id}/products")
    public ResponseEntity<List<ProductDTO>> getProductsByCategory(@PathVariable Long id) {
        return ResponseEntity.ok(categoryService.getProductsByCategory(id));
    }

    @GetMapping("/{id}/with_descendants/products")
    public ResponseEntity<List<ProductDTO>> getProductsByCategoryAndDescendants(@PathVariable Long id) {
        return ResponseEntity.ok(categoryService.getProductsByCategoryAndDescendants(id));
    }
}
