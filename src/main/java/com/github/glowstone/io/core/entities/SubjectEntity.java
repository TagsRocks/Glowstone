package com.github.glowstone.io.core.entities;

import com.github.glowstone.io.core.permissions.GlowstonePermissionService;
import com.github.glowstone.io.core.permissions.GlowstoneSubject;
import com.github.glowstone.io.core.permissions.collections.GroupSubjectCollection;
import com.github.glowstone.io.core.permissions.collections.PrivilegedSubjectCollection;
import com.github.glowstone.io.core.permissions.collections.UserSubjectCollection;
import com.google.common.base.Preconditions;
import com.google.gson.annotations.Expose;
import org.spongepowered.api.entity.living.player.User;
import org.spongepowered.api.service.permission.PermissionService;
import org.spongepowered.api.service.permission.Subject;

import javax.persistence.*;
import java.io.Serializable;

@NamedQueries({
        @NamedQuery(name = "getAllSubjects", query = "from SubjectEntity"),
        @NamedQuery(name = "getSubjectByIdentifier", query = "from SubjectEntity s where s.identifier = :identifier"),
        @NamedQuery(name = "getSubjectsByType", query = "from SubjectEntity s where s.type = :type"),
        @NamedQuery(name = "getSubjectByIdentifierAndType", query = "from SubjectEntity s where s.identifier = :identifier and s.type = :type")
})
@Entity
@org.hibernate.annotations.Entity(dynamicUpdate = true)
@Table(name = "subjects", uniqueConstraints = {
        @UniqueConstraint(columnNames = "subject_id"), @UniqueConstraint(columnNames = "identifier")
})
public class SubjectEntity implements Serializable {

    private static final long serialVersionUID = -6640605136430939650L;

    @Id
    @Expose(serialize = false, deserialize = false)
    @Column(name = "subject_id", unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long subjectId;

    @Expose
    @Column(name = "type", nullable = false)
    private String type;

    @Expose
    @Column(name = "identifier", unique = true, nullable = false)
    private String identifier;

    @Expose
    @Column(name = "name", nullable = false)
    private String name;

    @Expose
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "subject_data_id")
    private SubjectDataEntity subjectData;

    /**
     * SubjectEntity default constructor
     */
    public SubjectEntity() {
    }

    /**
     * SubjectEntity constructor
     *
     * @param type       String
     * @param identifier String
     * @param name       String
     */
    public SubjectEntity(String identifier, String name, String type) {
        Preconditions.checkNotNull(identifier);
        Preconditions.checkNotNull(name);
        Preconditions.checkNotNull(type);

        this.identifier = identifier;
        this.name = name;
        this.type = type;
    }

    /**
     * SubjectEntity constructor
     *
     * @param user User
     */
    public SubjectEntity(User user) {
        Preconditions.checkNotNull(user);

        this.identifier = user.getIdentifier();
        this.name = user.getName();
        this.type = PermissionService.SUBJECTS_USER;
    }

    /**
     * @return long
     */
    public long getSubjectId() {
        return this.subjectId;
    }

    /**
     * @return String
     */
    public String getType() {
        return this.type;
    }

    /**
     * @param type String
     */
    public void setType(String type) {
        Preconditions.checkNotNull(type);

        this.type = type;
    }

    /**
     * @return String
     */
    public String getIdentifier() {
        return this.identifier;
    }

    /**
     * @param identifier String
     */
    public void setIdentifier(String identifier) {
        Preconditions.checkNotNull(identifier);

        this.identifier = identifier;
    }

    /**
     * @return String
     */
    public String getName() {
        return this.name;
    }

    /**
     * @param name String
     */
    public void setName(String name) {
        Preconditions.checkNotNull(name);

        this.name = name;
    }

    /**
     * @return SubjectDataEntity
     */
    public SubjectDataEntity getSubjectData() {
        return this.subjectData;
    }

    /**
     * @param subjectData SubjectDataEntity
     */
    public void setSubjectData(SubjectDataEntity subjectData) {
        Preconditions.checkNotNull(subjectData);

        this.subjectData = subjectData;
    }

    /**
     * @return Subject
     */
    public Subject getSubject() {

        Subject subject;
        switch (this.type) {
            case GlowstonePermissionService.SUBJECTS_USER:
                subject = new GlowstoneSubject(this.identifier, this.name, this.type, UserSubjectCollection.instance);
                break;

            case GlowstonePermissionService.SUBJECTS_GROUP:
                subject = new GlowstoneSubject(this.identifier, this.name, this.type, GroupSubjectCollection.instance);
                break;

            default:
            case GlowstonePermissionService.SUBJECT_DEFAULT:
                subject = new GlowstoneSubject(this.identifier, this.name, this.type, PrivilegedSubjectCollection.instance);
                break;
        }

        this.getSubjectData().getAllPermissions().forEach(permissions ->
                subject.getSubjectData().getAllPermissions().put(permissions.getEntry().getKey(), permissions.getEntry().getValue())
        );
        this.getSubjectData().getAllParents().forEach(parents ->
                subject.getSubjectData().getAllParents().put(parents.getEntry().getKey(), parents.getEntry().getValue())
        );
        this.getSubjectData().getAllOptions().forEach(options ->
                subject.getSubjectData().getAllOptions().put(options.getEntry().getKey(), options.getEntry().getValue())
        );

        return subject;
    }

}
