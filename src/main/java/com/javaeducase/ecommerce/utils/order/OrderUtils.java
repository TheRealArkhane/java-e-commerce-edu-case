package com.javaeducase.ecommerce.utils.order;

import com.javaeducase.ecommerce.dto.cart.CartDTO;
import com.javaeducase.ecommerce.dto.cart.CartItemDTO;
import com.javaeducase.ecommerce.dto.order.OrderDTO;
import com.javaeducase.ecommerce.entities.cart.Cart;
import com.javaeducase.ecommerce.entities.order.Order;
import com.javaeducase.ecommerce.utils.cart.CartUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class OrderUtils {

    private final CartUtils cartUtils;

    public OrderDTO convertOrderToOrderDTO(Order order) {

        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setId(order.getId());
        orderDTO.setAddress(order.getAddress());
        orderDTO.setDelivery(order.getDelivery());
        orderDTO.setPayment(order.getPayment());
        orderDTO.setOrderCreateDateTime(order.getOrderCreateDateTime());
        orderDTO.setTotalAmount(order.getTotalAmount());
        orderDTO.setCart(cartUtils.convertCartToCartDTO(order.getCart()));
        return orderDTO;
    }
}
