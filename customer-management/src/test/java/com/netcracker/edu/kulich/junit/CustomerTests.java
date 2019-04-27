package com.netcracker.edu.kulich.junit;

import com.netcracker.edu.kulich.entity.Customer;
import com.netcracker.edu.kulich.exception.InvalidCustomerDataException;
import com.netcracker.edu.kulich.service.CustomerService;
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
    private CustomerService customerService;

    @Test
    public void createAndReadTwoCustomersTest() {
        Customer customer = new Customer();
        customer.setFio("FIO1");
        customer.setAge(100);

        try {
            customerService.save(customer);
        } catch (InvalidCustomerDataException exc) {
            fail();
        }

        customer = new Customer();
        customer.setFio("FIO2");
        customer.setAge(80);
        try {
            customer = customerService.save(customer);
        } catch (InvalidCustomerDataException exc) {
            fail();
        }
        Customer customer1 = customerService.getById(customer.getId());

        assertEquals(customer, customer1);
    }

    @Test
    public void findAllCustomersTest() {
        List<Customer> customers = customerService.findAllCustomers();

        Customer customer = new Customer();
        customer.setFio("FIO1");
        customer.setAge(100);
        try {
            customer = customerService.save(customer);
        } catch (InvalidCustomerDataException exc) {
            fail();
        }
        customers.add(customer);

        customer = new Customer();
        customer.setFio("FIO2");
        customer.setAge(80);
        try {
            customer = customerService.save(customer);
        } catch (InvalidCustomerDataException exc) {
            fail();
        }
        customers.add(customer);

        List<Customer> customers1 = customerService.findAllCustomers();

        assertArrayEquals(customers.toArray(), customers1.toArray());
    }

    @Test
    public void updateCustomerTest() {
        Customer customer = new Customer();
        customer.setFio("FIO1");
        customer.setAge(100);
        try {
            customer = customerService.save(customer);
        } catch (InvalidCustomerDataException exc) {
            fail();
        }
        Customer customer1 = new Customer();
        customer1.setFio("FIO2");
        customer1.setAge(80);
        try {
            customer1 = customerService.save(customer1);
        } catch (InvalidCustomerDataException exc) {
            fail();
        }
        customer1.setAge(500);

        customer.setFio("new FIO1");

        Customer customer2 = null;
        Customer customer3 = null;

        try {
            customer2 = customerService.updateCustomer(customer);
            customer3 = customerService.updateCustomer(customer1);
        } catch (InvalidCustomerDataException exc) {
            fail();
        }
        assertEquals(customer, customer2);
        assertEquals(customer1, customer3);

    }

    @Test
    public void deleteCustomerTest() {
        Customer customer = new Customer();
        customer.setFio("FIO1");
        customer.setAge(100);
        try {
            customer = customerService.save(customer);
        } catch (InvalidCustomerDataException exc) {
            fail();
        }
        Customer customer1 = new Customer();
        customer1.setFio("FIO2");
        customer1.setAge(80);
        try {
            customer1 = customerService.save(customer1);
        } catch (InvalidCustomerDataException exc) {
            fail();
        }
        customerService.deleteCustomerById(customer1.getId());
        customerService.deleteCustomerById(customer.getId());

        assertNull(customerService.getById(customer.getId()));
        assertNull(customerService.getById(customer1.getId()));
    }

    @Test
    public void testCustomerSaveAndUpdateInvalidData() {
        Customer customer = new Customer();
        customer.setFio("FIO1");
        customer.setAge(152);

        try {
            customerService.save(customer);
        } catch (InvalidCustomerDataException exc) {
            if (exc.getMessage().equals(CustomerService.INCORRECT_AGE))
                Assert.assertTrue(true);
            else
                fail();
        }

        customer = new Customer();
        customer.setFio("");
        customer.setAge(80);
        try {
            customerService.save(customer);
        } catch (InvalidCustomerDataException exc) {
            if (exc.getMessage().equals(CustomerService.NULL_FIO))
                Assert.assertTrue(true);
            else
                fail();
        }

        customer.setFio("");
        customer.setAge(13);
        try {
            customerService.save(customer);
        } catch (InvalidCustomerDataException exc) {
            assertTrue(true);
        }


    }
}
