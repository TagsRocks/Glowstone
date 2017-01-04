package com.github.glowstone.io.core.persistence;

import com.github.glowstone.io.core.configs.DefaultConfig;
import com.github.glowstone.io.core.configs.interfaces.Config;
import com.google.common.base.Preconditions;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PersistenceService {

    private static PersistenceService instance;
    private final Config config;
    private final Configuration configuration;
    private SessionFactory sessionFactory;

    /**
     * PersistenceService constructor
     *
     * @param config Config
     */
    public PersistenceService(Config config, Configuration configuration) {
        Preconditions.checkNotNull(config);
        Preconditions.checkNotNull(configuration);

        instance = this;
        this.config = config;
        this.configuration = configuration;
        this.initialize();
    }

    /**
     * @return PersistenceService
     */
    public static PersistenceService getInstance() {
        return instance;
    }

    /**
     * @return SessionFactory
     */
    public SessionFactory getSessionFactory() {
        return this.sessionFactory;
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

        this.configuration.addProperties(properties);

        ServiceRegistry registry = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties()).build();
        this.sessionFactory = configuration.buildSessionFactory(registry);
    }

}
