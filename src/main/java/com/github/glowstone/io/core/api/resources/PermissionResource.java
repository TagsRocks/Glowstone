package com.github.glowstone.io.core.api.resources;

import com.github.glowstone.io.core.entities.PermissionEntity;
import com.github.glowstone.io.core.objects.ErrorObject;
import com.github.glowstone.io.core.persistence.repositories.PermissionRepository;
import com.google.common.base.Preconditions;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Optional;

@Path("/permissions")
@Produces(MediaType.APPLICATION_JSON)
public class PermissionResource extends Resource {

    private final PermissionRepository permissionRepository;

    /**
     * PermissionResource constructor
     *
     * @param permissionRepository PermissionRepository
     */
    public PermissionResource(PermissionRepository permissionRepository) {
        Preconditions.checkNotNull(permissionRepository);

        this.permissionRepository = permissionRepository;
    }

    @GET
    public Response getPermissions() {
        return success(this.permissionRepository.getAllPermissions());
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addPermission(PermissionEntity permission) {
        if (permission.getPermission() == null) {
            String details = "The permission field is missing";
            return error(Response.Status.BAD_REQUEST, new ErrorObject(Resource.MISSING_PARAMETER, details));
        }

        Optional<PermissionEntity> optional = this.permissionRepository.get(permission.getPermission(), permission.getValue());
        if (optional.isPresent()) {
            return success(optional.get());
        } else {
            return success(this.permissionRepository.save(permission));
        }
    }

    @GET
    @Path("/{id}")
    public Response getPermission(@PathParam("id") long id) {
        Optional<PermissionEntity> optional = this.permissionRepository.get(id);
        if (optional.isPresent()) {
            return success(optional.get());
        } else {
            String details = String.format("Permission not found with id: %s", id);
            return error(Response.Status.NOT_FOUND, new ErrorObject(Resource.ENTITY_NOT_FOUND, details));
        }
    }

}
