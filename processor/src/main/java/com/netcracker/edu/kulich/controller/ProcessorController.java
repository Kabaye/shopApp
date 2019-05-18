package com.netcracker.edu.kulich.controller;

import com.netcracker.edu.kulich.controller.client.WebClient;
import com.netcracker.edu.kulich.dto.CustomerDTO;
import com.netcracker.edu.kulich.dto.OfferDTO;
import com.netcracker.edu.kulich.dto.OrderDTO;
import com.netcracker.edu.kulich.dto.SimplifiedOrderDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ProcessorController {
    private static final Logger logger = LoggerFactory.getLogger(ProcessorController.class);

    private final WebClient webClient;

    public ProcessorController(WebClient webClient) {
        this.webClient = webClient;
    }


    @GetMapping(value = "/offers")
    @ResponseStatus(HttpStatus.OK)
    public List<OfferDTO> getAllOffers() {
        logger.info("Getting all offers.");
        List<OfferDTO> offers = webClient.getAllOffers();
        logger.info("All offers received.");
        return offers;
    }

    @GetMapping(value = "/offers/{id:[\\d]+}")
    @ResponseStatus(HttpStatus.OK)
    public OfferDTO getOfferById(@PathVariable("id") Long id) {
        logger.info("Getting offer by id.");
        OfferDTO offer = webClient.getOfferById(id);
        logger.info("Offer received.");
        return offer;
    }

    @GetMapping(value = "/customers")
    @ResponseStatus(HttpStatus.OK)
    public List<CustomerDTO> getAllCustomers() {
        logger.info("Getting all customers.");
        List<CustomerDTO> customers = webClient.getAllCustomers();
        logger.info("All customers received.");
        return customers;
    }

    @GetMapping(value = "/customers/{email}")
    @ResponseStatus(HttpStatus.OK)
    public CustomerDTO getCustomerById(@PathVariable("email") String email) {
        logger.info("Getting customer by email.");
        CustomerDTO customerDTO = webClient.getCustomerByEmail(email);
        logger.info("Customer received.");
        return customerDTO;
    }

    @PostMapping(value = "/customers")
    @ResponseStatus(HttpStatus.CREATED)
    public CustomerDTO signUp(@RequestBody CustomerDTO customerDTO) {
        logger.info("Signing up with e-mail.");
        CustomerDTO customer = webClient.signUpWithEmail(customerDTO);
        logger.info("Customer signed up.");
        return customer;
    }

    @PostMapping(value = "/orders")
    @ResponseStatus(HttpStatus.CREATED)
    public OrderDTO createOrder(@RequestBody SimplifiedOrderDTO simplifiedOrderDTO) {
        logger.info("Creating order with e-mail, date, and list of offers.");
        OrderDTO orderDTO = webClient.createOrder(simplifiedOrderDTO);
        logger.info("Order created.");
        return orderDTO;
    }

    @GetMapping(value = "/orders/{id:[\\d]+}")
    @ResponseStatus(HttpStatus.OK)
    public OrderDTO getOrderById(@PathVariable("id") Long id) {
        logger.info("Getting order by id.");
        OrderDTO orderDTO = webClient.getOrderById(id);
        logger.info("Order received.");
        return orderDTO;
    }

    @GetMapping(value = "/orders")
    @ResponseStatus(HttpStatus.OK)
    public List<OrderDTO> getAllOrders() {
        logger.info("Getting all orders.");
        List<OrderDTO> ordersDTO = webClient.getAllOrders();
        logger.info("Orders received.");
        return ordersDTO;
    }

    @PutMapping(value = "/orders/{id:[\\d]+}/items")
    @ResponseStatus(HttpStatus.OK)
    public OrderDTO addItemToOrder(@RequestBody Long itemId, @PathVariable("id") Long id) {
        logger.info("Adding order item to order.");
        OrderDTO order = webClient.addItemToOrder(id, itemId);
        logger.info("Order item added.");
        return order;
    }

    @DeleteMapping(value = "/orders/{id:[\\d]+}/items/{itemId:[\\d]+}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeItemFromOrder(@PathVariable("id") Long id, @PathVariable("itemId") Long itemId) {
        logger.info("Deleting order item from order.");
        webClient.removeItemFromOrder(id, itemId);
        logger.info("Order item deleted.");
    }

    @PutMapping(value = "/orders/{id:[\\d]+}/pay")
    @ResponseStatus(HttpStatus.OK)
    public OrderDTO payForOrder(@PathVariable("id") Long id) {
        logger.info("Paying for order.");
        OrderDTO orderDTO = webClient.payForOrder(id);
        logger.info("Order is paid.");
        return orderDTO;
    }


    @GetMapping(value = "/statuses/{payStatus}")
    @ResponseStatus(HttpStatus.OK)
    public List<OrderDTO> getAllOrdersByPaymentStatus(@PathVariable("payStatus") String status) {
        logger.info("Getting all orders by payment status.");
        List<OrderDTO> orderDTOS = webClient.getAllOrdersByPaymentStatus(status);
        logger.info("All orders received.");
        return orderDTOS;
    }

    @GetMapping(value = "/emails/{email}")
    @ResponseStatus(HttpStatus.OK)
    public List<OrderDTO> getAllOrdersByEmail(@PathVariable("email") String email) {
        logger.info("Getting all orders by e-mail.");
        List<OrderDTO> orderDTOS = webClient.getAllOrdersByEmail(email);
        logger.info("All orders received.");
        return orderDTOS;
    }

    @GetMapping(value = "/emails/{email}/amount")
    @ResponseStatus(HttpStatus.OK)
    public String getAmountOfItemsPurchasedByCustomerWithEmail(@PathVariable("email") String email) {
        logger.info("Getting amount of items, purchased by customer.");
        Integer amount = webClient.getAmountOfItemsPurchasedByCustomerWithEmail(email);
        logger.info("Amount of items, purchased by customer, received.");
        return "The number of offers purchased by the customer with e-mail: \"" + email + "\" is: " + amount + ";";
    }

    @GetMapping(value = "/emails/{email}/price")
    @ResponseStatus(HttpStatus.OK)
    public String GetFullPriceOfItemsBoughtByCustomerWithEmail(@PathVariable("email") String email) {
        logger.info("Getting full price of items, purchased by customer.");
        Double fullPrice = webClient.GetFullPriceOfItemsBoughtByCustomerWithEmail(email);
        logger.info("Full price of items, purchased by customer, received.");
        return "The total price of goods purchased by the customer with e-mail: \"" + email + "\" is: " + String.format("%.2f", fullPrice) + " Belorussian rubles;";
    }

    @PostMapping(value = "/orders/{id:[\\d]+}/status/next")
    @ResponseStatus(HttpStatus.OK)
    public OrderDTO setNextOrderStatus(@PathVariable("id") Long id) {
        logger.info("Setting next order status.");
        OrderDTO orderDTO = webClient.setNextOrderStatus(id);
        logger.info("Next order status installed");
        return orderDTO;
    }

    @PostMapping(value = "/orders/{id:[\\d]+}/cancel")
    @ResponseStatus(HttpStatus.OK)
    public OrderDTO cancelOrder(@PathVariable("id") Long id) {
        logger.info("Cancelling order.");
        OrderDTO orderDTO = webClient.cancelOrder(id);
        logger.info("Order canceled.");
        return orderDTO;
    }
}
