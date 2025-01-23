package com.javaeducase.ecommerce.controller.product.category;

import com.javaeducase.ecommerce.dto.product.CategoryDTO;
import com.javaeducase.ecommerce.dto.product.ProductDTO;
import com.javaeducase.ecommerce.handler.CustomErrorResponse;
import com.javaeducase.ecommerce.service.product.category.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;


    @Operation(summary = "Get all categories",
            description = "Retrieves a list of all categories.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Categories retrieved successfully",
                    content = {@Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = CategoryDTO.class)))})
    })
    @GetMapping
    public ResponseEntity<List<CategoryDTO>> getAllCategories() {
        List<CategoryDTO> categories = categoryService.getAllCategories();
        return ResponseEntity.ok(categories);
    }


    @Operation(summary = "Get a category by ID",
            description = "Retrieves a single category by ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Category retrieved successfully",
                    content = {@Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CategoryDTO.class))}),
            @ApiResponse(responseCode = "404",
                    description = "Category not found",
                    content = {@Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = CustomErrorResponse.class))})
    })
    @GetMapping("/{id}")
    public ResponseEntity<CategoryDTO> getCategoryById(@PathVariable Long id) {
        CategoryDTO category = categoryService.getCategoryById(id);
        return ResponseEntity.ok(category);
    }


    @Operation(summary = "Get children of a category",
            description = "Retrieves all subcategories (children) for a given category.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Children categories retrieved successfully",
                    content = {@Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = CategoryDTO.class)))}),
            @ApiResponse(responseCode = "404",
                    description = "Category not found",
                    content = {@Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CustomErrorResponse.class))})
    })
    @GetMapping("/{id}/children")
    public ResponseEntity<List<CategoryDTO>> getCategoryChildren(@PathVariable Long id) {
        List<CategoryDTO> children = categoryService.getCategoryChildren(id);
        return ResponseEntity.ok(children);
    }


    @Operation(summary = "Get products of a category",
            description = "Retrieves all products associated with a given category.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Products retrieved successfully",
                    content = {@Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = ProductDTO.class)))}),
            @ApiResponse(responseCode = "404",
                    description = "Category not found",
                    content = {@Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CustomErrorResponse.class))})
    })
    @GetMapping("/{id}/products")
    public ResponseEntity<List<ProductDTO>> getCategoryProducts(@PathVariable Long id) {
        List<ProductDTO> products = categoryService.getCategoryProducts(id);
        return ResponseEntity.ok(products);
    }
}
