package com.github.glowstone.io.core;

import com.github.glowstone.io.core.api.ApiService;
import com.github.glowstone.io.core.api.resources.PlayerResource;
import com.github.glowstone.io.core.api.resources.SubjectResource;
import com.github.glowstone.io.core.api.resources.WorldResource;
import com.github.glowstone.io.core.configs.GlowstoneConfig;
import com.github.glowstone.io.core.entities.*;
import com.github.glowstone.io.core.listeners.PlayerListener;
import com.github.glowstone.io.core.permissions.GlowstonePermissionService;
import com.github.glowstone.io.core.permissions.collections.GroupSubjectCollection;
import com.github.glowstone.io.core.permissions.collections.UserSubjectCollection;
import com.github.glowstone.io.core.permissions.subjects.DefaultSubject;
import com.github.glowstone.io.core.persistence.PersistenceService;
import com.github.glowstone.io.core.persistence.repositories.SubjectEntityRepository;
import com.google.inject.Inject;
import org.glassfish.jersey.server.ResourceConfig;
import org.hibernate.cfg.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.config.ConfigDir;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.GameReloadEvent;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.event.game.state.GamePreInitializationEvent;
import org.spongepowered.api.event.game.state.GameStoppedServerEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.service.permission.PermissionService;
import org.spongepowered.api.service.permission.Subject;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Plugin(id = Glowstone.ID, name = Glowstone.NAME, version = Glowstone.VERSION, description = Glowstone.DESCRIPTION)
public class Glowstone {

    public static final String ID = "glowstone";
    public static final String NAME = "Glowstone";
    public static final String VERSION = "1.0.0";
    public static final String DESCRIPTION = "A Minecraft apiService plugin that runs on top of Sponge for Glowstone.io integration";

    private static Glowstone instance;

    private GlowstoneConfig config;
    private ApiService apiService;
    private PersistenceService persistenceService;
    private SubjectEntityRepository subjectEntityRepository;

    @Inject
    private Logger logger;

    @Inject
    @ConfigDir(sharedRoot = false)
    private File configDir;

    /**
     * Get the plugin instance
     *
     * @return Glowstone
     */
    public static Glowstone getInstance() {
        return instance;
    }

    /**
     * Get the logger for Glowstone
     *
     * @return Logger
     */
    public Logger getLogger() {
        return this.logger;
    }

    @Listener
    public void onGamePreInitializationEvent(GamePreInitializationEvent event) {

        instance = this;
        this.logger = LoggerFactory.getLogger(Glowstone.NAME);
        this.getLogger().info(String.format("Starting up %s v%s", Glowstone.NAME, Glowstone.VERSION));

        // Load configs
        this.setupConfigs();

        // Setup database
        this.setupDatabase();

        // Setup permission service
        this.setupPermissionService();
    }

    @Listener
    public void onGameInitializationEvent(GameInitializationEvent event) {
        Sponge.getEventManager().registerListeners(this, new PlayerListener());
        this.setupApiService();
    }

    @Listener
    public void onGameReloadEvent(GameReloadEvent event) {

        // Close db sessions
        if (this.persistenceService != null && this.persistenceService.getSessionFactory().isOpen()) {
            this.persistenceService.getSessionFactory().close();
        }

        // Reload configs
        this.setupConfigs();

        // Reload database
        this.setupDatabase();

        // Reload Server
        this.apiService.stop();
        this.apiService.start();

        this.getLogger().info(String.format("%s was reloaded", Glowstone.NAME));
    }

    @Listener
    public void onGameStoppedServerEvent(GameStoppedServerEvent event) {
        if (this.persistenceService != null && this.persistenceService.getSessionFactory().isOpen()) {
            this.persistenceService.getSessionFactory().close();
        }

        if (this.apiService != null) {
            this.apiService.stop();
        }
    }

    /**
     * Setup the configs
     */
    private void setupConfigs() {

        if (!this.configDir.isDirectory() && !this.configDir.mkdirs()) {
            this.getLogger().error(String.format("Unable to create %s config directory, please check file permissions.", Glowstone.NAME));
            Sponge.getServer().shutdown();
        }

        try {
            this.config = new GlowstoneConfig(this.configDir, "glowstone.conf");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Setup the database
     */
    private void setupDatabase() {
        Configuration configuration = new Configuration();
        configuration.addAnnotatedClass(ContextEntity.class);
        configuration.addAnnotatedClass(OptionEntity.class);
        configuration.addAnnotatedClass(OptionMapEntity.class);
        configuration.addAnnotatedClass(PermissionEntity.class);
        configuration.addAnnotatedClass(PermissionMapEntity.class);
        configuration.addAnnotatedClass(SubjectDataEntity.class);
        configuration.addAnnotatedClass(SubjectEntity.class);
        configuration.addAnnotatedClass(SubjectMapEntity.class);

        String type = this.config.get().getNode(GlowstoneConfig.DATABASE_SETTINGS, "type").getString("");
        String url = this.config.get().getNode(GlowstoneConfig.DATABASE_SETTINGS, "url").getString("");
        String database = this.config.get().getNode(GlowstoneConfig.DATABASE_SETTINGS, "database").getString("");
        String username = this.config.get().getNode(GlowstoneConfig.DATABASE_SETTINGS, "username").getString("");
        String password = this.config.get().getNode(GlowstoneConfig.DATABASE_SETTINGS, "password").getString("");

        try {
            this.persistenceService = new PersistenceService(configuration, type, url, database, username, password);
        } catch (Exception e) {
            this.getLogger().error(String.format("Please check you database settings in the %s config.", Glowstone.NAME));
            Sponge.getServer().shutdown();
        }

        this.subjectEntityRepository = new SubjectEntityRepository(this.persistenceService.getSessionFactory());
    }

    /**
     * Setup the permission service
     */
    private void setupPermissionService() {
        List<SubjectEntity> users = this.subjectEntityRepository.getUserSubjectEntities();
        users.forEach(user -> UserSubjectCollection.instance.getSubjects().put(user.getIdentifier(), user.getSubject()));

        List<SubjectEntity> groups = this.subjectEntityRepository.getGroupSubjectEntities();
        groups.forEach(group -> GroupSubjectCollection.instance.getSubjects().put(group.getIdentifier(), group.getSubject()));

        Optional<SubjectEntity> optionalDefault = this.subjectEntityRepository.getDefault();
        if (optionalDefault.isPresent()) {
            Subject defaultSubject = optionalDefault.get().getSubject();
            DefaultSubject.instance.getSubjectData().getAllPermissions().putAll(defaultSubject.getSubjectData().getAllPermissions());
            DefaultSubject.instance.getSubjectData().getAllParents().putAll(defaultSubject.getSubjectData().getAllParents());
            DefaultSubject.instance.getSubjectData().getAllOptions().putAll(defaultSubject.getSubjectData().getAllOptions());
        } else {
            SubjectEntityRepository.getInstance().save(DefaultSubject.instance.getSubjectEntity());
        }

        Sponge.getServiceManager().setProvider(this, PermissionService.class, GlowstonePermissionService.instance);
    }

    /**
     * Setup the api service
     */
    private void setupApiService() {
        int port = this.config.get().getNode(GlowstoneConfig.API_SETTINGS, "port").getInt(8766);
        ResourceConfig resourceConfig = new ResourceConfig();
        resourceConfig.register(new PlayerResource());
        resourceConfig.register(new SubjectResource(this.subjectEntityRepository));
        resourceConfig.register(new WorldResource());

        this.apiService = new ApiService(resourceConfig, port);
    }

}
