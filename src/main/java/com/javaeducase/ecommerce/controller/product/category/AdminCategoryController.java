package com.javaeducase.ecommerce.controller.product.category;

import com.javaeducase.ecommerce.dto.product.CategoryDTO;
import com.javaeducase.ecommerce.handler.CustomErrorResponse;
import com.javaeducase.ecommerce.service.product.category.AdminCategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Tag(name = "Admin Categories (Administrator)",
        description = "Methods for managing Categories")
@RestController
@RequestMapping("/admin/categories")
@RequiredArgsConstructor
public class AdminCategoryController {

    private final AdminCategoryService adminCategoryService;


    @Operation(summary = "Create a new category",
            description = "Creates a new category. If a parent category is provided, " +
                    "the new category will be added as its child.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Category created successfully",
                    content = {@Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CategoryDTO.class))}),
            @ApiResponse(responseCode = "400",
                    description = "Invalid request data",
                    content = {@Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CustomErrorResponse.class))}),
            @ApiResponse(responseCode = "404",
                    description = "Parent category not found",
                    content = {@Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CustomErrorResponse.class))})
    })
    @PostMapping("/create")
    public ResponseEntity<CategoryDTO> createCategory(@RequestBody CategoryDTO categoryDTO) {
        CategoryDTO createdCategory = adminCategoryService.createCategory(categoryDTO);
        return ResponseEntity.ok(createdCategory);
    }


    @Operation(summary = "Update an existing category",
            description = "Updates the details of an existing category by ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Category updated successfully",
                    content = {@Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CategoryDTO.class))}),
            @ApiResponse(responseCode = "400",
                    description = "Invalid request data",
                    content = {@Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CustomErrorResponse.class))}),
            @ApiResponse(responseCode = "404",
                    description = "Category not found",
                    content = {@Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CustomErrorResponse.class))})
    })
    @PutMapping("/{id}")
    public ResponseEntity<CategoryDTO> updateCategory(@PathVariable Long id, @RequestBody CategoryDTO categoryDTO) {
        CategoryDTO updatedCategory = adminCategoryService.updateCategory(id, categoryDTO);
        return ResponseEntity.ok(updatedCategory);
    }


    @Operation(summary = "Delete a category",
            description = "Deletes a category by its ID. " +
                    "Ensures no subcategories or products are associated before deletion.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Category deleted successfully",
                    content = {@Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Map.class))}),
            @ApiResponse(responseCode = "400",
                    description = "Category has subcategories or associated products",
                    content = {@Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CustomErrorResponse.class))}),
            @ApiResponse(responseCode = "404",
                    description = "Category not found",
                    content = {@Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CustomErrorResponse.class))})
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteCategory(@PathVariable Long id) {
        adminCategoryService.deleteCategory(id);
        Map<String, String> responseBody = new HashMap<>();
        responseBody.put("message", "Category successfully deleted");
        return ResponseEntity.ok(responseBody);
    }
}

