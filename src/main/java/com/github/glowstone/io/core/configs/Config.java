package com.github.glowstone.io.core.configs;

import com.github.glowstone.io.core.configs.interfaces.Configuration;
import com.google.common.base.Preconditions;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import ninja.leaping.configurate.loader.ConfigurationLoader;

import java.io.File;
import java.io.IOException;

public abstract class Config implements Configuration {

    private static Configuration instance;
    private final File directory;
    private final File file;
    private final ConfigurationLoader<CommentedConfigurationNode> loader;
    private final CommentedConfigurationNode config;

    /**
     * Config constructor
     *
     * @param directory File
     * @param filename  String
     * @throws IOException maybe thrown if there was an error loading the file, or creating the file for the first time.
     */
    Config(File directory, String filename) throws IOException {
        Preconditions.checkNotNull(directory);
        Preconditions.checkNotNull(filename);

        instance = this;
        this.directory = directory;
        this.file = new File(this.directory, filename);
        this.loader = HoconConfigurationLoader.builder().setFile(this.file).build();

        if (!this.file.isFile() && this.file.createNewFile()) {
            this.config = this.loader.load();
            this.setDefaults();
            this.save();
        } else {
            this.config = this.loader.load();
        }
    }

    /**
     * Get this Configuration instance
     *
     * @return Configuration
     */
    public static Configuration getInstance() {
        return instance;
    }

    /**
     * Get the parent directory for this config file
     *
     * @return File
     */
    public File getDirectory() {
        return this.directory;
    }

    /**
     * Get this config's file
     *
     * @return File
     */
    public File getFile() {
        return this.file;
    }

    /**
     * Get the main config node
     *
     * @return CommentedConfigurationNode
     */
    public CommentedConfigurationNode get() {
        return this.config;
    }

    /**
     * Save this config
     *
     * @throws IOException will be thrown if there was an error saving the file
     */
    public void save() throws IOException {
        this.loader.save(get());
    }

    /**
     * Set default values the first time the config file is generated
     */
    protected abstract void setDefaults();

}