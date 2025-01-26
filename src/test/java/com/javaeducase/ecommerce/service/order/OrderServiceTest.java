package com.javaeducase.ecommerce.service.order;

import com.javaeducase.ecommerce.dto.order.OrderDTO;
import com.javaeducase.ecommerce.dto.order.RequestOrderDTO;
import com.javaeducase.ecommerce.entity.cart.Cart;
import com.javaeducase.ecommerce.entity.cart.CartItem;
import com.javaeducase.ecommerce.entity.order.Delivery;
import com.javaeducase.ecommerce.entity.order.Order;
import com.javaeducase.ecommerce.entity.order.OrderDetail;
import com.javaeducase.ecommerce.entity.order.Payment;
import com.javaeducase.ecommerce.entity.product.Offer;
import com.javaeducase.ecommerce.entity.user.User;
import com.javaeducase.ecommerce.repository.cart.CartRepository;
import com.javaeducase.ecommerce.repository.order.DeliveryRepository;
import com.javaeducase.ecommerce.repository.order.OrderRepository;
import com.javaeducase.ecommerce.repository.order.PaymentRepository;
import com.javaeducase.ecommerce.repository.order.PickupLocationRepository;
import com.javaeducase.ecommerce.repository.product.OfferRepository;
import com.javaeducase.ecommerce.service.cart.CartService;
import com.javaeducase.ecommerce.service.user.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private CartRepository cartRepository;

    @Mock
    private DeliveryRepository deliveryRepository;

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OfferRepository offerRepository;

    @Mock
    private PickupLocationRepository pickupLocationRepository;

    @Mock
    private UserService userService;

    @Mock
    private CartService cartService;

    @Mock
    private DaDataService daDataService;

    @InjectMocks
    private OrderService orderService;

    private User user;
    private Cart cart;
    private Payment payment;
    private Delivery delivery;
    private Offer offer;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);

        offer = new Offer();
        offer.setId(1L);
        offer.setPrice(100);
        offer.setStockQuantity(10);
        offer.setAttributes(new ArrayList<>());
        offer.setIsAvailable(true);
        offer.setIsDeleted(false);

        CartItem cartItem = new CartItem();
        cartItem.setOffer(offer);
        cartItem.setQuantity(2);

        cart = new Cart();
        cart.setId(1L);
        cart.setUser(user);
        cart.setItems(List.of(cartItem));
        cart.setTotalQuantity(2);
        cart.setTotalAmount(200);

        payment = new Payment();
        payment.setId(1L);
        payment.setName("Credit Card");

        delivery = new Delivery();
        delivery.setId(1L);
        delivery.setName("Courier");
        delivery.setPayments(new ArrayList<>());
        delivery.getPayments().add(payment);
        delivery.setDeliveryPrice(50);
    }

    @Test
    void createOrder_Success() {
        when(userService.getCurrentUser()).thenReturn(user);
        when(cartRepository.findByUserId(user.getId())).thenReturn(Optional.of(cart));
        when(deliveryRepository.findById(1L)).thenReturn(Optional.of(delivery));
        when(paymentRepository.findById(payment.getId())).thenReturn(Optional.of(payment));

        String validatedAddress = "Valid Address";
        when(daDataService.validateAddress("Valid Address")).thenReturn(validatedAddress);

        Order savedOrder = new Order();
        savedOrder.setId(1L);
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> {
            Order order = invocation.getArgument(0);
            order.setId(1L);
            return order;
        });

        RequestOrderDTO requestOrderDTO = new RequestOrderDTO();
        requestOrderDTO.setDeliveryId(1L);
        requestOrderDTO.setPaymentId(payment.getId());
        requestOrderDTO.setAddress("Valid Address");

        OrderDTO result = orderService.createOrder(requestOrderDTO);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(2, result.getTotalQuantity());
        assertEquals(250, result.getTotalAmount());
        assertEquals(validatedAddress, result.getAddress());

        verify(cartRepository).findByUserId(user.getId());
        verify(deliveryRepository).findById(1L);
        verify(paymentRepository).findById(payment.getId());
        verify(daDataService).validateAddress("Valid Address");
        verify(orderRepository).save(any(Order.class));
        verify(cartService).clearCart();
    }

    @Test
    void createOrder_CartIsEmpty() {
        when(userService.getCurrentUser()).thenReturn(user);

        Cart emptyCart = new Cart();
        emptyCart.setId(1L);
        emptyCart.setUser(user);
        emptyCart.setItems(new ArrayList<>());
        emptyCart.setTotalQuantity(0);
        emptyCart.setTotalAmount(0);

        when(cartRepository.findByUserId(user.getId())).thenReturn(Optional.of(emptyCart));

        RequestOrderDTO requestOrderDTO = new RequestOrderDTO();
        requestOrderDTO.setDeliveryId(1L);
        requestOrderDTO.setPaymentId(1L);
        requestOrderDTO.setAddress("Valid Address");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                orderService.createOrder(requestOrderDTO));

        assertEquals("Cart is empty", exception.getMessage());

        verify(cartRepository).findByUserId(user.getId());
        verifyNoInteractions(deliveryRepository, paymentRepository, daDataService, orderRepository, cartService);
    }

    @Test
    void getUserOrders_Success() {
        Order order = new Order();
        order.setId(1L);
        order.setUser(user);
        order.setTotalQuantity(2);
        order.setTotalAmount(250);
        order.setDelivery(delivery);
        order.setPayment(payment);

        OrderDetail orderDetail = new OrderDetail();
        orderDetail.setOrder(order);
        orderDetail.setOffer(offer);
        orderDetail.setQuantity(2);
        orderDetail.setTotalOfferAmount(200);
        order.getOrderDetails().add(orderDetail);

        when(orderRepository.findAllByUserId(user.getId())).thenReturn(List.of(order));

        List<OrderDTO> result = orderService.getUserOrders(user.getId());

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getId());
        assertEquals(2, result.get(0).getTotalQuantity());
        assertEquals(250, result.get(0).getTotalAmount());

        verify(orderRepository).findAllByUserId(user.getId());
    }
}

