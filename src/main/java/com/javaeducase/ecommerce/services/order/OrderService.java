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
import com.javaeducase.ecommerce.exceptions.order.DeliveryNotFoundException;
import com.javaeducase.ecommerce.exceptions.order.PaymentNotFoundException;
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
                .orElseThrow(() -> new CartNotFoundException("У пользователя отсутствует корзина"));

        if (cart.getItems().isEmpty()) {
            throw new IllegalArgumentException("Корзина пуста, необходимо заполнить ее товаром");
        }

        Delivery delivery = deliveryRepository.findById(deliveryId)
                .orElseThrow(() -> new DeliveryNotFoundException("Не найден способ доставки с id: " + deliveryId));

        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new PaymentNotFoundException("Не найден способ доставки с id: " + paymentId));

        if (!delivery.getPayments().contains(payment)) {
            throw new IllegalArgumentException("Выбранный способ оплаты недопустим для выбранного способа доставки");
        }

        Order order = new Order();
        order.setUser(currentUser);
        if (address == null || address.isEmpty()) {
            throw new IllegalArgumentException("Необходимо ввести адрес");
        }
        else if (delivery.getId().equals(1L)) {
            if (!pickupLocationRepository.findByAddress(address).isPresent()) {
                throw new IllegalArgumentException("Такого пункта самовывоза нет");
            }
            order.setAddress(address);
        }
        else {
            order.setAddress(address);
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
        order.setTotalQuantity(cart.getTotalQuantity());
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
