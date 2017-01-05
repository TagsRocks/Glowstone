package com.github.glowstone.io.core.api.resources;

import com.github.glowstone.io.core.objects.WorldObject;
import com.google.common.base.Preconditions;
import com.google.gson.Gson;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.world.World;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.Collection;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

@Path("/worlds")
@Produces(MediaType.APPLICATION_JSON)
public class WorldResource {

    @GET
    public String getWorlds() {

        Gson data = new Gson();
        Collection<WorldObject> worlds = new CopyOnWriteArrayList<>();
        Collection<World> serverWorlds = Sponge.getServer().getWorlds();
        serverWorlds.forEach(world -> worlds.add(new WorldObject(world)));

        return data.toJson(worlds);
    }

    @GET
    @Path("/{worldId}")
    public String getWorld(@PathParam("worldId") String worldId) {
        Preconditions.checkNotNull(worldId);

        Gson data = new Gson();
        World world = this.getWorldById(worldId);

        return (world == null) ? data.toJson(String.format("World '%s' not found", worldId)) : data.toJson(new WorldObject(world));
    }

    /**
     * Get the world by name or UUID
     *
     * @param worldId String name or UUID
     * @return World
     */
    private World getWorldById(String worldId) {
        Preconditions.checkNotNull(worldId);

        Optional<World> optionalWorld;
        try {
            UUID uniqueId = UUID.fromString(worldId);
            optionalWorld = Sponge.getServer().getWorld(uniqueId);
        } catch (IllegalArgumentException e) {
            optionalWorld = Sponge.getServer().getWorld(worldId);
        }

        return optionalWorld.orElse(null);
    }

}
