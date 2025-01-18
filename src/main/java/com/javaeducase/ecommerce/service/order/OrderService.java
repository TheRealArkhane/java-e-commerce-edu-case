package com.javaeducase.ecommerce.service.order;

import com.javaeducase.ecommerce.dto.order.OrderDTO;
import com.javaeducase.ecommerce.dto.order.RequestOrderDTO;
import com.javaeducase.ecommerce.entity.cart.Cart;
import com.javaeducase.ecommerce.entity.cart.CartItem;
import com.javaeducase.ecommerce.entity.order.*;
import com.javaeducase.ecommerce.entity.product.Offer;
import com.javaeducase.ecommerce.entity.user.User;
import com.javaeducase.ecommerce.exception.cart.CartNotFoundException;
import com.javaeducase.ecommerce.exception.order.DeliveryNotFoundException;
import com.javaeducase.ecommerce.exception.order.PaymentNotFoundException;
import com.javaeducase.ecommerce.repository.cart.CartRepository;
import com.javaeducase.ecommerce.repository.order.DeliveryRepository;
import com.javaeducase.ecommerce.repository.order.OrderRepository;
import com.javaeducase.ecommerce.repository.order.PaymentRepository;
import com.javaeducase.ecommerce.repository.order.PickupLocationRepository;
import com.javaeducase.ecommerce.repository.product.OfferRepository;
import com.javaeducase.ecommerce.service.cart.CartService;
import com.javaeducase.ecommerce.service.user.UserService;
import com.javaeducase.ecommerce.util.order.OrderUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

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
    private final DaDataService daDataService;

    @Transactional
    public OrderDTO createOrder(RequestOrderDTO requestOrderDTO) {

        Long deliveryId = requestOrderDTO.getDeliveryId();
        Long paymentId = requestOrderDTO.getPaymentId();
        String address = requestOrderDTO.getAddress();
        User currentUser = userService.getCurrentUser();

        Cart cart = cartRepository.findByUserId(currentUser.getId())
                .orElseThrow(() -> new CartNotFoundException("Cart not found"));

        if (cart.getItems().isEmpty()) {
            throw new IllegalArgumentException("Cart is empty");
        }

        Delivery delivery = deliveryRepository.findById(deliveryId)
                .orElseThrow(() -> new DeliveryNotFoundException("Do not found delivery with id: " + deliveryId));

        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new PaymentNotFoundException("Do not found payment with id: " + paymentId));

        if (!delivery.getPayments().contains(payment)) {
            throw new IllegalArgumentException("The selected payment method is not allowed for the chosen delivery method");
        }

        Order order = new Order();
        order.setUser(currentUser);
        String daDataAddress = daDataService.validateAddress(address);
        if (daDataAddress == null || daDataAddress.isEmpty()) {
            throw new IllegalArgumentException("An address is required");
        }
        else if (Objects.equals(delivery.getName(), "���������")) {
            PickupLocation pickupLocation = pickupLocationRepository.findByAddress(daDataAddress).orElse(null);
            if (pickupLocation == null) {
                throw new IllegalArgumentException("There is no such pickup location");
            }
            order.setAddress(daDataAddress);
            order.setPickupLocation(pickupLocation.getName());
        }
        else {
            if (pickupLocationRepository.findAllAddress().contains(daDataAddress)) {
                throw new IllegalArgumentException("Delivery cannot be ordered to a pickup location address");
            }
            order.setAddress(daDataAddress);
            order.setPickupLocation(null);
        }

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
        order.setTotalQuantity(cart.calculateTotalQuantity());
        order.setTotalAmount(cart.calculateTotalAmount() + delivery.getDeliveryPrice());
        orderRepository.save(order);

        cart.getItems().forEach(cartItem -> {
            Offer offer = cartItem.getOffer();
            int remainingStock = offer.getStockQuantity() - cartItem.getQuantity();
            if (remainingStock == 0) {
                offer.setIsAvailable(false);
            }
            offer.setStockQuantity(remainingStock);
            offerRepository.save(offer);
        });
        cartService.clearCart();
        cartRepository.save(cart);

        return OrderUtils.convertOrderToOrderDTO(order);
    }

    public List<OrderDTO> getUserOrders(Long userId) {
        return orderRepository.findAllByUserId(userId).stream().map(OrderUtils::convertOrderToOrderDTO).toList();
    }
}
