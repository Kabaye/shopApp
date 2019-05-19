package com.netcracker.edu.kulich.controller.client;

import com.netcracker.edu.kulich.dto.CustomerDTO;
import com.netcracker.edu.kulich.dto.OfferDTO;
import com.netcracker.edu.kulich.dto.OrderDTO;
import com.netcracker.edu.kulich.dto.SimplifiedOrderDTO;

import java.util.List;

public interface WebClient {

    List<OfferDTO> getAllOffers();

    OfferDTO getOfferById(Long id);

    List<CustomerDTO> getAllCustomers();

    CustomerDTO getCustomerByEmail(String email);

    CustomerDTO signUpWithEmail(CustomerDTO customer);

    OrderDTO createOrder(SimplifiedOrderDTO simplifiedOrder);

    OrderDTO getOrderById(Long id);

    List<OrderDTO> getAllOrders();

    OrderDTO addItemToOrder(Long orderId, Long itemId);

    void removeItemFromOrder(Long orderId, Long itemId);

    OrderDTO payForOrder(Long orderId);

    List<OrderDTO> getAllOrdersByPaymentStatus(String status);

    List<OrderDTO> getAllOrdersByEmail(String email);

    Integer getAmountOfItemsPurchasedByCustomerWithEmail(String email);

    Double GetFullPriceOfItemsBoughtByCustomerWithEmail(String email);

    OrderDTO setNextOrderStatus(Long orderId);

    OrderDTO cancelOrder(Long orderId);

    OrderDTO saveOrder(OrderDTO orderDTO);
}
