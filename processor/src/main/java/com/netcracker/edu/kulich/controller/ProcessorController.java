package com.netcracker.edu.kulich.controller;

import com.netcracker.edu.kulich.controller.client.WebClient;
import com.netcracker.edu.kulich.dto.*;
import com.netcracker.edu.kulich.dto.transformator.Transformator;
import com.netcracker.edu.kulich.logging.annotation.DefaultLogging;
import com.netcracker.edu.kulich.logging.annotation.Logging;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@DefaultLogging
@RestController
public class ProcessorController {
    private WebClient webClient;
    private Transformator transformator;

    @Autowired
    public ProcessorController(WebClient defaultWebClient, Transformator transformator) {
        this.webClient = defaultWebClient;
        this.transformator = transformator;
    }

    @GetMapping(value = "/offers")
    @ResponseStatus(HttpStatus.OK)
    @Logging(startMessage = "Getting all offers...", endMessage = "All offers received.", startFromNewLine = true)
    public List<OfferDTO> getAllOffers() {
        return webClient.getAllOffers();
    }

    @GetMapping(value = "/offers/{id:[\\d]+}")
    @ResponseStatus(HttpStatus.OK)
    @Logging(startMessage = "Getting offer by id...", endMessage = "Offer received.", startFromNewLine = true)
    public OfferDTO getOfferById(@PathVariable("id") Long id) {
        return webClient.getOfferById(id);
    }

    @GetMapping(value = "/customers")
    @ResponseStatus(HttpStatus.OK)
    @Logging(startMessage = "Getting all customers...", endMessage = "All customers received.", startFromNewLine = true)
    public List<CustomerDTO> getAllCustomers() {
        return webClient.getAllCustomers();
    }

    @GetMapping(value = "/customers/{email}")
    @ResponseStatus(HttpStatus.OK)
    @Logging(startMessage = "Getting customer by email...", endMessage = "Customer received.", startFromNewLine = true)
    public CustomerDTO getCustomerByEmail(@PathVariable("email") String email) {
        return webClient.getCustomerByEmail(email);
    }

    @PostMapping(value = "/customers")
    @ResponseStatus(HttpStatus.CREATED)
    @Logging(startMessage = "Signing up with e-mail...", endMessage = "Customer signed up.", startFromNewLine = true)
    public CustomerDTO signUpWithEmail(@RequestBody CustomerDTO customerDTO) {
        return webClient.signUpWithEmail(customerDTO);
    }

    @PostMapping(value = "/orders")
    @ResponseStatus(HttpStatus.CREATED)
    @Logging(startMessage = "Creating order with e-mail, date, and list of offers...", endMessage = "Order created.", startFromNewLine = true)
    public OrderDTO createOrder(@RequestBody SimplifiedOrderDTO simplifiedOrder) {
        CustomerDTO customer = webClient.getCustomerByEmail(simplifiedOrder.getEmail());

        Set<OfferDTO> offers = webClient.getOffersByIds(simplifiedOrder.getItemIds());

        Set<OrderItemDTO> items = offers.stream().map(transformator::convertOfferDtoToOrderItemDto).collect(Collectors.toSet());

        OrderDTO orderDTO = new OrderDTO(simplifiedOrder.getDate(), customer.getEmail(), items);

        return webClient.saveOrder(orderDTO);
    }

    @GetMapping(value = "/orders/{id:[\\d]+}")
    @ResponseStatus(HttpStatus.OK)
    @Logging(startMessage = "Getting order by id...", endMessage = "Order received.", startFromNewLine = true)
    public OrderDTO getOrderById(@PathVariable("id") Long id) {
        return webClient.getOrderById(id);
    }

    @GetMapping(value = "/orders")
    @ResponseStatus(HttpStatus.OK)
    @Logging(startMessage = "Getting all orders...", endMessage = "Orders received.", startFromNewLine = true)
    public List<OrderDTO> getAllOrders(@RequestParam(required = false) String email,
                                       @RequestParam(required = false) String status) {
        if (email != null) {
            return getAllOrdersByEmail(email);
        }
        if (status != null) {
            return getAllOrdersByPaymentStatus(status);
        }
        return webClient.getAllOrders();
    }

