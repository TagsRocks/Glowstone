package com.github.glowstone.io.core.objects;

import com.google.common.base.Preconditions;
import org.spongepowered.api.world.World;

import java.io.Serializable;

public class WorldObject implements Serializable {

    private static final long serialVersionUID = -7271007420382479187L;

    public String uniqueId;
    public String name;
    public String difficulty;
    public String gameMode;
    public String dimension;
    public String generator;
    public Long seed;
    public boolean isEnabled;
    public boolean isInitialized;
    public boolean loadOnStartup;
    public boolean generateSpawnsOnLoad;
    public boolean keepsSpawnsLoaded;
    public boolean isHardcore;
    public boolean allowCommands;
    public boolean isPVPEnabled;
    public boolean generateBonusChests;
    public boolean usesMapFeatures;

    /**
     * WorldObject constructor
     *
     * @param world WorldObject
     */
    public WorldObject(World world) {
        Preconditions.checkNotNull(world);

        this.uniqueId = world.getProperties().getUniqueId().toString();
        this.name = world.getProperties().getWorldName();
        this.difficulty = world.getProperties().getDifficulty().getName();
        this.gameMode = world.getProperties().getGameMode().getName();
        this.dimension = world.getProperties().getDimensionType().getName();
        this.generator = world.getProperties().getGeneratorType().getName();
        this.seed = world.getProperties().getSeed();
        this.isEnabled = world.getProperties().isEnabled();
        this.isInitialized = world.getProperties().isInitialized();
        this.loadOnStartup = world.getProperties().loadOnStartup();
        this.generateSpawnsOnLoad = world.getProperties().doesGenerateSpawnOnLoad();
        this.keepsSpawnsLoaded = world.getProperties().doesKeepSpawnLoaded();
        this.isHardcore = world.getProperties().isHardcore();
        this.allowCommands = world.getProperties().areCommandsAllowed();
        this.isPVPEnabled = world.getProperties().isPVPEnabled();
        this.generateBonusChests = world.getProperties().doesGenerateBonusChest();
        this.usesMapFeatures = world.getProperties().usesMapFeatures();
    }
}
