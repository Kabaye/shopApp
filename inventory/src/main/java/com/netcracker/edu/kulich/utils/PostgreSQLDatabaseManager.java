package com.netcracker.edu.kulich.utils;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class PostgreSQLDatabaseManager {
    private static final EntityManagerFactory ENTITY_MANAGER_FACTORY = Persistence.createEntityManagerFactory("ordersPersistenceUnit");

    public static EntityManagerFactory getDatabaseManagerFactory() {
        return ENTITY_MANAGER_FACTORY;
    }
}
