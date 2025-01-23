package com.javaeducase.ecommerce.controller.product.attribute;

import com.javaeducase.ecommerce.dto.product.AttributeDTO;
import com.javaeducase.ecommerce.handler.CustomErrorResponse;
import com.javaeducase.ecommerce.service.product.attribute.AdminAttributeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Admin Attributes (Administrator)",
        description = "Methods for managing Attributes")
@RestController
@RequestMapping("/admin/attributes")
@RequiredArgsConstructor
public class AdminAttributeController {

    private final AdminAttributeService adminAttributeService;


    @Operation(summary = "Create a new attribute",
            description = "Allows the admin to create a new attribute with a name and value.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Attribute created successfully",
                    content = {@Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = AttributeDTO.class))}),
            @ApiResponse(responseCode = "400",
                    description = "Invalid input data",
                    content = {@Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CustomErrorResponse.class))})
    })
    @PostMapping("/create")
    public ResponseEntity<AttributeDTO> createAttribute(@RequestBody AttributeDTO attributeDTO) {
        AttributeDTO createdAttribute = adminAttributeService.createAttribute(attributeDTO);
        return ResponseEntity.ok(createdAttribute);
    }


    @Operation(summary = "Update an existing attribute",
            description = "Allows the admin to update an attribute's name and value by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Attribute updated successfully",
                    content = {@Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = AttributeDTO.class))}),
            @ApiResponse(responseCode = "404",
                    description = "Attribute not found",
                    content = {@Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CustomErrorResponse.class))}),
            @ApiResponse(responseCode = "400",
                    description = "Duplicate attribute or invalid data",
                    content = {@Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CustomErrorResponse.class))})
    })
    @PutMapping("/{id}")
    public ResponseEntity<AttributeDTO> updateAttribute(
            @PathVariable Long id,
            @RequestBody AttributeDTO attributeDTO) {
        AttributeDTO updatedAttribute = adminAttributeService.updateAttribute(id, attributeDTO);
        return ResponseEntity.ok(updatedAttribute);
    }
}

