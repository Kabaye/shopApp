package com.netcracker.edu.kulich.controller;

import com.netcracker.edu.kulich.controller.client.WebClient;
import com.netcracker.edu.kulich.dto.CustomerDTO;
import com.netcracker.edu.kulich.dto.OfferDTO;
import com.netcracker.edu.kulich.dto.OrderDTO;
import com.netcracker.edu.kulich.dto.SimplifiedOrderDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ProcessorController {
    private final WebClient webClient;

    @Autowired
    public ProcessorController(WebClient webClient) {
        this.webClient = webClient;
    }

    @GetMapping(value = "/offers")
    @ResponseStatus(HttpStatus.OK)
    public List<OfferDTO> getAllOffers() {
        return webClient.getAllOffers();
    }

    @GetMapping(value = "/offers/{id:[\\d]+}")
    @ResponseStatus(HttpStatus.OK)
    public OfferDTO getOfferById(@PathVariable("id") Long id) {
        return webClient.getOfferById(id);
    }

    @GetMapping(value = "/customers")
    @ResponseStatus(HttpStatus.OK)
    public List<CustomerDTO> getAllCustomers() {
        return webClient.getAllCustomers();
    }

    @GetMapping(value = "/customers/{email}")
    @ResponseStatus(HttpStatus.OK)
    public CustomerDTO getCustomerById(@PathVariable("email") String email) {
        return webClient.getCustomerByEmail(email);
    }

    @PostMapping(value = "/customers")
    @ResponseStatus(HttpStatus.CREATED)
    public CustomerDTO signUp(@RequestBody CustomerDTO customerDTO) {
        return webClient.signUpWithEmail(customerDTO);
    }

    @PostMapping(value = "/orders")
    @ResponseStatus(HttpStatus.CREATED)
    public OrderDTO createOrder(@RequestBody SimplifiedOrderDTO simplifiedOrderDTO) {
        return webClient.createOrder(simplifiedOrderDTO);
    }

    @GetMapping(value = "/orders/{id:[\\d]+}")
    @ResponseStatus(HttpStatus.OK)
    public OrderDTO getOrderById(@PathVariable("id") Long id) {
        return webClient.getOrderById(id);
    }

    @GetMapping(value = "/orders")
    @ResponseStatus(HttpStatus.OK)
    public List<OrderDTO> getAllOrders() {
        return webClient.getAllOrders();
    }

    @PutMapping(value = "/orders/{id:[\\d]+}/items")
    @ResponseStatus(HttpStatus.OK)
    public OrderDTO addItemToOrder(@RequestBody Long itemId, @PathVariable("id") Long id) {
        return webClient.addItemToOrder(id, itemId);
    }

    @DeleteMapping(value = "/orders/{id:[\\d]+}/items/{itemId:[\\d]+}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeItemFromOrder(@PathVariable("id") Long id, @PathVariable("itemId") Long itemId) {
        webClient.removeItemFromOrder(id, itemId);
    }

    @PutMapping(value = "/orders/{id:[\\d]+}/pay")
    @ResponseStatus(HttpStatus.OK)
    public OrderDTO payForOrder(@PathVariable("id") Long id) {
        return webClient.payForOrder(id);
    }


    @GetMapping(value = "/statuses/{payStatus}")
    @ResponseStatus(HttpStatus.OK)
    public List<OrderDTO> getAllOrdersByPaymentStatus(@PathVariable("payStatus") String status) {
        return webClient.getAllOrdersByPaymentStatus(status);
    }

    @GetMapping(value = "/emails/{email}")
    @ResponseStatus(HttpStatus.OK)
    public List<OrderDTO> getAllOrdersByEmail(@PathVariable("email") String email) {
        return webClient.getAllOrdersByEmail(email);
    }

    @GetMapping(value = "/emails/{email}/amount")
    @ResponseStatus(HttpStatus.OK)
    public String getAmountOfItemsBoughtByCustomerWithEmail(@PathVariable("email") String email) {
        Integer amount = webClient.getAmountOfItemsBoughtByCustomerWithEmail(email);
        return "The number of offers purchased by the customer with e-mail: \"" + email + "\" is: " + amount + ";";
    }

    @GetMapping(value = "/emails/{email}/price")
    @ResponseStatus(HttpStatus.OK)
    public String GetFullPriceOfItemsBoughtByCustomerWithEmail(@PathVariable("email") String email) {
        Double fullPrice = webClient.GetFullPriceOfItemsBoughtByCustomerWithEmail(email);
        return "The total price of goods purchased by the customer with e-mail: \"" + email + "\" is: " + String.format("%.2f", fullPrice) + " Belorussian rubles;";
    }

    @PutMapping(value = "/orders/{id:[\\d]+}/status/next")
    @ResponseStatus(HttpStatus.OK)
    public OrderDTO setNextOrderStatus(@PathVariable("id") Long id) {
        return webClient.setNextOrderStatus(id);
    }
}
