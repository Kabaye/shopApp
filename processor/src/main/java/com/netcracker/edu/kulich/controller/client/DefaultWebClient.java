package com.netcracker.edu.kulich.controller.client;

import com.netcracker.edu.kulich.dto.CustomerDTO;
import com.netcracker.edu.kulich.dto.OfferDTO;
import com.netcracker.edu.kulich.dto.OrderDTO;
import com.netcracker.edu.kulich.dto.transformator.Transformator;
import com.netcracker.edu.kulich.logging.annotation.DefaultLogging;
import com.netcracker.edu.kulich.logging.annotation.Logging;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@DefaultLogging
@Component
public class DefaultWebClient implements WebClient {
    private RestTemplate restTemplate;
    private String inventory;
    private String catalog;
    private String customerManagement;
    private Transformator transformator;

    public DefaultWebClient(RestTemplate restTemplate, String inventoryUrl, String catalogUrl, String customerManagementUrl,
                            Transformator transformator, ResponseErrorHandler restTemplateResponseErrorHandler) {
        this.restTemplate = restTemplate;
        this.inventory = inventoryUrl + "/inventory";
        this.catalog = catalogUrl + "/catalog";
        this.customerManagement = customerManagementUrl + "/customer-management";
        this.transformator = transformator;
        this.restTemplate.setErrorHandler(restTemplateResponseErrorHandler);
    }

    @Logging(startMessage = "Sending request to get all offers...", endMessage = "Response on request for all offers received.")
    public List<OfferDTO> getAllOffers() {
        ResponseEntity<List<OfferDTO>> response = restTemplate.exchange(
                catalog + "/offers",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<OfferDTO>>() {
                });
        return response.getBody();
    }

    @Logging(startMessage = "Sending request to get offer by id...", endMessage = "Response on request for offer received.")
    public OfferDTO getOfferById(Long id) {
        ResponseEntity<OfferDTO> response = restTemplate.exchange(
                catalog + "/offers/" + id,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<OfferDTO>() {
                });
        return response.getBody();
    }

    @Logging(startMessage = "Sending request to get all customers...", endMessage = "Response on request for all customers received.")
    public List<CustomerDTO> getAllCustomers() {
        return restTemplate.exchange(
                customerManagement + "/customers",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<CustomerDTO>>() {
                }).getBody();
    }

    @Logging(startMessage = "Sending request to get customer by email...", endMessage = "Response on request for customer received.")
    public CustomerDTO getCustomerByEmail(String email) {
        return restTemplate.exchange(
                customerManagement + "/customers/" + email,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<CustomerDTO>() {
                }).getBody();
    }

    @Logging(startMessage = "Sending request for user registration...", endMessage = "Response on request for user registration received.")
    public CustomerDTO signUpWithEmail(CustomerDTO customer) {
        return restTemplate.exchange(
                customerManagement + "/customers",
                HttpMethod.POST,
                new HttpEntity<>(customer),
                new ParameterizedTypeReference<CustomerDTO>() {
                }).getBody();
    }

    @Logging(startMessage = "Sending request to get order by id...", endMessage = "Response on request for customer received.")
    public OrderDTO getOrderById(Long id) {
        return restTemplate.exchange(inventory + "/orders/" + id,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<OrderDTO>() {
                }).getBody();
    }

    @Logging(startMessage = "Sending request to get all orders...", endMessage = "Response on request for orders received.")
    public List<OrderDTO> getAllOrders() {
        return restTemplate.exchange(inventory + "/orders",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<OrderDTO>>() {
                }).getBody();
    }

    @Logging(startMessage = "Sending request to add order item to order...", endMessage = "Response on request for adding order item received.")
    public OrderDTO addItemToOrder(Long orderId, Long itemId) {
        return restTemplate.exchange(inventory + "/orders/" + orderId + "/items",
                HttpMethod.PUT,
                new HttpEntity<>(transformator.convertOfferDtoToOrderItemDto(getOfferById(itemId))),
                new ParameterizedTypeReference<OrderDTO>() {
                }).getBody();
    }

