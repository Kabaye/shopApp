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
    private final Transformator transformator;

    private final CustomerService customerService;

    public CustomerController(Transformator transformator, CustomerService customerService) {
        this.transformator = transformator;
        this.customerService = customerService;
    }

    @GetMapping(value = "/{email}")
    public ResponseEntity<CustomerDTO> getCustomer(@PathVariable("email") String email) {
        Customer customer = customerService.getCustomerById(email);
        if (customer == null) {
            throw new CustomerControllerException("Customer with e-mail: \'" + email + "\' doesn't exist.");
        }
        return new ResponseEntity<>(transformator.convertToDto(customer), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<CustomerDTO> saveCustomer(@RequestBody CustomerDTO customerDTO) {
        Customer customer = customerService.saveCustomer(transformator.convertToEntity(customerDTO));
        return new ResponseEntity<>(transformator.convertToDto(customer), HttpStatus.CREATED);
    }

    @DeleteMapping(value = "/{email}")
    public ResponseEntity deleteCustomer(@PathVariable("email") String email) {
        customerService.deleteCustomerById(email);
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

    @PutMapping(value = "/{email}")
    public ResponseEntity<CustomerDTO> updateCustomer(@RequestBody CustomerDTO customerDTO, @PathVariable("email") String email) {
        Customer customer = transformator.convertToEntity(customerDTO);
        customer.setEmail(email);
        customer = customerService.updateCustomer(customer);
        return new ResponseEntity<>(transformator.convertToDto(customer), HttpStatus.OK);
    }
}