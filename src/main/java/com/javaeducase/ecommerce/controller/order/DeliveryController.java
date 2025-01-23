package com.javaeducase.ecommerce.controller.order;

import com.javaeducase.ecommerce.entity.order.Delivery;
import com.javaeducase.ecommerce.entity.order.PickupLocation;
import com.javaeducase.ecommerce.handler.CustomErrorResponse;
import com.javaeducase.ecommerce.service.order.DeliveryService;
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
@RequestMapping("/deliveries")
@RequiredArgsConstructor
public class DeliveryController {

    private final DeliveryService deliveryService;


    @Operation(summary = "Get all deliveries",
            description = "Retrieve a list of all available deliveries")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Successfully retrieved the list of deliveries",
                    content = {@Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Delivery.class))
            })
    })
    @GetMapping
    public ResponseEntity<List<Delivery>> getAllDeliveries() {
        return ResponseEntity.ok(deliveryService.getDeliveries());
    }


    @Operation(summary = "Get delivery by ID",
            description = "Retrieve a delivery by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Successfully retrieved the delivery",
                    content = {@Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Delivery.class))
            }),
            @ApiResponse(responseCode = "404",
                    description = "Delivery not found",
                    content = {@Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CustomErrorResponse.class))
            })
    })
    @GetMapping("/{id}")
    public ResponseEntity<Delivery> getDeliveryById(@PathVariable Long id) {
        return ResponseEntity.ok(deliveryService.getDeliveryById(id));
    }


    @Operation(summary = "Get all pickup locations",
            description = "Retrieve a list of all pickup locations")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Successfully retrieved the list of pickup locations",
                    content = {@Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = PickupLocation.class))
            })
    })
    @GetMapping("/pickup_locations")
    public ResponseEntity<List<PickupLocation>> getPickupLocations() {
        return ResponseEntity.ok(deliveryService.getPickupLocations());
    }
}
