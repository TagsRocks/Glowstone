package com.github.glowstone.io.core.api.resources;

import com.github.glowstone.io.core.entities.ContextEntity;
import com.github.glowstone.io.core.objects.ErrorObject;
import com.github.glowstone.io.core.persistence.repositories.ContextRepository;
import com.google.common.base.Preconditions;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Optional;

@Path("/contexts")
@Produces(MediaType.APPLICATION_JSON)
public class ContextResource extends Resource {

    private final ContextRepository contextRepository;

    /**
     * ContextResource constructor
     *
     * @param contextRepository ContextRepository
     */
    public ContextResource(ContextRepository contextRepository) {
        Preconditions.checkNotNull(contextRepository);

        this.contextRepository = contextRepository;
    }

    @GET
    public Response getContexts() {
        return success(this.contextRepository.getAllContexts());
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addContext(ContextEntity context) {
        if (context.getType() == null) {
            String details = "The `type` field is missing";
            return error(Response.Status.BAD_REQUEST, new ErrorObject(Resource.MISSING_PARAMETER, details));
        }

        if (context.getName() == null) {
            String details = "The `name` field is missing";
            return error(Response.Status.BAD_REQUEST, new ErrorObject(Resource.MISSING_PARAMETER, details));
        }

        Optional<ContextEntity> optional = this.contextRepository.get(context.getType(), context.getName());
        if (optional.isPresent()) {
            return success(optional.get());
        } else {
            return created(this.contextRepository.save(context));
        }
    }

    @GET
    @Path("/{contextId}")
    public Response getContext(@PathParam("contextId") long id) {
        Optional<ContextEntity> optional = this.contextRepository.get(id);
        if (optional.isPresent()) {
            return success(optional.get());
        } else {
            return notFound("Context", id);
        }
    }

    @PUT
    @Path("/{contextId}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateContext(@PathParam("contextId") long id, ContextEntity context) {
        Optional<ContextEntity> optional = this.contextRepository.get(id);
        if (optional.isPresent()) {
            ContextEntity updated = optional.get();
            if (context.getType() != null) {
                updated.setType(context.getType());
            }
            if (context.getName() != null) {
                updated.setName(context.getName());
            }
            updated = this.contextRepository.save(updated);
            return success(updated);
        }

        return notFound("Context", id);
    }

    @DELETE
    @Path("/{contextId}")
    public Response deleteContext(@PathParam("contextId") long id) {
        Optional<ContextEntity> optional = this.contextRepository.get(id);
        if (optional.isPresent()) {
            this.contextRepository.remove(optional.get());
            return deleted();
        }

        return notFound("Context", id);
    }

}
