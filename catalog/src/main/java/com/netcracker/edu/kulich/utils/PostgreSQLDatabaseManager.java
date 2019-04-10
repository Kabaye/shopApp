package com.netcracker.edu.kulich.utils;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class PostgreSQLDatabaseManager implements DatabaseManager {
    private static final EntityManagerFactory ENTITY_MANAGER_FACTORY = Persistence.createEntityManagerFactory("shopDatabasePersistenceUnit");

    private EntityManager entityManager;

    private PostgreSQLDatabaseManager() {
        entityManager = ENTITY_MANAGER_FACTORY.createEntityManager();
    }

    private final static DatabaseManager INSTANCE = new PostgreSQLDatabaseManager();

    public static DatabaseManager getInstance() {
        return PostgreSQLDatabaseManager.INSTANCE;
    }

    public EntityManager getEntityManager() {
        return entityManager;
    }
}
