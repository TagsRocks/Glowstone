package com.github.glowstone.io.core.entities;

import com.google.common.base.Preconditions;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@org.hibernate.annotations.Entity(dynamicUpdate = true)
@Table(name = "subjects", uniqueConstraints = {@UniqueConstraint(columnNames = "subject_id"), @UniqueConstraint(columnNames = "identifier")})
public class SubjectEntity implements Serializable {

    private static final long serialVersionUID = -6640605136430939650L;

    public static final int SUBJECT_TYPE_USER = 1;
    public static final int SUBJECT_TYPE_GROUP = 2;
    public static final int SUBJECT_TYPE_SYSTEM = 3;
    public static final int SUBJECT_TYPE_COMMAND_BLOCK = 4;
    public static final int SUBJECT_TYPE_ROLE_TEMPLATE = 5;

    /**
     * SubjectEntity constructor
     *
     * @param subjectTypeId int
     * @param identifier    String
     */
    public SubjectEntity(int subjectTypeId, String identifier, String name) {
        Preconditions.checkNotNull(subjectTypeId);
        Preconditions.checkNotNull(identifier);
        Preconditions.checkNotNull(name);
        Preconditions.checkArgument((subjectTypeId > 0 && subjectTypeId <= 5));

        this.subjectTypeId = subjectTypeId;
        this.identifier = identifier;
        this.name = name;
    }

    @Id
    @Column(name = "subject_id", unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int subjectId;

    @Column(name = "subject_type_id", unique = false, nullable = false)
    private int subjectTypeId;

    @Column(name = "identifier", unique = true, nullable = false)
    private String identifier;

    @Column(name = "name", nullable = false)
    private String name;

    @ManyToMany(cascade = {CascadeType.ALL})
    @JoinTable(name = "subject_children", joinColumns = {@JoinColumn(name = "subject_id")}, inverseJoinColumns = {@JoinColumn(name = "child_subject_id")})
    private Set<SubjectEntity> children = new HashSet<>();

    @ManyToMany(mappedBy = "children")
    private Set<SubjectEntity> parents = new HashSet<>();

    /**
     * @return int
     */
    public int getSubjectTypeId() {
        return this.subjectTypeId;
    }

    /**
     * @param subjectTypeId int
     */
    public void setSubjectTypeId(int subjectTypeId) {
        Preconditions.checkNotNull(identifier);
        Preconditions.checkArgument((subjectTypeId > 0 && subjectTypeId <= 5));

        this.subjectTypeId = subjectTypeId;
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
     * @return Set<SubjectEntity>
     */
    public Set<SubjectEntity> getChildren() {
        return this.children;
    }

    /**
     * @return Set<SubjectEntity>
     */
    public Set<SubjectEntity> getParents() {
        return this.parents;
    }

}
