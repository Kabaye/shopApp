package com.netcracker.edu.kulich.controller;

import com.netcracker.edu.kulich.dto.CustomerDTO;
import com.netcracker.edu.kulich.dto.transformator.Transformator;
import com.netcracker.edu.kulich.entity.Customer;
import com.netcracker.edu.kulich.exception.controller.CustomerControllerException;
import com.netcracker.edu.kulich.service.CustomerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/customers")
public class CustomerController {
    private final String GETTING_NOT_EXISTENT_CUSTOMER = "You try to get not existent customer.";

    private final Transformator transformator;

    private final CustomerService customerService;

    public CustomerController(Transformator transformator, CustomerService customerService) {
        this.transformator = transformator;
        this.customerService = customerService;
    }

    @GetMapping(value = "/{id:[\\d]+}")
    public ResponseEntity<CustomerDTO> getCustomer(@PathVariable("id") Long id) {
        Customer customer = customerService.getCustomerById(id);
        if (customer == null) {
            throw new CustomerControllerException(GETTING_NOT_EXISTENT_CUSTOMER);
        }
        return new ResponseEntity<>(transformator.convertToDto(customer), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<CustomerDTO> saveCustomer(@RequestBody CustomerDTO customerDTO) {
        Customer customer = customerService.saveCustomer(transformator.convertToEntity(customerDTO));
        return new ResponseEntity<>(transformator.convertToDto(customer), HttpStatus.CREATED);
    }

    @DeleteMapping(value = "/{id:[\\d]+}")
    public ResponseEntity deleteCustomer(@PathVariable("id") Long id) {
        customerService.deleteCustomerById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping
    public ResponseEntity<List<CustomerDTO>> getAll() {
        List<Customer> customers = customerService.findAllCustomers();
        List<CustomerDTO> customerDTOs = customers.stream()
                .map(transformator::convertToDto)
                .collect(Collectors.toList());
        return new ResponseEntity<>(customerDTOs, HttpStatus.OK);
    }

    @PutMapping(value = "/{id:[\\d]+}")
    public ResponseEntity<CustomerDTO> updateCustomer(@RequestBody CustomerDTO customerDTO, @PathVariable("id") Long id) {
        Customer customer = transformator.convertToEntity(customerDTO);
        customer.setId(id);
        customer = customerService.updateCustomer(customer);
        return new ResponseEntity<>(transformator.convertToDto(customer), HttpStatus.OK);
    }
}