package com.github.glowstone.io.core.api.resources;

import com.github.glowstone.io.core.entities.OptionEntity;
import com.github.glowstone.io.core.objects.ErrorObject;
import com.github.glowstone.io.core.persistence.repositories.OptionRepository;
import com.google.common.base.Preconditions;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Optional;

@Path("/options")
@Produces(MediaType.APPLICATION_JSON)
public class OptionResource extends Resource {

    private final OptionRepository optionRepository;

    /**
     * OptionResource constructor
     *
     * @param optionRepository OptionRepository
     */
    public OptionResource(OptionRepository optionRepository) {
        Preconditions.checkNotNull(optionRepository);

        this.optionRepository = optionRepository;
    }

    @GET
    public Response getOptions() {
        return success(this.optionRepository.getAllOptions());
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addOption(OptionEntity option) {
        if (option.getKey() == null) {
            String details = "The `key` field is missing";
            return error(Response.Status.BAD_REQUEST, new ErrorObject(Resource.MISSING_PARAMETER, details));
        }

        if (option.getValue() == null) {
            String details = "The `value` field is missing";
            return error(Response.Status.BAD_REQUEST, new ErrorObject(Resource.MISSING_PARAMETER, details));
        }

        Optional<OptionEntity> optional = this.optionRepository.get(option.getKey(), option.getValue());
        if (optional.isPresent()) {
            return success(optional.get());
        } else {
            return created(this.optionRepository.save(option));
        }
    }

    @GET
    @Path("/{optionId}")
    public Response getOption(@PathParam("optionId") long id) {
        Optional<OptionEntity> optional = this.optionRepository.get(id);
        if (optional.isPresent()) {
            return success(optional.get());
        } else {
            return notFound("Option", id);
        }
    }

    @PUT
    @Path("/{optionId}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateOption(@PathParam("optionId") long id, OptionEntity option) {
        Optional<OptionEntity> optional = this.optionRepository.get(id);
        if (optional.isPresent()) {
            OptionEntity updated = optional.get();
            if (option.getKey() != null) {
                updated.setKey(option.getKey());
            }
            if (option.getValue() != null) {
                updated.setValue(option.getValue());
            }
            updated = this.optionRepository.save(updated);
            return success(updated);
        }

        return notFound("Option", id);
    }

    @DELETE
    @Path("/{optionId}")
    public Response deleteOption(@PathParam("optionId") long id) {
        Optional<OptionEntity> optional = this.optionRepository.get(id);
        if (optional.isPresent()) {
            this.optionRepository.remove(optional.get());
            return deleted();
        }

        return notFound("Option", id);
    }

}
