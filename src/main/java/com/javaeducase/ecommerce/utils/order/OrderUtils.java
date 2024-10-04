package com.javaeducase.ecommerce.utils.order;

import com.javaeducase.ecommerce.dto.order.DeliveryDTO;
import com.javaeducase.ecommerce.dto.order.OrderDTO;
import com.javaeducase.ecommerce.dto.order.OrderDetailDTO;
import com.javaeducase.ecommerce.dto.order.PaymentDTO;
import com.javaeducase.ecommerce.entities.order.Delivery;
import com.javaeducase.ecommerce.entities.order.Order;
import com.javaeducase.ecommerce.entities.order.OrderDetail;
import com.javaeducase.ecommerce.entities.order.Payment;
import com.javaeducase.ecommerce.utils.product.CommonAllProductLinkedUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class OrderUtils {

    private final CommonAllProductLinkedUtils commonAllProductLinkedUtils;

    public OrderDTO convertOrderToOrderDTO(Order order) {

        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setId(order.getId());
        orderDTO.setAddress(order.getAddress());

        List<OrderDetail> orderDetails = order.getOrderDetails();
        List<OrderDetailDTO> orderDetailsDTO = new ArrayList<>();
        for (OrderDetail orderDetail : orderDetails) {
            orderDetailsDTO.add(convertOrderDetailToOrderDetailDTO(orderDetail));
        }
        orderDTO.setOrderDetailDTO(orderDetailsDTO);

        orderDTO.setDelivery(convertDeliveryToDeliveryDTO(order.getDelivery()));
        orderDTO.setPayment(convertPaymentToPaymentDTO(order.getPayment()));
        orderDTO.setOrderCreateDateTime(order.getOrderCreateDateTime());
        orderDTO.setTotalQuantity(order.getTotalQuantity());
        orderDTO.setTotalAmount(order.getTotalAmount());
        return orderDTO;
    }

    public DeliveryDTO convertDeliveryToDeliveryDTO(Delivery delivery) {
        DeliveryDTO deliveryDTO = new DeliveryDTO();
        deliveryDTO.setId(delivery.getId());
        deliveryDTO.setName(delivery.getName());
        deliveryDTO.setDeliveryPrice(delivery.getDeliveryPrice());
        return deliveryDTO;
    }

    public PaymentDTO convertPaymentToPaymentDTO(Payment payment) {
        PaymentDTO paymentDTO = new PaymentDTO();
        paymentDTO.setId(payment.getId());
        paymentDTO.setName(payment.getName());
        return paymentDTO;
    }

    public OrderDetailDTO convertOrderDetailToOrderDetailDTO(OrderDetail orderDetail) {
        OrderDetailDTO orderDetailDTO = new OrderDetailDTO();
        orderDetailDTO.setOffer(commonAllProductLinkedUtils.convertOfferToOfferDTO(orderDetail.getOffer()));
        orderDetailDTO.setQuantity(orderDetail.getQuantity());
        orderDetailDTO.setTotalOfferAmount(orderDetail.getTotalOfferAmount());
        return orderDetailDTO;
    }
}
