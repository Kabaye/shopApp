package com.netcracker.edu.kulich;

import com.netcracker.edu.kulich.dao.CustomerDAO;
import com.netcracker.edu.kulich.entity.Customer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class CustomerManagementApplication {
    public static void main(String[] args) {
        ApplicationContext applicationContext = SpringApplication.run(CustomerManagementApplication.class, args);
        CustomerDAO customerDAO = applicationContext.getBean("customerDAO", CustomerDAO.class);

        Customer c = new Customer();
        c.setFio("Hello 1");
        c.setAge(125);

        Customer c1 = new Customer();
        c1.setFio("Hello 2");
        c1.setAge(75);

        customerDAO.save(c);

        customerDAO.save(c1);
        System.out.println(customerDAO.findAll() + "        <- nice");
    }
}
