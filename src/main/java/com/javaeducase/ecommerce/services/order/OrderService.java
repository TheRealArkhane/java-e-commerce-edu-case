package com.javaeducase.ecommerce.services.order;

import com.javaeducase.ecommerce.dto.order.OrderDTO;
import com.javaeducase.ecommerce.dto.order.RequestOrderDTO;
import com.javaeducase.ecommerce.entities.cart.Cart;
import com.javaeducase.ecommerce.entities.cart.CartItem;
import com.javaeducase.ecommerce.entities.order.Delivery;
import com.javaeducase.ecommerce.entities.order.Order;
import com.javaeducase.ecommerce.entities.order.OrderDetail;
import com.javaeducase.ecommerce.entities.order.Payment;
import com.javaeducase.ecommerce.entities.product.Offer;
import com.javaeducase.ecommerce.entities.user.User;
import com.javaeducase.ecommerce.exceptions.cart.CartNotFoundException;
import com.javaeducase.ecommerce.repositories.cart.CartRepository;
import com.javaeducase.ecommerce.repositories.order.DeliveryRepository;
import com.javaeducase.ecommerce.repositories.order.OrderRepository;
import com.javaeducase.ecommerce.repositories.order.PaymentRepository;
import com.javaeducase.ecommerce.repositories.order.PickupLocationRepository;
import com.javaeducase.ecommerce.repositories.product.OfferRepository;
import com.javaeducase.ecommerce.services.cart.CartService;
import com.javaeducase.ecommerce.services.user.UserService;
import com.javaeducase.ecommerce.utils.order.OrderUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final CartRepository cartRepository;
    private final DeliveryRepository deliveryRepository;
    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;
    private final OfferRepository offerRepository;
    private final PickupLocationRepository pickupLocationRepository;
    private final UserService userService;
    private final CartService cartService;
    private final OrderUtils orderUtils;

    @Transactional
    public OrderDTO createOrder(RequestOrderDTO requestOrderDTO) {

        Long deliveryId = requestOrderDTO.getDeliveryId();
        Long paymentId = requestOrderDTO.getPaymentId();
        String address = requestOrderDTO.getAddress();
        User currentUser = userService.getCurrentUser();

        Cart cart = cartRepository.findByUserId(currentUser.getId())
                .orElseThrow(() -> new CartNotFoundException("User cart not found"));

        if (cart.getItems().isEmpty()) {
            throw new IllegalArgumentException("Cart is empty, cannot create order");
        }

        // Получаем информацию о доставке и проверяем валидность
        Delivery delivery = deliveryRepository.findById(deliveryId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid delivery method"));

        // Проверяем валидность метода оплаты
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid payment method"));

        if (!delivery.getPayments().contains(payment)) {
            throw new IllegalArgumentException("Selected payment method is not valid for this delivery");
        }

        String orderAddress;
        if (address == null || address.isEmpty()) {
            throw new IllegalArgumentException("Address is required for courier delivery");
        }
        else if (delivery.getId().equals(1L)) {
            if (!pickupLocationRepository.findByAddress(address).isPresent()) {
                throw new IllegalArgumentException("Такого пункта самовывоза нет");
            }
            else {
                orderAddress = address;
            }
        }
        else {
            throw new IllegalArgumentException("Invalid delivery method");
        }



        Order order = new Order();
        order.setUser(currentUser);
        order.setAddress(orderAddress);

        for (CartItem cartItem : cart.getItems()) {
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrder(order);
            orderDetail.setOffer(cartItem.getOffer());
            orderDetail.setQuantity(cartItem.getQuantity());
            orderDetail.setTotalOfferAmount(cartItem.getTotalPrice());

            order.getOrderDetails().add(orderDetail);
        }

        order.setDelivery(delivery);
        order.setPayment(payment);
        order.setTotalAmount(cart.getTotalAmount() + delivery.getDeliveryPrice());
        orderRepository.save(order);

        cart.getItems().forEach(cartItem -> {
            Offer offer = cartItem.getOffer();
            int remainingStock = offer.getStockQuantity() - cartItem.getQuantity();
            offer.setStockQuantity(remainingStock);
            offerRepository.save(offer);
        });
        cartService.clearCart();
        cartRepository.save(cart);

        return orderUtils.convertOrderToOrderDTO(order);
    }

    public List<OrderDTO> getUserOrders(Long userId) {
        return orderRepository.findAllByUserId(userId).stream().map(orderUtils::convertOrderToOrderDTO).toList();
    }
}
