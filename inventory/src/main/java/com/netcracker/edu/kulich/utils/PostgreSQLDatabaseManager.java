package com.netcracker.edu.kulich.utils;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class PostgreSQLDatabaseManager implements DatabaseManager {
    private static final EntityManagerFactory ENTITY_MANAGER_FACTORY = Persistence.createEntityManagerFactory("ordersPersistenceUnit");

    private EntityManager entityManager;

    private PostgreSQLDatabaseManager() {
        entityManager = ENTITY_MANAGER_FACTORY.createEntityManager();
    }

    public static EntityManagerFactory getDatabaseManagerFactory() {
        return ENTITY_MANAGER_FACTORY;
    }

    public EntityManager getEntityManager() {
        return entityManager;
    }
}
