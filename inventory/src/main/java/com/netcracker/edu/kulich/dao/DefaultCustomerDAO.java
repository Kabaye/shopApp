package com.netcracker.edu.kulich.dao;

import com.netcracker.edu.kulich.entity.Customer;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Transactional
@Repository(value = "customerDAO")
public class DefaultCustomerDAO implements CustomerDAO {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Customer create(Customer customer) {
        customer = entityManager.merge(customer);
        return customer;
    }

    @Override
    public Customer read(Long id) {
        Customer foundCustomer = entityManager.find(Customer.class, id);
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
    public void delete(Long id) {
        Customer customer = entityManager.getReference(Customer.class, id);
        entityManager.remove(customer);
    }
}
