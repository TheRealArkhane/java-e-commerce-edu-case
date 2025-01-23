package com.javaeducase.ecommerce.controller.order;

import com.javaeducase.ecommerce.dto.order.OrderDTO;
import com.javaeducase.ecommerce.dto.order.RequestOrderDTO;
import com.javaeducase.ecommerce.handler.CustomErrorResponse;
import com.javaeducase.ecommerce.service.order.OrderService;
import com.javaeducase.ecommerce.service.user.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final UserService userService;


    @Operation(summary = "Get current user's orders",
            description = "Retrieve all orders for the currently authenticated user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Successfully retrieved orders",
                    content = {@Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = OrderDTO.class))
            })
    })
    @GetMapping("/me")
    public ResponseEntity<List<OrderDTO>> getOrders() {
        return ResponseEntity.ok(orderService.getUserOrders(userService.getCurrentUser().getId()));
    }


    @Operation(summary = "Create a new order",
            description = "Create a new order from the user's cart")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201",
                    description = "Order successfully created",
                    content = {@Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = OrderDTO.class))
            }),
            @ApiResponse(responseCode = "400",
                    description = "Invalid order data",
                    content = {@Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CustomErrorResponse.class))
            }),
            @ApiResponse(responseCode = "404",
                    description = "Cart or other related entity not found",
                    content = {@Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CustomErrorResponse.class))
            })
    })
    @PostMapping("/create")
    public ResponseEntity<OrderDTO> createOrder(@RequestBody RequestOrderDTO requestOrderDTO) {
        OrderDTO orderDTO = orderService.createOrder(requestOrderDTO);
        return new ResponseEntity<>(orderDTO, HttpStatus.CREATED);
    }
}
