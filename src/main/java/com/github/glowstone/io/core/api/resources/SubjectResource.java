package com.github.glowstone.io.core.api.resources;

import com.github.glowstone.io.core.entities.SubjectEntity;
import com.github.glowstone.io.core.persistence.repositories.SubjectRepository;
import com.google.common.base.Preconditions;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.spongepowered.api.service.permission.PermissionService;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.Optional;

@Path("/subjects")
@Produces(MediaType.APPLICATION_JSON)
public class SubjectResource {

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
    public String getSubjects() {

        Gson data = new Gson();
        List<SubjectEntity> subjects = this.subjectRepository.getAllSubjectEntities();

        return data.toJson(subjects);
    }

    @GET
    @Path("/users")
    public String getUserSubjects() {

        Gson data = new Gson();
        List<SubjectEntity> subjects = this.subjectRepository.getUserSubjectEntities();

        return data.toJson(subjects);
    }

    @GET
    @Path("/users/{userId}")
    public String getUserSubject(@PathParam("userId") String userId) {
        Preconditions.checkNotNull(userId);

        Gson data = new Gson();
        Optional<SubjectEntity> optional = this.subjectRepository.getSubjectEntityByIdentifierAndType(userId, PermissionService.SUBJECTS_USER);

        return optional.isPresent() ? data.toJson(optional.get()) : data.toJson(String.format("Subject '%s' not found", userId));
    }

    @GET
    @Path("/groups")
    public String getGroupSubjects() {

        Gson data = new Gson();
        List<SubjectEntity> subjects = this.subjectRepository.getGroupSubjectEntities();

        return data.toJson(subjects);
    }

    @GET
    @Path("/groups/{groupId}")
    public String getGroupSubject(@PathParam("groupId") String groupId) {
        Preconditions.checkNotNull(groupId);

        Gson data = new Gson();
        Optional<SubjectEntity> optional = this.subjectRepository.getSubjectEntityByIdentifierAndType(groupId, PermissionService.SUBJECTS_GROUP);

        return optional.isPresent() ? data.toJson(optional.get()) : data.toJson(String.format("Subject '%s' not found", groupId));
    }

}
