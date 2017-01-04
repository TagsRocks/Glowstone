package com.github.glowstone.io.core.configs.interfaces;

import ninja.leaping.configurate.commented.CommentedConfigurationNode;

import java.io.File;
import java.io.IOException;

public interface Config {

    /**
     * Get the parent directory for this config file
     *
     * @return File
     */
    File getDirectory();

    /**
     * Get this config's file
     *
     * @return File
     */
    File getFile();

    /**
     * Get the main config node
     *
     * @return CommentedConfigurationNode
     */
    CommentedConfigurationNode get();

    /**
     * Save this config
     *
     * @throws IOException will be thrown if there was an error saving the file
     */
    void save() throws IOException;

}
