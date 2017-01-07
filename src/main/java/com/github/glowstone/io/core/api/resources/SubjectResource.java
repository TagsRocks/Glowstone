package com.github.glowstone.io.core.api.resources;

import com.github.glowstone.io.core.entities.SubjectEntity;
import com.github.glowstone.io.core.persistence.repositories.SubjectRepository;
import com.google.common.base.Preconditions;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Optional;

@Path("/subjects")
@Produces(MediaType.APPLICATION_JSON)
public class SubjectResource extends Resource {

    private final SubjectRepository subjectRepository;

    /**
     * SubjectResource constructor
     *
     * @param subjectRepository SubjectRepository
     */
    public SubjectResource(SubjectRepository subjectRepository) {
        Preconditions.checkNotNull(subjectRepository);

        this.subjectRepository = subjectRepository;
    }

    @GET
    public Response getSubjects() {
        return success(this.subjectRepository.getAllSubjects());
    }

    @GET
    @Path("/users")
    public Response getUserSubjects() {
        return success(this.subjectRepository.getUserSubjects());
    }

    @GET
    @Path("/users/{userId}")
    public Response getUserSubject(@PathParam("userId") long id) {
        Optional<SubjectEntity> optional = this.subjectRepository.get(id);
        if (optional.isPresent()) {
            return success(optional.get());
        } else {
            return notFound("User", id);
        }
    }

    @GET
    @Path("/groups")
    public Response getGroupSubjects() {
        return success(this.subjectRepository.getGroupSubjects());
    }

    @GET
    @Path("/groups/{groupId}")
    public Response getGroupSubject(@PathParam("groupId") long id) {
        Optional<SubjectEntity> optional = this.subjectRepository.get(id);
        if (optional.isPresent()) {
            return success(optional.get());
        } else {
            return notFound("Group", id);
        }
    }

}
