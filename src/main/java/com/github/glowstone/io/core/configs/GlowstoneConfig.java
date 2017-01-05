package com.github.glowstone.io.core.configs;

import java.io.File;
import java.io.IOException;

public class GlowstoneConfig extends Config {

    public static final String API_SETTINGS = "API Settings";
    public static final String DATABASE_SETTINGS = "Database Settings";

    private static GlowstoneConfig instance;

    /**
     * Config constructor
     *
     * @param directory File
     * @param filename  String
     * @throws IOException maybe thrown if there was an error loading the file, or creating the file for the first time.
     */
    public GlowstoneConfig(File directory, String filename) throws IOException {
        super(directory, filename);

        instance = this;
    }

    /**
     * @return the GlowstoneConfig instance
     */
    public GlowstoneConfig getInstance() {
        return instance;
    }

    @Override
    protected void setDefaults() {
        get().getNode(API_SETTINGS, "port").setValue(8766);

        get().getNode(DATABASE_SETTINGS, "type").setValue("H2"); //.setComment("Accepted Types: H2");
        get().getNode(DATABASE_SETTINGS, "url").setValue("jdbc:h2:file:." + File.separator + "glowstone" + File.separator);
        get().getNode(DATABASE_SETTINGS, "database").setValue("data");
        get().getNode(DATABASE_SETTINGS, "username").setValue("");
        get().getNode(DATABASE_SETTINGS, "password").setValue("");
    }

}
