package com.netcracker.edu.kulich.junit;

import com.netcracker.edu.kulich.dao.CustomerDAO;
import com.netcracker.edu.kulich.dao.CustomerDAOManager;
import com.netcracker.edu.kulich.entity.Customer;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class CustomerTests {
    private CustomerDAO customerDAO = new CustomerDAOManager();

    @Test
    public void createAndReadTwoCustomersTest() {
        Customer customer = new Customer();
        customer.setFio("FIO1");
        customer.setAge(100);

        customerDAO.create(customer);

        customer = new Customer();
        customer.setFio("FIO2");
        customer.setAge(80);

        customer = customerDAO.create(customer);

        Customer customer1 = customerDAO.read(customer.getId());

        assertEquals(customer.toString(), customer1.toString());
    }

    @Test
    public void findAllCustomersTest() {
        List<Customer> customers = customerDAO.findAll();

        Customer customer = new Customer();
        customer.setFio("FIO1");
        customer.setAge(100);

        customer = customerDAO.create(customer);
        customers.add(customer);

        customer = new Customer();
        customer.setFio("FIO2");
        customer.setAge(80);

        customer = customerDAO.create(customer);
        customers.add(customer);

        List<Customer> customers1 = customerDAO.findAll();

        assertEquals(customers.toString(), customers1.toString());
    }

    @Test
    public void updateCustomerTest() {
        Customer customer = new Customer();
        customer.setFio("FIO1");
        customer.setAge(100);

        customer = customerDAO.create(customer);

        Customer customer1 = new Customer();
        customer1.setFio("FIO2");
        customer1.setAge(80);

        customer1 = customerDAO.create(customer1);

        customer1.setAge(500);

        customer.setFio("new FIO1");

        assertEquals(customer.toString() + customer1.toString(), customerDAO.update(customer).toString() + customerDAO.update(customer1).toString());
    }

    @Test
    public void deleteCustomerTest() {
        Customer customer = new Customer();
        customer.setFio("FIO1");
        customer.setAge(100);

        customer = customerDAO.create(customer);

        Customer customer1 = new Customer();
        customer1.setFio("FIO2");
        customer1.setAge(80);

        customer1 = customerDAO.create(customer1);

        customerDAO.delete(customer1.getId());
        customerDAO.delete(customer.getId());
        assertNull(customerDAO.read(customer.getId()));
        assertNull(customerDAO.read(customer1.getId()));
    }
}