    @Logging(startMessage = "Sending request to remove order item from order...", endMessage = "Response on request for removing order item received.")
    public OrderDTO removeItemFromOrder(Long orderId, Long itemId) {
        return restTemplate.exchange(inventory + "/orders/" + orderId + "/items/" + itemId,
                HttpMethod.DELETE,
                null,
                new ParameterizedTypeReference<OrderDTO>() {
                }).getBody();
    }

    @Logging(startMessage = "Sending request to pay for order...", endMessage = "Response on request for paying for order received.")
    public OrderDTO payForOrder(Long orderId) {
        return restTemplate.exchange(inventory + "/orders/" + orderId + "/pay",
                HttpMethod.POST,
                null,
                new ParameterizedTypeReference<OrderDTO>() {
                }).getBody();
    }

    @Logging(startMessage = "Sending request to get orders by payment status...", endMessage = "Response on request for getting orders by payment status received.")
    public List<OrderDTO> getAllOrdersByPaymentStatus(String status) {
        return restTemplate.exchange(UriComponentsBuilder.fromHttpUrl(inventory)
                        .path("/orders")
                        .queryParam("status", status)
                        .toUriString(),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<OrderDTO>>() {
                }).getBody();
    }

    @Logging(startMessage = "Sending request to get orders by e-mail...", endMessage = "Response on request for getting orders by e-mail received.")
    public List<OrderDTO> getAllOrdersByEmail(String email) {
        return restTemplate.exchange(UriComponentsBuilder.fromHttpUrl(inventory)
                        .path("/orders")
                        .queryParam("email", email)
                        .toUriString(),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<OrderDTO>>() {
                }).getBody();
    }

    @Logging(startMessage = "Sending request to get amount of items, purchased by e-mail...", endMessage = "Response on request for getting amount of items, purchased by e-mail, received.")
    public Integer getAmountOfItemsPurchasedByCustomerWithEmail(String email) {
        return restTemplate.exchange(inventory + "/orders/" + email + "/amount",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<Integer>() {
                }).getBody();
    }

    @Logging(startMessage = "Sending request to get full price of items, purchased by e-mail...", endMessage = "Response on request for getting full price of items, purchased by e-mail, received.")
    public Double GetFullPriceOfItemsBoughtByCustomerWithEmail(String email) {
        return restTemplate.exchange(inventory + "/orders/" + email + "/full-price",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<Double>() {
                }).getBody();
    }

    @Logging(startMessage = "Sending request to set next status to order...", endMessage = "Response on request for installing next status to order received.")
    public OrderDTO setNextOrderStatus(Long orderId) {
        return restTemplate.exchange(inventory + "/orders/" + orderId + "/next",
                HttpMethod.POST,
                null,
                new ParameterizedTypeReference<OrderDTO>() {
                }).getBody();
    }

    @Logging(startMessage = "Sending request to cancel order...", endMessage = "Response on request for cancelling order received.")
    public OrderDTO cancelOrder(Long orderId) {
        return restTemplate.exchange(inventory + "/orders/" + orderId + "/cancel",
                HttpMethod.POST,
                null,
                new ParameterizedTypeReference<OrderDTO>() {
                }).getBody();
    }

    @Logging(startMessage = "Sending request to save order...", endMessage = "Response on request for saving order received.")
    public OrderDTO saveOrder(OrderDTO orderDTO) {
        return restTemplate.exchange(
                inventory + "/orders",
                HttpMethod.POST,
                new HttpEntity<>(orderDTO),
                new ParameterizedTypeReference<OrderDTO>() {
                }
        ).getBody();
    }

    @Logging(startMessage = "Sending request to get all orders by ids...", endMessage = "Response on request for getting orders received.")
    public Set<OfferDTO> getOffersByIds(Set<Long> ids) {
        return ids.stream()
                .map(this::getOfferById)
                .collect(Collectors.toSet());
    }

}
