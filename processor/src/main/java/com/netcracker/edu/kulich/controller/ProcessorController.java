package com.netcracker.edu.kulich.controller;

import com.netcracker.edu.kulich.controller.client.WebClient;
import com.netcracker.edu.kulich.dto.CustomerDTO;
import com.netcracker.edu.kulich.dto.OfferDTO;
import com.netcracker.edu.kulich.dto.OrderDTO;
import com.netcracker.edu.kulich.dto.SimplifiedOrderDTO;
import com.netcracker.edu.kulich.logging.DefaultLogging;
import com.netcracker.edu.kulich.logging.Logging;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@DefaultLogging
@RestController
@NoArgsConstructor
public class ProcessorController {
    private WebClient defaultWebClient;

    @Autowired
    public ProcessorController(WebClient defaultWebClient) {
        this.defaultWebClient = defaultWebClient;
    }

    @GetMapping(value = "/offers")
    @ResponseStatus(HttpStatus.OK)
    @Logging(startMessage = "Getting all offers...", endMessage = "All offers received.", startFromNewLine = true)
    public List<OfferDTO> getAllOffers() {
        return defaultWebClient.getAllOffers();
    }

    @GetMapping(value = "/offers/{id:[\\d]+}")
    @ResponseStatus(HttpStatus.OK)
    @Logging(startMessage = "Getting offer by id...", endMessage = "Offer received.", startFromNewLine = true)
    public OfferDTO getOfferById(@PathVariable("id") Long id) {
        return defaultWebClient.getOfferById(id);
    }

    @GetMapping(value = "/customers")
    @ResponseStatus(HttpStatus.OK)
    @Logging(startMessage = "Getting all customers...", endMessage = "All customers received.", startFromNewLine = true)
    public List<CustomerDTO> getAllCustomers() {
        return defaultWebClient.getAllCustomers();
    }

    @GetMapping(value = "/customers/{email}")
    @ResponseStatus(HttpStatus.OK)
    @Logging(startMessage = "Getting customer by email...", endMessage = "Customer received.", startFromNewLine = true)
    public CustomerDTO getCustomerByEmail(@PathVariable("email") String email) {
        return defaultWebClient.getCustomerByEmail(email);
    }

    @PostMapping(value = "/customers")
    @ResponseStatus(HttpStatus.CREATED)
    @Logging(startMessage = "Signing up with e-mail...", endMessage = "Customer signed up.", startFromNewLine = true)
    public CustomerDTO signUpWithEmail(@RequestBody CustomerDTO customerDTO) {
        return defaultWebClient.signUpWithEmail(customerDTO);
    }

    @PostMapping(value = "/orders")
    @ResponseStatus(HttpStatus.CREATED)
    @Logging(startMessage = "Creating order with e-mail, date, and list of offers...", endMessage = "Order created.", startFromNewLine = true)
    public OrderDTO createOrder(@RequestBody SimplifiedOrderDTO simplifiedOrderDTO) {
        return defaultWebClient.createOrder(simplifiedOrderDTO);
    }

    @GetMapping(value = "/orders/{id:[\\d]+}")
    @ResponseStatus(HttpStatus.OK)
    @Logging(startMessage = "Getting order by id...", endMessage = "Order received.", startFromNewLine = true)
    public OrderDTO getOrderById(@PathVariable("id") Long id) {
        return defaultWebClient.getOrderById(id);
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
        return defaultWebClient.getAllOrders();
    }

    @PutMapping(value = "/orders/{id:[\\d]+}/items")
    @ResponseStatus(HttpStatus.OK)
    @Logging(startMessage = "Adding order item to order...", endMessage = "Order item added.", startFromNewLine = true)
    public OrderDTO addItemToOrder(@RequestBody Long itemId, @PathVariable("id") Long id) {
        return defaultWebClient.addItemToOrder(id, itemId);
    }

    @DeleteMapping(value = "/orders/{id:[\\d]+}/items/{itemId:[\\d]+}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Logging(startMessage = "Deleting order item from order...", endMessage = "Order item deleted.", startFromNewLine = true)
    public void removeItemFromOrder(@PathVariable("id") Long id, @PathVariable("itemId") Long itemId) {
        defaultWebClient.removeItemFromOrder(id, itemId);
    }

    @PostMapping(value = "/orders/{id:[\\d]+}/pay")
    @ResponseStatus(HttpStatus.OK)
    @Logging(startMessage = "Paying for order...", endMessage = "Order is paid.", startFromNewLine = true)
    public OrderDTO payForOrder(@PathVariable("id") Long id) {
        return defaultWebClient.payForOrder(id);
    }


    @Logging(startMessage = "Getting all orders by payment status...", endMessage = "All orders received.")
    public List<OrderDTO> getAllOrdersByPaymentStatus(String status) {
        return defaultWebClient.getAllOrdersByPaymentStatus(status);
    }

    @Logging(startMessage = "Getting all orders by e-mail...", endMessage = "All orders received.")
    public List<OrderDTO> getAllOrdersByEmail(String email) {
        return defaultWebClient.getAllOrdersByEmail(email);
    }

    @GetMapping(value = "/orders/{email}/amount")
    @ResponseStatus(HttpStatus.OK)
    @Logging(startMessage = "Getting amount of items, purchased by customer...", endMessage = "Amount of items, purchased by customer, received.", startFromNewLine = true)
    public String getAmountOfItemsPurchasedByCustomerWithEmail(@PathVariable("email") String email) {
        Integer amount = defaultWebClient.getAmountOfItemsPurchasedByCustomerWithEmail(email);
        return "The number of offers purchased by the customer with e-mail: \"" + email + "\" is: " + amount + ";";
    }

    @GetMapping(value = "/orders/{email}/price")
    @ResponseStatus(HttpStatus.OK)
    @Logging(startMessage = "Getting full price of items, purchased by customer...", endMessage = "Full price of items, purchased by customer, received.", startFromNewLine = true)
    public String GetFullPriceOfItemsBoughtByCustomerWithEmail(@PathVariable("email") String email) {
        Double fullPrice = defaultWebClient.GetFullPriceOfItemsBoughtByCustomerWithEmail(email);
        return "The total price of goods purchased by the customer with e-mail: \"" + email + "\" is: " + String.format("%.2f", fullPrice) + " Belorussian rubles;";
    }

    @PostMapping(value = "/orders/{id:[\\d]+}/nextStatus")
    @ResponseStatus(HttpStatus.OK)
    @Logging(startMessage = "Setting next order status...", endMessage = "Next order status installed.", startFromNewLine = true)
    public OrderDTO setNextOrderStatus(@PathVariable("id") Long id) {
        return defaultWebClient.setNextOrderStatus(id);
    }

    @PostMapping(value = "/orders/{id:[\\d]+}/cancel")
    @ResponseStatus(HttpStatus.OK)
    @Logging(startMessage = "Cancelling order...", endMessage = "Order canceled.", startFromNewLine = true)
    public OrderDTO cancelOrder(@PathVariable("id") Long id) {
        return defaultWebClient.cancelOrder(id);
    }
}
