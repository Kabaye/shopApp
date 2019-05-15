package com.netcracker.edu.kulich.dao;

import com.netcracker.edu.kulich.entity.Customer;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import java.util.List;

@SuppressWarnings("ALL")
@Repository(value = "customerDAO")
public class DefaultCustomerDAO implements CustomerDAO {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Customer save(Customer customer) {
        customer = entityManager.merge(customer);
        return customer;
    }

    @Override
    public Customer getById(String email) {
        Customer foundCustomer = entityManager.find(Customer.class, email);
        return foundCustomer;
    }

    @Override
    public List<Customer> findAll() {
        List<Customer> customers;
        customers = entityManager.createQuery("SELECT elem FROM Customer elem ORDER BY elem.id", Customer.class).getResultList();
        return customers;
    }

    @Override
    public Customer update(Customer customer) {
        customer = entityManager.merge(customer);
        return customer;
    }

    @Override
    public void deleteById(String email) throws EntityNotFoundException {
        Customer customer = entityManager.find(Customer.class, email);
        if (customer == null)
            throw new EntityNotFoundException();
        entityManager.remove(customer);
    }
}
