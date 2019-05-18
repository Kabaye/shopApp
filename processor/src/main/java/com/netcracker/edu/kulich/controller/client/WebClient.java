package com.netcracker.edu.kulich.controller.client;

import com.netcracker.edu.kulich.dto.*;
import com.netcracker.edu.kulich.dto.transformator.Transformator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class WebClient {
    private static final Logger logger = LoggerFactory.getLogger(WebClient.class);

    private RestTemplate restTemplate;
    private String inventory;
    private String catalog;
    private String customerManagement;
    private Transformator transformator;

    public WebClient(@Value("${inventory.url}") String inventory, @Value("${catalog.url}") String catalog,
                     @Value("${customer.url}") String customerManagement, RestTemplate restTemplate, ResponseErrorHandler restTemplateResponseErrorHandler, Transformator transformator) {
        this.inventory = inventory;
        this.catalog = catalog;
        this.customerManagement = customerManagement;
        this.restTemplate = restTemplate;
        this.transformator = transformator;
        this.restTemplate.setErrorHandler(restTemplateResponseErrorHandler);
    }

    public List<OfferDTO> getAllOffers() {
        logger.info("Sending request to get all offers.");
        ResponseEntity<List<OfferDTO>> response = restTemplate.exchange(
                catalog + "/offers",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<OfferDTO>>() {
                });
        logger.info("Response received.");
        return response.getBody();
    }

    public OfferDTO getOfferById(Long id) {
        logger.info("Sending request to get offer by id.");
        ResponseEntity<OfferDTO> response = restTemplate.exchange(
                catalog + "/offers/" + id,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<OfferDTO>() {
                });
        logger.info("Response received.");
        return response.getBody();
    }

    public List<CustomerDTO> getAllCustomers() {
        logger.info("Sending request to get all customers.");
        ResponseEntity<List<CustomerDTO>> response = restTemplate.exchange(
                customerManagement + "/customers",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<CustomerDTO>>() {
                });
        logger.info("Response received.");
        return response.getBody();
    }

    public CustomerDTO getCustomerByEmail(String email) {
        logger.info("Sending request to get customer by email.");
        ResponseEntity<CustomerDTO> response = restTemplate.exchange(
                customerManagement + "/customers/" + email,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<CustomerDTO>() {
                });
        logger.info("Response received.");
        return response.getBody();
    }

    public CustomerDTO signUpWithEmail(CustomerDTO customer) {
        logger.info("Sending request for user registration...");
        ResponseEntity<CustomerDTO> response = restTemplate.exchange(
                customerManagement + "/customers",
                HttpMethod.POST,
                new HttpEntity<>(customer),
                new ParameterizedTypeReference<CustomerDTO>() {
                });
        logger.info("Response received.");
        return response.getBody();
    }

    public OrderDTO createOrder(SimplifiedOrderDTO simplifiedOrder) {
        CustomerDTO customer = getCustomerByEmail(simplifiedOrder.getEmail());

        Set<OfferDTO> offers = getOffersByIds(simplifiedOrder.getItemIds());

        Set<OrderItemDTO> items = offers.stream().map(transformator::convertOfferDtoToOrderItemDto).collect(Collectors.toSet());

        OrderDTO orderDTO = new OrderDTO(simplifiedOrder.getDate(), customer.getEmail(), items);
        return saveOrder(orderDTO);
    }

    public OrderDTO getOrderById(Long id) {
        return restTemplate.exchange(inventory + "/orders/" + id,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<OrderDTO>() {
                }).getBody();
    }

    public List<OrderDTO> getAllOrders() {
        return restTemplate.exchange(inventory + "/orders",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<OrderDTO>>() {
                }).getBody();
    }

    public OrderDTO addItemToOrder(Long orderId, Long itemId) {
        return restTemplate.exchange(inventory + "/orders/" + orderId + "/items",
                HttpMethod.PUT,
                new HttpEntity<>(transformator.convertOfferDtoToOrderItemDto(getOfferById(itemId))),
                new ParameterizedTypeReference<OrderDTO>() {
                }).getBody();
    }

    public void removeItemFromOrder(Long orderId, Long itemId) {
        restTemplate.exchange(inventory + "/orders/" + orderId + "/items/" + itemId,
                HttpMethod.DELETE,
                null,
                new ParameterizedTypeReference<OrderDTO>() {
                }).getBody();
    }

    public OrderDTO payForOrder(Long orderId) {
        return restTemplate.exchange(inventory + "/orders/" + orderId + "/pay",
                HttpMethod.PUT,
                null,
                new ParameterizedTypeReference<OrderDTO>() {
                }).getBody();
    }

    public List<OrderDTO> getAllOrdersByPaymentStatus(String status) {
        return restTemplate.exchange(inventory + "/statuses/" + status,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<OrderDTO>>() {
                }).getBody();
    }

    public List<OrderDTO> getAllOrdersByEmail(String email) {
        return restTemplate.exchange(inventory + "/emails/" + email,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<OrderDTO>>() {
                }).getBody();
    }

    public Integer getAmountOfItemsPurchasedByCustomerWithEmail(String email) {
        return restTemplate.exchange(inventory + "/emails/" + email + "/amount",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<Integer>() {
                }).getBody();
    }

    public Double GetFullPriceOfItemsBoughtByCustomerWithEmail(String email) {
        return restTemplate.exchange(inventory + "/emails/" + email + "/full-price",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<Double>() {
                }).getBody();
    }

    public OrderDTO setNextOrderStatus(Long orderId) {
        return restTemplate.exchange(inventory + "/orders/" + orderId + "/status/next",
                HttpMethod.POST,
                null,
                new ParameterizedTypeReference<OrderDTO>() {
                }).getBody();
    }

    public OrderDTO cancelOrder(Long orderId) {
        OrderDTO orderDTO = restTemplate.exchange(inventory + "/orders/" + orderId + "/status/cancel",
                HttpMethod.POST,
                null,
                new ParameterizedTypeReference<OrderDTO>() {
                }).getBody();
        return orderDTO;
    }

    private OrderDTO saveOrder(OrderDTO orderDTO) {
        orderDTO = restTemplate.exchange(
                inventory + "/orders",
                HttpMethod.POST,
                new HttpEntity<>(orderDTO),
                new ParameterizedTypeReference<OrderDTO>() {
                }
        ).getBody();
        return orderDTO;
    }

    private Set<OfferDTO> getOffersByIds(Set<Long> ids) {
        return ids.stream()
                .map(this::getOfferById)
                .collect(Collectors.toSet());
    }

}