    @PutMapping(value = "/orders/{id:[\\d]+}/items")
    @ResponseStatus(HttpStatus.OK)
    @Logging(startMessage = "Adding order item to order...", endMessage = "Order item added.", startFromNewLine = true)
    public OrderDTO addItemToOrder(@RequestBody Long itemId, @PathVariable("id") Long id) {
        return webClient.addItemToOrder(id, itemId);
    }

    @DeleteMapping(value = "/orders/{id:[\\d]+}/items/{itemId:[\\d]+}")
    @ResponseStatus(HttpStatus.OK)
    @Logging(startMessage = "Deleting order item from order...", endMessage = "Order item deleted.", startFromNewLine = true)
    public OrderDTO removeItemFromOrder(@PathVariable("id") Long id, @PathVariable("itemId") Long itemId) {
        return webClient.removeItemFromOrder(id, itemId);
    }

    @PostMapping(value = "/orders/{id:[\\d]+}/pay")
    @ResponseStatus(HttpStatus.OK)
    @Logging(startMessage = "Paying for order...", endMessage = "Order is paid.", startFromNewLine = true)
    public OrderDTO payForOrder(@PathVariable("id") Long id) {
        return webClient.payForOrder(id);
    }


    @Logging(startMessage = "Getting all orders by payment status...", endMessage = "All orders received.")
    public List<OrderDTO> getAllOrdersByPaymentStatus(String status) {
        return webClient.getAllOrdersByPaymentStatus(status);
    }

    @Logging(startMessage = "Getting all orders by e-mail...", endMessage = "All orders received.")
    public List<OrderDTO> getAllOrdersByEmail(String email) {
        return webClient.getAllOrdersByEmail(email);
    }

    @GetMapping(value = "/orders/{email}/amount")
    @ResponseStatus(HttpStatus.OK)
    @Logging(startMessage = "Getting amount of items, purchased by customer...", endMessage = "Amount of items, purchased by customer, received.", startFromNewLine = true)
    public String getAmountOfItemsPurchasedByCustomerWithEmail(@PathVariable("email") String email) {
        Integer amount = webClient.getAmountOfItemsPurchasedByCustomerWithEmail(email);
        return "The number of offers purchased by the customer with e-mail: \"" + email + "\" is: " + amount + ";";
    }

    @GetMapping(value = "/orders/{email}/price")
    @ResponseStatus(HttpStatus.OK)
    @Logging(startMessage = "Getting full price of items, purchased by customer...", endMessage = "Full price of items, purchased by customer, received.", startFromNewLine = true)
    public String GetFullPriceOfItemsBoughtByCustomerWithEmail(@PathVariable("email") String email) {
        Double fullPrice = webClient.GetFullPriceOfItemsBoughtByCustomerWithEmail(email);
        return "The total price of goods purchased by the customer with e-mail: \"" + email + "\" is: " + String.format("%.2f", fullPrice) + " Belorussian rubles;";
    }

    @PostMapping(value = "/orders/{id:[\\d]+}/nextStatus")
    @ResponseStatus(HttpStatus.OK)
    @Logging(startMessage = "Setting next order status...", endMessage = "Next order status installed.", startFromNewLine = true)
    public OrderDTO setNextOrderStatus(@PathVariable("id") Long id) {
        return webClient.setNextOrderStatus(id);
    }

    @PostMapping(value = "/orders/{id:[\\d]+}/cancel")
    @ResponseStatus(HttpStatus.OK)
    @Logging(startMessage = "Cancelling order...", endMessage = "Order canceled.", startFromNewLine = true)
    public OrderDTO cancelOrder(@PathVariable("id") Long id) {
        return webClient.cancelOrder(id);
    }
}
