package com.netcracker.edu.kulich.dao;

import com.netcracker.edu.kulich.entity.Customer;
import com.netcracker.edu.kulich.utils.PostgreSQLEntityManagerFactory;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import java.util.List;


public class CustomerDAOManager implements CustomerDAO {
    private EntityManagerFactory entityManagerFactory = PostgreSQLEntityManagerFactory.getEntityManagerFactory();
    private EntityManager entityManager;

    @Override
    public Customer create(Customer customer) {
        entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        customer = entityManager.merge(customer);
        transaction.commit();
        entityManager.close();
        return customer;
    }

    @Override
    public Customer read(Long id) {
        entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        Customer foundCustomer = entityManager.find(Customer.class, id);
        transaction.commit();
        entityManager.close();
        return foundCustomer;
    }

    @Override
    public List<Customer> findAll() {
        entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        List<Customer> customers;
        transaction.begin();
        customers = entityManager.createQuery("SELECT elem FROM Customer elem ORDER BY elem.id", Customer.class).getResultList();
        transaction.commit();
        entityManager.close();
        return customers;
    }

    @Override
    public Customer update(Customer customer) {
        entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction;
        transaction = entityManager.getTransaction();
        transaction.begin();
        entityManager.merge(customer);
        transaction.commit();
        entityManager.close();
        return customer;
    }

    @Override
    public void delete(Long id) {
        entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        Customer customer = entityManager.getReference(Customer.class, id);
        entityManager.remove(customer);
        transaction.commit();
        entityManager.close();
    }
}
