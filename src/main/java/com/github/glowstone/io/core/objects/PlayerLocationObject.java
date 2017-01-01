package com.github.glowstone.io.core.objects;

public class PlayerLocationObject {

    public String world;

    public int x;

    public int y;

    public int z;

    /**
     * @param world String
     * @param x     int
     * @param y     int
     * @param z     int
     */
    public PlayerLocationObject(String world, int x, int y, int z) {
        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;
    }

}
