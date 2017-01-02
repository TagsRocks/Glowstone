package com.github.glowstone.io.core;

import com.github.glowstone.io.core.configs.DefaultConfig;
import com.github.glowstone.io.core.configs.interfaces.Configuration;
import com.github.glowstone.io.core.http.ApiService;
import com.github.glowstone.io.core.http.resources.PlayerResource;
import com.github.glowstone.io.core.http.resources.WorldResource;
import com.github.glowstone.io.core.persistence.PersistenceService;
import com.google.inject.Inject;
import org.glassfish.jersey.server.ResourceConfig;
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

import java.io.File;
import java.io.IOException;

@Plugin(id = Glowstone.ID, name = Glowstone.NAME, version = Glowstone.VERSION, description = Glowstone.DESCRIPTION)
public class Glowstone {

    public static final String ID = "glowstone";
    public static final String NAME = "Glowstone";
    public static final String VERSION = "1.0.0";
    public static final String DESCRIPTION = "A Minecraft apiService plugin that runs on top of Sponge for Glowstone.io integration";

    private static Glowstone instance;
    private Configuration config;
    private ApiService apiService;
    private PersistenceService persistenceService;

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
    public void onPreInit(GamePreInitializationEvent event) {

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
    }

    @Listener
    public void onGameInit(GameInitializationEvent event) {
        this.setupApiService();
    }

    @Listener
    public void onGameReload(GameReloadEvent event) {

        // Close db sessions
        if (this.persistenceService != null) {
            this.persistenceService.getEntityManager().close();
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
    public void onGameStop(GameStoppingEvent event) {

        if (this.persistenceService != null) {
            this.persistenceService.getEntityManager().close();
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
        this.persistenceService = new PersistenceService(this.config, Glowstone.ID);
    }

    /**
     * Setup the api service
     */
    private void setupApiService() {
        ResourceConfig resourceConfig = new ResourceConfig();
        resourceConfig.register(new PlayerResource());
        resourceConfig.register(new WorldResource());
        int port = this.config.get().getNode(DefaultConfig.API_SETTINGS, "port").getInt(8766);
        this.apiService = new ApiService(resourceConfig, port);
    }

}
