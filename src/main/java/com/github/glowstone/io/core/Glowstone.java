package com.github.glowstone.io.core;

import com.github.glowstone.io.core.configs.DefaultConfig;
import com.github.glowstone.io.core.entities.SubjectEntity;
import com.github.glowstone.io.core.http.IncomingServer;
import com.github.glowstone.io.core.permissions.GlowstonePermissionService;
import com.google.inject.Inject;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongepowered.api.Game;
import org.spongepowered.api.config.ConfigDir;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.GameReloadEvent;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.event.game.state.GamePreInitializationEvent;
import org.spongepowered.api.event.game.state.GameStoppingEvent;
import org.spongepowered.api.plugin.Plugin;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;

@Plugin(id = Glowstone.ID, name = Glowstone.NAME, version = Glowstone.VERSION, description = Glowstone.DESCRIPTION)
public class Glowstone {

    private static Glowstone instance;
    private IncomingServer server;
    public static final String ID = "glowstone";
    public static final String NAME = "Glowstone";
    public static final String VERSION = "1.0.0";
    public static final String DESCRIPTION = "A Minecraft server plugin that runs on top of Sponge for Glowstone.io integration";

    @Inject
    private Game game;

    @Inject
    private static Logger logger;

    @Inject
    @ConfigDir(sharedRoot = false)
    private File configDir;

    private static DefaultConfig defaultConfig;
    private EntityManager entityManager;

    /**
     * Get the plugin instance
     *
     * @return Glowstone
     */
    public static Glowstone getInstance() {
        return instance;
    }

    /**
     * @return Logger
     */
    public static Logger getLogger() {
        return logger;
    }

    /**
     * @return DefaultConfig
     */
    public static DefaultConfig getDefaultConfig() {
        return defaultConfig;
    }

    /**
     * @return EntityManager
     */
    public EntityManager getEntityManager() {
        return entityManager;
    }

    @Listener
    public void onPreInit(GamePreInitializationEvent event) {
        Glowstone.instance = this;
        Glowstone.logger = LoggerFactory.getLogger(Glowstone.NAME);
        getLogger().info(String.format("Starting up %s v%s", Glowstone.NAME, Glowstone.VERSION));

        // Create configuration directory
        if (!this.configDir.isDirectory()) {
            if (this.configDir.mkdirs()) {
                getLogger().info("configs directory successfully created");
            } else {
                getLogger().error("Unable to create plugin directory, please file permissions.");
                this.game.getServer().shutdown();
            }
        }

        // Load configs
        defaultConfig = new DefaultConfig(this.configDir);
        defaultConfig.load();

        // Setup database
        setupDatabase();
    }

    @Listener
    public void onGameInit(GameInitializationEvent event) {
        server = new IncomingServer();
    }

    @Listener
    public void onGameReload(GameReloadEvent event) {

        // Close db sessions
        entityManager.close();

        // Reload configs
        defaultConfig = new DefaultConfig(this.configDir);
        defaultConfig.load();

        setupDatabase();

        // Reload Server
        server.stop();
        server.start();

        getLogger().info(String.format("%s was reloaded", NAME));
    }

    @Listener
    public void onGameStop(GameStoppingEvent event) {

        if (entityManager != null) {
            entityManager.close();
        }

        if (server != null) {
            server.stop();
        }
    }

    /**
     * Setup the database / hibernate
     */
    private void setupDatabase() {

        String type = getDefaultConfig().get().getNode(DefaultConfig.DATABASE_SETTINGS, "type").getString("H2");
        String url = getDefaultConfig().get().getNode(DefaultConfig.DATABASE_SETTINGS, "url").getString("jdbc:h2:file:." + File.separator + "glowstone" +
                File.separator);
        String database = getDefaultConfig().get().getNode(DefaultConfig.DATABASE_SETTINGS, "database").getString("data");
        String username = getDefaultConfig().get().getNode(DefaultConfig.DATABASE_SETTINGS, "username").getString("");
        String password = getDefaultConfig().get().getNode(DefaultConfig.DATABASE_SETTINGS, "password").getString("");

        if (url.isEmpty() || username.isEmpty() || password.isEmpty()) {
            getLogger().error(String.format("Database is not configured with a username, password and/or url. Check the %s configs.", NAME));
            game.getServer().shutdown();
            return;
        }

        String propertyFile;
        switch (type) {
            case "H2":
                propertyFile = "h2.properties";
                break;

            default:
                propertyFile = null;
                break;
        }

        if (propertyFile == null) {
            Glowstone.getLogger().error(String.format("Database is not configured with an accepted type. Check the %s configs.", NAME));
            game.getServer().shutdown();
            return;
        }

        Properties properties = new Properties();
        try (InputStream resource = getClass().getClassLoader().getResourceAsStream(propertyFile)) {
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

        Configuration configuration = new Configuration();
        configuration.addProperties(properties);

        ServiceRegistry registry = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties()).build();
        configuration.buildSessionFactory(registry);

        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("glowstone", properties);
        entityManager = entityManagerFactory.createEntityManager();

        // TESTING //
        entityManager.getTransaction().begin();
        SubjectEntity player1 = new SubjectEntity(SubjectEntity.SUBJECT_TYPE_USER, "0123456", "silvermmonkey");
        SubjectEntity player2 = new SubjectEntity(SubjectEntity.SUBJECT_TYPE_USER, "98765432", "goldmmonkey");
        SubjectEntity group = new SubjectEntity(SubjectEntity.SUBJECT_TYPE_GROUP, "93084303", GlowstonePermissionService.DEFAULT_GROUP);

        group.getChildren().add(player1);
        group.getChildren().add(player2);

        entityManager.persist(player1);
        entityManager.persist(player2);
        entityManager.persist(group);
        entityManager.getTransaction().commit();

        Query query = entityManager.createNativeQuery("SELECT * FROM subjects", SubjectEntity.class);
        List<SubjectEntity> list = query.getResultList();
        list.forEach(subject -> {
            StringBuilder stringBuilder = new StringBuilder();
            subject.getChildren().forEach(c -> stringBuilder.append(c.getName()).append(", "));
            getLogger().info(subject.getName() + " children: " + stringBuilder.toString());
        });

    }

}
