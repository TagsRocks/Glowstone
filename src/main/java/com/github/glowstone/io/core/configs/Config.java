package com.github.glowstone.io.core.configs;

import com.github.glowstone.io.core.Glowstone;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import ninja.leaping.configurate.loader.ConfigurationLoader;

import java.io.File;
import java.io.IOException;

abstract public class Config {

    private File configDir;
    private File configFile;
    private ConfigurationLoader<CommentedConfigurationNode> configLoader;
    private CommentedConfigurationNode config;

    /**
     * Get this configs's directory
     *
     * @return File
     */
    public File getConfigDir() {
        return this.configDir;
    }

    /**
     * Get this configs's file
     *
     * @return File
     */
    public File getConfigFile() {
        return this.configFile;
    }

    /**
     * Set this configs's file
     *
     * @param configFile File
     */
    public void setConfigFile(File configFile) {
        this.configFile = configFile;
    }

    /**
     * Get the configuration loader
     *
     * @return ConfigurationLoader<CommentedConfigurationNode>
     */
    public ConfigurationLoader<CommentedConfigurationNode> getConfigLoader() {
        return this.configLoader;
    }

    /**
     * Set the configuration loader
     *
     * @param configLoader ConfigurationLoader<CommentedConfigurationNode>
     */
    public void setConfigLoader(ConfigurationLoader<CommentedConfigurationNode> configLoader) {
        this.configLoader = configLoader;
    }

    /**
     * Get this configs
     *
     * @return CommendedConfigurationNode
     */
    public CommentedConfigurationNode get() {
        return this.config;
    }

    /**
     * Set this configs
     *
     * @param config CommentedConfigurationNode
     */
    public void setConfig(CommentedConfigurationNode config) {
        this.config = config;
    }

    /**
     * configs constructor
     *
     * @param configDir File
     */
    public Config(File configDir) {
        this.configDir = configDir;
    }

    /**
     * Load configs
     */
    public void load() {

        setConfigLoader(HoconConfigurationLoader.builder().setFile(getConfigFile()).build());

        try {

            boolean isNew = false;
            if (!getConfigFile().isFile()) {
                if (getConfigFile().createNewFile()) {
                    isNew = true;
                }
            }
            setConfig(getConfigLoader().load());

            if (isNew) {
                setDefaults();
                save();
            }

        } catch (IOException e) {
            Glowstone.getLogger().error("There was an error loading the configs: " + e.getMessage());
        }

    }

    /**
     * Set default values the first time the configs file is generated
     */
    abstract protected void setDefaults();

    /**
     * Save this configs to disk
     */
    public void save() {

        try {
            getConfigLoader().save(get());

        } catch (IOException e) {
            Glowstone.getLogger().error("There was an error saving the configs: " + e.getMessage());
        }

    }

}