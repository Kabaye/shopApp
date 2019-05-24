package com.netcracker.edu.kulich.customer.dto.transformator;

import com.netcracker.edu.kulich.customer.dto.CustomerDTO;
import com.netcracker.edu.kulich.customer.entity.Customer;
import org.springframework.stereotype.Component;

@Component
public class Transformator {

    public Customer convertToEntity(CustomerDTO customerDTO) {
        Customer customer = new Customer();
        customer.setAge(customerDTO.getAge());
        customer.setFio(customerDTO.getFio());
        customer.setEmail(customerDTO.getEmail());
        return customer;
    }

    public CustomerDTO convertToDto(Customer customer) {
        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setAge(customer.getAge());
        customerDTO.setFio(customer.getFio());
        customerDTO.setEmail(customer.getEmail());
        return customerDTO;
    }

}
