package com.github.glowstone.io.core;

import com.github.glowstone.io.core.configs.DefaultConfig;
import com.github.glowstone.io.core.configs.interfaces.Config;
import com.github.glowstone.io.core.entities.*;
import com.github.glowstone.io.core.http.ApiService;
import com.github.glowstone.io.core.http.resources.PlayerResource;
import com.github.glowstone.io.core.http.resources.SubjectResource;
import com.github.glowstone.io.core.http.resources.WorldResource;
import com.github.glowstone.io.core.listeners.PlayerListener;
import com.github.glowstone.io.core.permissions.GlowstonePermissionService;
import com.github.glowstone.io.core.permissions.GlowstoneSubject;
import com.github.glowstone.io.core.permissions.collections.GroupSubjectCollection;
import com.github.glowstone.io.core.permissions.collections.UserSubjectCollection;
import com.github.glowstone.io.core.permissions.subjects.DefaultSubject;
import com.github.glowstone.io.core.persistence.PersistenceService;
import com.github.glowstone.io.core.persistence.SubjectEntityStore;
import com.google.inject.Inject;
import org.glassfish.jersey.server.ResourceConfig;
import org.hibernate.cfg.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongepowered.api.Game;
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
    private Config config;
    private ApiService apiService;
    private PersistenceService persistenceService;
    private SubjectEntityStore subjectEntityStore;

    @Inject
    private Game game;

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

        // Create configuration directory
        if (!this.configDir.isDirectory()) {
            if (this.configDir.mkdirs()) {
                this.getLogger().info("configs directory successfully created");
            } else {
                this.getLogger().error("Unable to create plugin directory, please file permissions.");
                this.game.getServer().shutdown();
            }
        }

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
        try {
            this.config = new DefaultConfig(this.configDir, Glowstone.NAME + ".conf");
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

        this.persistenceService = new PersistenceService(this.config, configuration);
        this.subjectEntityStore = new SubjectEntityStore(this.persistenceService.getSessionFactory());
    }

    /**
     * Setup the permission service
     */
    private void setupPermissionService() {
        List<SubjectEntity> users = this.subjectEntityStore.getUserSubjectEntities();
        users.forEach(user -> UserSubjectCollection.instance.getSubjects().put(user.getIdentifier(), user.asSubject()));

        List<SubjectEntity> groups = this.subjectEntityStore.getGroupSubjectEntities();
        groups.forEach(group -> GroupSubjectCollection.instance.getSubjects().put(group.getIdentifier(), group.asSubject()));

        Optional<SubjectEntity> optionalDefault = this.subjectEntityStore.getDefault();
        if (optionalDefault.isPresent()) {
            Subject defaultSubject = optionalDefault.get().asSubject();
            DefaultSubject.instance.getSubjectData().getAllPermissions().putAll(defaultSubject.getSubjectData().getAllPermissions());
            DefaultSubject.instance.getSubjectData().getAllParents().putAll(defaultSubject.getSubjectData().getAllParents());
            DefaultSubject.instance.getSubjectData().getAllOptions().putAll(defaultSubject.getSubjectData().getAllOptions());
        } else {
            SubjectEntityStore.getInstance().save(DefaultSubject.instance.prepare());
        }

        Sponge.getServiceManager().setProvider(this, PermissionService.class, GlowstonePermissionService.instance);

        // TODO: remove this
        UserSubjectCollection.instance.getAllSubjects().forEach(subject -> getLogger().info(subject.getIdentifier(), ((GlowstoneSubject) subject).getName()));
    }

    /**
     * Setup the api service
     */
    private void setupApiService() {
        int port = this.config.get().getNode(DefaultConfig.API_SETTINGS, "port").getInt(8766);
        ResourceConfig resourceConfig = new ResourceConfig();
        resourceConfig.register(new PlayerResource());
        resourceConfig.register(new SubjectResource(this.subjectEntityStore));
        resourceConfig.register(new WorldResource());

        this.apiService = new ApiService(resourceConfig, port);
    }

}
