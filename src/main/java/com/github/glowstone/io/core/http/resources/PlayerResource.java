package com.github.glowstone.io.core.http.resources;

import com.github.glowstone.io.core.Glowstone;
import com.github.glowstone.io.core.objects.PlayerLocationObject;
import com.google.gson.Gson;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.entity.living.player.User;
import org.spongepowered.api.profile.GameProfile;
import org.spongepowered.api.service.user.UserStorageService;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

@Path("/players")
@Produces(MediaType.APPLICATION_JSON)
public class PlayerResource {

    @GET
    public String getPlayers() {

        Optional<UserStorageService> optionalService = Sponge.getServiceManager().provide(UserStorageService.class);
        if (!optionalService.isPresent()) {
            Glowstone.getLogger().error("User storage service unavailable.");
            return "";
        }

        UserStorageService userStorageService = optionalService.get();
        Collection<GameProfile> players = userStorageService.getAll();

        Gson json = new Gson();
        return json.toJson(players);

    }

    @GET
    @Path("/{playerId}")
    public String getPlayer(@PathParam("playerId") String playerId) {

        Gson json = new Gson();
        User user = getUser(playerId);

        if (user == null) {
            return json.toJson(String.format("Player '%s' not found", playerId));
        }

        return json.toJson(user.getProfile());
    }

    @GET
    @Path(("/{playerId}/location"))
    public String getPlayerLocation(@PathParam("playerId") String playerId) {

        Gson json = new Gson();
        User user = getUser(playerId);

        if (user == null) {
            return json.toJson(String.format("Player '%s' not found", playerId));
        }

        Optional<Player> optionalPlayer = user.getPlayer();
        if (!optionalPlayer.isPresent()) {
            return json.toJson(String.format("%s is offline.", user.getName()));
        }

        Player player = optionalPlayer.get();
        Location<World> location = player.getLocation();

        return json.toJson(new PlayerLocationObject(location.getExtent().getName(), location.getBlockX(), location.getBlockY(), location.getBlockZ()));

    }

    /**
     * Get the User by last known name or unique id
     *
     * @param playerId String
     * @return User
     */
    private User getUser(String playerId) {

        Optional<UserStorageService> optionalService = Sponge.getServiceManager().provide(UserStorageService.class);
        if (!optionalService.isPresent()) {
            return null;
        }

        UserStorageService userStorageService = optionalService.get();
        Optional<User> optionalUser;

        try {
            UUID playerUniqueId = UUID.fromString(playerId);
            optionalUser = userStorageService.get(playerUniqueId);
        } catch (Exception e) {
            optionalUser = userStorageService.get(playerId);
        }

        if (!optionalUser.isPresent()) {
            return null;
        }

        return optionalUser.get();

    }

}
