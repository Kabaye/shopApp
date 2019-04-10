package com.netcracker.edu.kulich.dao;

import com.netcracker.edu.kulich.entity.Customer;
import com.netcracker.edu.kulich.utils.PostgreSQLDatabaseManager;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.util.List;


public class CustomerDAOImplementation implements CustomerDAO {

    private EntityManager entityManager = PostgreSQLDatabaseManager.getInstance().getEntityManager();

    @Override
    public Customer create(Customer customer) {
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        entityManager.persist(customer);
        transaction.commit();
        return customer;
    }

    @Override
    public Customer read(Long id) {
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        Customer foundCustomer = entityManager.find(Customer.class, id);
        transaction.commit();
        return foundCustomer;
    }

    @Override
    public List<Customer> findAll() {
        EntityTransaction transaction = entityManager.getTransaction();
        List<Customer> customers;
        transaction.begin();
        customers = entityManager.createQuery("SELECT elem FROM Customer elem ORDER BY elem.id", Customer.class).getResultList();
        transaction.commit();
        return customers;
    }

    @Override
    public Customer update(Customer customer) {
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        Customer foundCustomer = entityManager.merge(customer);
        transaction.commit();
        return foundCustomer;
    }

    @Override
    public void delete(Customer customer) {
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        entityManager.remove(customer);
        transaction.commit();
    }
}
