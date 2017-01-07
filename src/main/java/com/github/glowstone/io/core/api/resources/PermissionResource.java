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
            String details = "The `permission` field is missing";
            return error(Response.Status.BAD_REQUEST, new ErrorObject(Resource.MISSING_PARAMETER, details));
        }

        Optional<PermissionEntity> optional = this.permissionRepository.get(permission.getPermission(), permission.getValue());
        if (optional.isPresent()) {
            return success(optional.get());
        } else {
            return created(this.permissionRepository.save(permission));
        }
    }

    @GET
    @Path("/{permissionId}")
    public Response getPermission(@PathParam("permissionId") long id) {
        Optional<PermissionEntity> optional = this.permissionRepository.get(id);
        if (optional.isPresent()) {
            return success(optional.get());
        } else {
            return notFound("Permission", id);
        }
    }

    @PUT
    @Path("/{permissionId}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updatePermission(@PathParam("permissionId") long id, PermissionEntity permission) {
        Optional<PermissionEntity> optional = this.permissionRepository.get(id);
        if (optional.isPresent()) {
            PermissionEntity updated = optional.get();
            if (permission.getPermission() != null) {
                updated.setPermission(permission.getPermission());
            }
            updated.setValue(permission.getValue());
            updated = this.permissionRepository.save(updated);
            return success(updated);
        }

        return notFound("Permission", id);
    }

    @DELETE
    @Path("/{permissionId}")
    public Response deletePermission(@PathParam("permissionId") long id) {
        Optional<PermissionEntity> optional = this.permissionRepository.get(id);
        if (optional.isPresent()) {
            this.permissionRepository.remove(optional.get());
            return deleted();
        }

        return notFound("Permission", id);
    }

}
