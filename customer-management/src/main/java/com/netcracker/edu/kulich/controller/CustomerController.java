package com.netcracker.edu.kulich.controller;

import com.netcracker.edu.kulich.dto.CustomerDTO;
import com.netcracker.edu.kulich.dto.transformator.Transformator;
import com.netcracker.edu.kulich.entity.Customer;
import com.netcracker.edu.kulich.exception.controller.CustomerControllerException;
import com.netcracker.edu.kulich.logging.DefaultLogging;
import com.netcracker.edu.kulich.logging.Logging;
import com.netcracker.edu.kulich.service.CustomerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@DefaultLogging
@RequestMapping("/customers")
public class CustomerController {
    private Transformator transformator;
    private CustomerService customerService;
    private Logger logger = LoggerFactory.getLogger(CustomerController.class);

    @Autowired
    public CustomerController(Transformator transformator, CustomerService customerService) {
        this.transformator = transformator;
        this.customerService = customerService;
    }

    @GetMapping(value = "/{email}")
    @Logging(startMessage = "Request for getting customer by id is received.", endMessage = "Response on request for getting customer by id is sent.", startFromNewLine = true)
    public ResponseEntity<CustomerDTO> getCustomer(@PathVariable("email") String email) {
        Customer customer = customerService.getCustomerById(email);
        if (customer == null) {
            logger.error("Customer is not found.");
            throw new CustomerControllerException("Customer with e-mail: \'" + email + "\' doesn't exist. Please, sign up, if you want to buy offers.");
        }
        return new ResponseEntity<>(transformator.convertToDto(customer), HttpStatus.OK);
    }

    @PostMapping
    @Logging(startMessage = "Request for saving customer is received.", endMessage = "Response on request for saving customer is sent.", startFromNewLine = true)
    public ResponseEntity<CustomerDTO> saveCustomer(@RequestBody CustomerDTO customerDTO) {
        Customer customer = customerService.saveCustomer(transformator.convertToEntity(customerDTO));
        return new ResponseEntity<>(transformator.convertToDto(customer), HttpStatus.CREATED);
    }

    @DeleteMapping(value = "/{email}")
    @Logging(startMessage = "Request for deleting customer is received.", endMessage = "Response on request for deleting customer is sent.", startFromNewLine = true)
    public ResponseEntity deleteCustomer(@PathVariable("email") String email) {
        customerService.deleteCustomerById(email);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping
    @Logging(startMessage = "Request for getting all customers is received.", endMessage = "Response on request for getting all customers is sent.", startFromNewLine = true)
    public ResponseEntity<List<CustomerDTO>> getAll() {
        List<Customer> customers = customerService.findAllCustomers();
        List<CustomerDTO> customerDTOs = customers.stream()
                .map(transformator::convertToDto)
                .collect(Collectors.toList());
        return new ResponseEntity<>(customerDTOs, HttpStatus.OK);
    }

    @PutMapping(value = "/{email}")
    @Logging(startMessage = "Request for updating customer is received.", endMessage = "Response on request for updating customer is sent.", startFromNewLine = true)
    public ResponseEntity<CustomerDTO> updateCustomer(@RequestBody CustomerDTO customerDTO, @PathVariable("email") String email) {
        Customer customer = transformator.convertToEntity(customerDTO);
        customer.setEmail(email);
        customer = customerService.updateCustomer(customer);
        return new ResponseEntity<>(transformator.convertToDto(customer), HttpStatus.OK);
    }
}