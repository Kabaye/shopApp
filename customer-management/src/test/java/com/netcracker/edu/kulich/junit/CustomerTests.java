package com.netcracker.edu.kulich.junit;

import com.netcracker.edu.kulich.entity.Customer;
import com.netcracker.edu.kulich.service.DefaultCustomerService;
import com.netcracker.edu.kulich.service.exception.CustomerServiceException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CustomerTests {

    @Autowired
    private DefaultCustomerService customerService;

    @Test
    public void createAndReadTwoCustomersTest() throws CustomerServiceException {
        Customer customer = new Customer();
        customer.setFio("FIO1");
        customer.setAge(100);

        customerService.saveCustomer(customer);

        customer = new Customer();
        customer.setFio("FIO2");
        customer.setAge(80);
        customer = customerService.saveCustomer(customer);

        Customer customer1 = customerService.getCustomerById(customer.getId());

        assertEquals(customer, customer1);
    }

    @Test
    public void findAllCustomersTest() throws CustomerServiceException {
        List<Customer> customers = customerService.findAllCustomers();

        Customer customer = new Customer();
        customer.setFio("FIO1");
        customer.setAge(100);
        customer = customerService.saveCustomer(customer);
        customers.add(customer);

        customer = new Customer();
        customer.setFio("FIO2");
        customer.setAge(80);
        customer = customerService.saveCustomer(customer);
        customers.add(customer);

        List<Customer> customers1 = customerService.findAllCustomers();

        assertArrayEquals(customers.toArray(), customers1.toArray());
    }

    @Test
    public void updateCustomerTest() throws CustomerServiceException {
        Customer customer = new Customer();
        customer.setFio("FIO1");
        customer.setAge(100);
        customer = customerService.saveCustomer(customer);

        Customer customer1 = new Customer();
        customer1.setFio("FIO2");
        customer1.setAge(80);
        customer1 = customerService.saveCustomer(customer1);

        customer1.setAge(60);

        customer.setFio("new FIO1");

        Customer customer2;
        Customer customer3;

        customerService.updateCustomer(customer);
        customerService.updateCustomer(customer1);

        customer2 = customerService.getCustomerById(customer.getId());
        customer3 = customerService.getCustomerById(customer1.getId());

        assertEquals(customer, customer2);
        assertEquals(customer1, customer3);
    }

    @Test
    public void deleteCustomerTest() throws CustomerServiceException {
        Customer customer = new Customer();
        customer.setFio("FIO1");
        customer.setAge(100);

        customer = customerService.saveCustomer(customer);

        Customer customer1 = new Customer();
        customer1.setFio("FIO2");
        customer1.setAge(80);

        customer1 = customerService.saveCustomer(customer1);

        customerService.deleteCustomerById(customer1.getId());
        customerService.deleteCustomerById(customer.getId());

        try {
            customerService.deleteCustomerById(customer.getId());
        } catch (CustomerServiceException exc) {
            if (exc.getMessage().contains("existent"))
                assertTrue(true);
        }
        assertNull(customerService.getCustomerById(customer.getId()));
        assertNull(customerService.getCustomerById(customer1.getId()));
    }

    @Test
    public void testCustomerSaveAndUpdateInvalidData() {
        Customer customer = new Customer();
        customer.setFio("FIO1");
        customer.setAge(152);

        try {
            customerService.saveCustomer(customer);
        } catch (CustomerServiceException exc) {
            assertTrue(true);
        }

        customer = new Customer();
        customer.setFio("");
        customer.setAge(80);

        try {
            customerService.saveCustomer(customer);
        } catch (CustomerServiceException exc) {
            Assert.assertTrue(true);
        }

        customer.setFio("");
        customer.setAge(13);

        try {
            customerService.saveCustomer(customer);
        } catch (CustomerServiceException exc) {
            assertTrue(true);
        }
    }
}
