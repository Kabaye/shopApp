package com.netcracker.edu.kulich.controller.client;

import com.netcracker.edu.kulich.dto.*;
import com.netcracker.edu.kulich.dto.transformator.Transformator;
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
    private final RestTemplate restTemplate;
    private final String inventory;
    private final String catalog;
    private final String customerManagement;
    private final Transformator transformator;

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
        ResponseEntity<List<OfferDTO>> response = restTemplate.exchange(
                catalog + "/offers",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<OfferDTO>>() {
                });
        return response.getBody();
    }

    public OfferDTO getOfferById(Long id) {
        ResponseEntity<OfferDTO> response = restTemplate.exchange(
                catalog + "/offers/" + id,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<OfferDTO>() {
                });
        return response.getBody();
    }

    public List<CustomerDTO> getAllCustomers() {
        ResponseEntity<List<CustomerDTO>> response = restTemplate.exchange(
                customerManagement + "/customers",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<CustomerDTO>>() {
                });
        return response.getBody();
    }

    public CustomerDTO getCustomerByEmail(String email) {
        ResponseEntity<CustomerDTO> response = restTemplate.exchange(
                customerManagement + "/customers/" + email,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<CustomerDTO>() {
                });
        return response.getBody();
    }

    public CustomerDTO signUpWithEmail(CustomerDTO customer) {
        return restTemplate.exchange(
                customerManagement + "/customers",
                HttpMethod.POST,
                new HttpEntity<>(customer),
                new ParameterizedTypeReference<CustomerDTO>() {
                }
        ).getBody();
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

    public Integer getAmountOfItemsBoughtByCustomerWithEmail(String email) {
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
                HttpMethod.PUT,
                null,
                new ParameterizedTypeReference<OrderDTO>() {
                }).getBody();
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
