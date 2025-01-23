package com.javaeducase.ecommerce.controller.product.offer;

import com.javaeducase.ecommerce.dto.product.AttributeDTO;
import com.javaeducase.ecommerce.dto.product.OfferDTO;
import com.javaeducase.ecommerce.handler.CustomErrorResponse;
import com.javaeducase.ecommerce.service.product.offer.OfferService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "Basic Offers (Customer)",
        description = "Methods for read-only interacting with Offers")
@RestController
@RequestMapping("/offers")
@RequiredArgsConstructor
public class OfferController {

    private final OfferService offerService;


    @Operation(summary = "Get an offer by ID",
            description = "Fetches an offer by ID from the db.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Offer successfully fetched",
                    content = {@Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = OfferDTO.class))}),
            @ApiResponse(responseCode = "404",
                    description = "Offer not found",
                    content = {@Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CustomErrorResponse.class))})
    })
    @GetMapping("/{id}")
    public ResponseEntity<OfferDTO> getOfferById(@PathVariable Long id) {
        return ResponseEntity.ok(offerService.getOfferById(id));
    }


    @Operation(summary = "Get attributes of an offer by ID",
            description = "Fetches all attributes of an offer by ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Attributes successfully fetched",
                    content = {@Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = List.class))}),
            @ApiResponse(responseCode = "404",
                    description = "Offer not found",
                    content = {@Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CustomErrorResponse.class))})
    })
    @GetMapping("/{id}/attributes")
    public ResponseEntity<List<AttributeDTO>> getOfferAttributes(@PathVariable Long id) {
        return ResponseEntity.ok(offerService.getAttributesByOfferId(id));
    }
}
