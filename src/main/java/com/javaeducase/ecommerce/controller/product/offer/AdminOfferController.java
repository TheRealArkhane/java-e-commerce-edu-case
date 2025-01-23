package com.javaeducase.ecommerce.controller.product.offer;

import com.javaeducase.ecommerce.dto.product.AttributeRequestDTO;
import com.javaeducase.ecommerce.dto.product.CreateOfferRequestDTO;
import com.javaeducase.ecommerce.dto.product.OfferDTO;
import com.javaeducase.ecommerce.handler.CustomErrorResponse;
import com.javaeducase.ecommerce.service.product.offer.AdminOfferService;
import com.javaeducase.ecommerce.service.product.offer.OfferService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Tag(name = "Admin Offers (Administrator)",
        description = "Methods for managing Offers")
@RestController
@RequestMapping("/admin/offers")
@RequiredArgsConstructor
public class AdminOfferController {

    private final AdminOfferService adminOfferService;
    private final OfferService offerService;


    @Operation(summary = "Create a new offer for a product",
            description = "Creates a new offer and associates it with a specified product.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201",
                    description = "Offer successfully created",
                    content = {@Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = OfferDTO.class))}),
            @ApiResponse(responseCode = "400",
                    description = "Invalid input data",
                    content = @Content),
            @ApiResponse(responseCode = "404",
                    description = "Product not found",
                    content = @Content)
    })
    @PostMapping("/create")
    public ResponseEntity<OfferDTO> createProductOffer(@RequestBody CreateOfferRequestDTO createOfferRequestDTO) {
        OfferDTO createdOffer = adminOfferService.createProductOffer(createOfferRequestDTO);
        return new ResponseEntity<>(createdOffer, HttpStatus.CREATED);
    }


    @Operation(summary = "Update an existing offer",
            description = "Updates the details of an existing offer.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Offer successfully updated",
                    content = {@Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = OfferDTO.class))}),
            @ApiResponse(responseCode = "404",
                    description = "Offer not found",
                    content = {@Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CustomErrorResponse.class))})
    })
    @PutMapping("/{id}")
    public ResponseEntity<OfferDTO> updateOffer(@PathVariable Long id, @RequestBody OfferDTO offerDTO) {
        return ResponseEntity.ok(adminOfferService.updateOffer(id, offerDTO));
    }


    @Operation(summary = "Delete an offer",
            description = "Marks an offer as isDeleted = true.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Offer successfully deleted",
                    content = {@Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Map.class))}),
            @ApiResponse(responseCode = "404",
                    description = "Offer not found",
                    content = {@Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CustomErrorResponse.class))})
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteOffer(@PathVariable Long id) {
        adminOfferService.deleteOffer(id);
        Map<String, String> responseBody = new HashMap<>();
        responseBody.put("message", "Offer successfully deleted");
        return ResponseEntity.ok(responseBody);
    }


    @Operation(summary = "Add an attribute to an offer",
            description = "Adds a specified attribute to an offer.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Attribute successfully added",
                    content = {@Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = OfferDTO.class))}),
            @ApiResponse(responseCode = "404",
                    description = "Offer or Attribute not found",
                    content = {@Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CustomErrorResponse.class))})
    })
    @PostMapping("/add_attribute_to_offer")
    public ResponseEntity<OfferDTO> addAttribute(@RequestBody AttributeRequestDTO attributeRequestDTO) {
        adminOfferService.addAttributeToOffer(attributeRequestDTO);
        return ResponseEntity.ok(offerService.getOfferById(attributeRequestDTO.getOfferId()));
    }

    @Operation(summary = "Remove an attribute from an offer",
            description = "Removes a specified attribute from an offer.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Attribute successfully removed",
                    content = {@Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = OfferDTO.class))}),
            @ApiResponse(responseCode = "404",
                    description = "Offer or Attribute not found",
                    content = {@Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CustomErrorResponse.class))})
    })
    @DeleteMapping("/remove_attribute_from_offer")
    public ResponseEntity<OfferDTO> removeAttribute(@RequestBody AttributeRequestDTO attributeRequestDTO) {
        return ResponseEntity.ok(adminOfferService.deleteAttributeFromOffer(attributeRequestDTO));
    }
}

