package com.github.glowstone.io.core.configs;

import com.github.glowstone.io.core.Glowstone;

import java.io.File;

public class DefaultConfig extends Config {

    public static final String API_SETTINGS = "API Settings";
    public static final String DATABASE_SETTINGS = "Database Settings";

    /**
     * DefaultConfig Constructor
     *
     * @param configDir File
     */
    public DefaultConfig(File configDir) {
        super(configDir);
        setConfigFile(new File(configDir, Glowstone.NAME + ".conf"));
    }

    @Override
    protected void setDefaults() {
        get().getNode(API_SETTINGS, "port").setValue(8766);

        get().getNode(DATABASE_SETTINGS, "type").setValue("H2").setComment("Accepted Types: H2");
        get().getNode(DATABASE_SETTINGS, "url").setValue("jdbc:h2:file:." + File.separator + "glowstone" + File.separator);
        get().getNode(DATABASE_SETTINGS, "database").setValue("data");
        get().getNode(DATABASE_SETTINGS, "username").setValue("");
        get().getNode(DATABASE_SETTINGS, "password").setValue("");
    }

}
