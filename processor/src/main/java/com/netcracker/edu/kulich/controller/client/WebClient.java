package com.netcracker.edu.kulich.controller.client;

import com.netcracker.edu.kulich.dto.CustomerDTO;
import com.netcracker.edu.kulich.dto.OfferDTO;
import com.netcracker.edu.kulich.dto.OrderDTO;

import java.util.List;
import java.util.Set;

public interface WebClient {

    List<OfferDTO> getAllOffers();

    OfferDTO getOfferById(Long id);

    List<CustomerDTO> getAllCustomers();

    CustomerDTO getCustomerByEmail(String email);

    CustomerDTO signUpWithEmail(CustomerDTO customer);

    OrderDTO getOrderById(Long id);

    List<OrderDTO> getAllOrders();

    OrderDTO addItemToOrder(Long orderId, Long itemId);

    OrderDTO removeItemFromOrder(Long orderId, Long itemId);

    OrderDTO payForOrder(Long orderId);

    List<OrderDTO> getAllOrdersByPaymentStatus(String status);

    List<OrderDTO> getAllOrdersByEmail(String email);

    Integer getAmountOfItemsPurchasedByCustomerWithEmail(String email);

    Double GetFullPriceOfItemsBoughtByCustomerWithEmail(String email);

    OrderDTO setNextOrderStatus(Long orderId);

    OrderDTO cancelOrder(Long orderId);

    OrderDTO saveOrder(OrderDTO orderDTO);

    Set<OfferDTO> getOffersByIds(Set<Long> ids);
}
