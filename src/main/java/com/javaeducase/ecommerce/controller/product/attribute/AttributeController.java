package com.javaeducase.ecommerce.controller.product.attribute;

import com.javaeducase.ecommerce.dto.product.AttributeDTO;
import com.javaeducase.ecommerce.handler.CustomErrorResponse;
import com.javaeducase.ecommerce.service.product.attribute.AttributeService;
import io.swagger.v3.oas.annotations.Operation;
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
@RequestMapping("/attributes")
@RequiredArgsConstructor
public class AttributeController {

    private final AttributeService attributeService;


    @Operation(summary = "Get all attributes",
            description = "Retrieve a list of all attributes.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Attributes retrieved successfully",
                    content = {@Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = List.class))})
    })
    @GetMapping
    public ResponseEntity<List<AttributeDTO>> getAllAttributes() {
        return ResponseEntity.ok(attributeService.getAllAttributes());
    }


    @Operation(summary = "Get attribute by ID",
            description = "Retrieve an attribute by ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Attribute retrieved successfully",
                    content = {@Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = AttributeDTO.class))}),
            @ApiResponse(responseCode = "404",
                    description = "Attribute not found",
                    content = {@Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CustomErrorResponse.class))})
    })
    @GetMapping("/{id}")
    public ResponseEntity<AttributeDTO> getAttributeById(@PathVariable Long id) {
        return ResponseEntity.ok(attributeService.getAttributeById(id));
    }
}

