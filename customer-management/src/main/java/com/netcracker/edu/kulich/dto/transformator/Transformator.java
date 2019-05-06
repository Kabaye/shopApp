package com.netcracker.edu.kulich.dto.transformator;

import com.netcracker.edu.kulich.dto.CustomerDTO;
import com.netcracker.edu.kulich.entity.Customer;
import org.springframework.stereotype.Component;

@Component
public class Transformator {

    public Customer convertToEntity(CustomerDTO customerDTO) {
        Customer customer = new Customer();
        customer.setAge(customerDTO.getAge());
        customer.setFio(customerDTO.getFio());
        customer.setId(customerDTO.getId());
        return customer;
    }

    public CustomerDTO convertToDto(Customer customer) {
        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setAge(customer.getAge());
        customerDTO.setFio(customer.getFio());
        customerDTO.setId(customer.getId());
        return customerDTO;
    }

}
