package com.github.glowstone.io.core.persistence;

import com.github.glowstone.io.core.configs.DefaultConfig;
import com.github.glowstone.io.core.configs.interfaces.Configuration;
import com.google.common.base.Preconditions;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.service.ServiceRegistry;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PersistenceService {

    private static PersistenceService instance;
    private final Configuration config;
    private final String persistenceUnitName;

    private EntityManager entityManager;

    /**
     * PersistenceService constructor
     *
     * @param config              Configuration
     * @param persistenceUnitName String
     */
    public PersistenceService(Configuration config, String persistenceUnitName) {
        Preconditions.checkNotNull(config);

        instance = this;
        this.config = config;
        this.persistenceUnitName = persistenceUnitName;
        this.initialize();
    }

    /**
     * @return PersistenceService
     */
    public static PersistenceService getInstance() {
        return instance;
    }

    /**
     * @return EntityManager
     */
    public EntityManager getEntityManager() {
        return this.entityManager;
    }

    /**
     * Initialize the database connection
     */
    private void initialize() {

        String type = this.config.get().getNode(DefaultConfig.DATABASE_SETTINGS, "type").getString("");
        String url = this.config.get().getNode(DefaultConfig.DATABASE_SETTINGS, "url").getString("");
        String database = this.config.get().getNode(DefaultConfig.DATABASE_SETTINGS, "database").getString("");
        String username = this.config.get().getNode(DefaultConfig.DATABASE_SETTINGS, "username").getString("");
        String password = this.config.get().getNode(DefaultConfig.DATABASE_SETTINGS, "password").getString("");

        Preconditions.checkArgument(!type.isEmpty());
        Preconditions.checkArgument(!url.isEmpty());
        Preconditions.checkArgument(!database.isEmpty());
        Preconditions.checkArgument(!username.isEmpty());
        Preconditions.checkArgument(!password.isEmpty());

        // Dynamically load the property file based on type
        Properties properties = new Properties();
        try (InputStream resource = getClass().getClassLoader().getResourceAsStream(type.toLowerCase() + ".properties")) {
            properties.load(resource);
        } catch (IOException e) {
            e.printStackTrace();
        }

        properties.setProperty("hibernate.connection.url", url + database);
        properties.setProperty("hibernate.connection.username", username);
        properties.setProperty("hibernate.connection.password", password);

        try {
            Class.forName(properties.getProperty("hibernate.connection.driver_class"));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        org.hibernate.cfg.Configuration configuration = new org.hibernate.cfg.Configuration();
        configuration.addProperties(properties);

        ServiceRegistry registry = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties()).build();
        configuration.buildSessionFactory(registry);

        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory(this.persistenceUnitName, properties);
        entityManager = entityManagerFactory.createEntityManager();
    }

}
