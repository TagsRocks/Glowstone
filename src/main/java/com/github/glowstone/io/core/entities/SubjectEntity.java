package com.github.glowstone.io.core.entities;

import com.google.common.base.Preconditions;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@Entity
@org.hibernate.annotations.Entity(dynamicUpdate = true)
@Table(name = "subjects", uniqueConstraints = {
        @UniqueConstraint(columnNames = "subject_id"), @UniqueConstraint(columnNames = "identifier")
})
public class SubjectEntity implements Serializable {

    private static final long serialVersionUID = -6640605136430939650L;

    @Id
    @Column(name = "subject_id", unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int subjectId;

    @Column(name = "type", nullable = false)
    private String type;

    @Column(name = "identifier", unique = true, nullable = false)
    private String identifier;

    @Column(name = "name", nullable = false)
    private String name;

    @OneToOne(mappedBy = "subject", cascade = {CascadeType.ALL})
    private SubjectDataEntity subjectData;

    @ManyToMany(mappedBy = "subjects")
    private Set<SubjectMapEntity> subjectMappings;

    /**
     * SubjectEntity constructor
     *
     * @param type       String
     * @param identifier String
     * @param name       String
     */
    public SubjectEntity(String type, String identifier, String name) {
        Preconditions.checkNotNull(type);
        Preconditions.checkNotNull(identifier);
        Preconditions.checkNotNull(name);

        this.type = type;
        this.identifier = identifier;
        this.name = name;
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

}
