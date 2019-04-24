package com.netcracker.edu.kulich.junit;

import com.netcracker.edu.kulich.dao.CustomerDAO;
import com.netcracker.edu.kulich.entity.Customer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CustomerTests {

    @Autowired
    private CustomerDAO customerDAO;

    @Test
    public void createAndReadTwoCustomersTest() {
        Customer customer = new Customer();
        customer.setFio("FIO1");
        customer.setAge(100);

        customerDAO.save(customer);

        customer = new Customer();
        customer.setFio("FIO2");
        customer.setAge(80);

        customer = customerDAO.save(customer);

        Customer customer1 = customerDAO.getById(customer.getId());

        assertEquals(customer.toString(), customer1.toString());
    }

    @Test
    public void findAllCustomersTest() {
        List<Customer> customers = customerDAO.findAll();

        Customer customer = new Customer();
        customer.setFio("FIO1");
        customer.setAge(100);

        customer = customerDAO.save(customer);
        customers.add(customer);

        customer = new Customer();
        customer.setFio("FIO2");
        customer.setAge(80);

        customer = customerDAO.save(customer);
        customers.add(customer);

        List<Customer> customers1 = customerDAO.findAll();

        assertEquals(customers.toString(), customers1.toString());
    }

    @Test
    public void updateCustomerTest() {
        Customer customer = new Customer();
        customer.setFio("FIO1");
        customer.setAge(100);

        customer = customerDAO.save(customer);

        Customer customer1 = new Customer();
        customer1.setFio("FIO2");
        customer1.setAge(80);

        customer1 = customerDAO.save(customer1);

        customer1.setAge(500);

        customer.setFio("new FIO1");

        assertEquals(customer.toString() + customer1.toString(), customerDAO.update(customer).toString() + customerDAO.update(customer1).toString());
    }

    @Test
    public void deleteCustomerTest() {
        Customer customer = new Customer();
        customer.setFio("FIO1");
        customer.setAge(100);

        customer = customerDAO.save(customer);

        Customer customer1 = new Customer();
        customer1.setFio("FIO2");
        customer1.setAge(80);

        customer1 = customerDAO.save(customer1);

        customerDAO.deleteById(customer1.getId());
        customerDAO.deleteById(customer.getId());
        assertNull(customerDAO.getById(customer.getId()));
        assertNull(customerDAO.getById(customer1.getId()));
    }
}
