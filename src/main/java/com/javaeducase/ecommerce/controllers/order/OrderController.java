package com.javaeducase.ecommerce.controllers.order;

import com.javaeducase.ecommerce.dto.order.OrderDTO;
import com.javaeducase.ecommerce.dto.order.RequestOrderDTO;
import com.javaeducase.ecommerce.services.order.OrderService;
import com.javaeducase.ecommerce.services.user.UserService;
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

    @GetMapping
    public ResponseEntity<List<OrderDTO>> getOrders() {
        return ResponseEntity.ok(orderService.getUserOrders(userService.getCurrentUser().getId()));
    }

    @PostMapping("/create")
    public ResponseEntity<OrderDTO> createOrder(@RequestBody RequestOrderDTO requestOrderDTO) {
        OrderDTO orderDTO = orderService.createOrder(requestOrderDTO);
        return new ResponseEntity<>(orderDTO, HttpStatus.CREATED);
    }
}
