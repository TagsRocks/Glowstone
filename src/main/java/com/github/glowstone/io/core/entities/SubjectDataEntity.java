package com.github.glowstone.io.core.entities;

import com.google.common.base.Preconditions;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@Entity
@org.hibernate.annotations.Entity(dynamicUpdate = true)
@Table(name = "subject_data", uniqueConstraints = {
        @UniqueConstraint(columnNames = "subject_data_id")
})
public class SubjectDataEntity implements Serializable {

    private static final long serialVersionUID = -3317877691585905050L;

    @Id
    @Column(name = "subject_id", unique = true, nullable = false)
    @GeneratedValue(generator = "generator")
    @GenericGenerator(name = "generator", strategy = "foreign",
            parameters = @Parameter(name = "property", value = "subject")
    )
    private int subjectId;

    @OneToOne
    @PrimaryKeyJoinColumn
    private SubjectEntity subject;

    @ManyToMany(cascade = {CascadeType.ALL})
    @JoinTable(name = "subject_data_permissions",
            joinColumns = {@JoinColumn(name = "subject_id")},
            inverseJoinColumns = {@JoinColumn(name = "permission_map_id")}
    )
    private Set<PermissionMapEntity> permissions;

    @ManyToMany(cascade = {CascadeType.ALL})
    @JoinTable(name = "subject_data_subjects",
            joinColumns = {@JoinColumn(name = "subject_id")},
            inverseJoinColumns = {@JoinColumn(name = "subject_map_id")}
    )
    private Set<SubjectMapEntity> parents;

    @ManyToMany(cascade = {CascadeType.ALL})
    @JoinTable(name = "subject_data_options",
            joinColumns = {@JoinColumn(name = "subject_id")},
            inverseJoinColumns = {@JoinColumn(name = "option_map_id")}
    )
    private Set<OptionMapEntity> options;

    /**
     * SubjectDataEntity constructor
     */
    public SubjectDataEntity() {
    }

    /**
     * @return SubjectEntity
     */
    public SubjectEntity getSubject() {
        return this.subject;
    }

    /**
     * @param subject SubjectEntity
     */
    public void setSubject(SubjectEntity subject) {
        Preconditions.checkNotNull(subject);

        this.subject = subject;
    }

    /**
     * @return Set of PermissionMapEntity
     */
    public Set<PermissionMapEntity> getAllPermissions() {
        return this.permissions;
    }

    /**
     * @return Set of SubjectMapEntity
     */
    public Set<SubjectMapEntity> getAllParents() {
        return this.parents;
    }

    /**
     * @return Set of OptionMapEntity
     */
    public Set<OptionMapEntity> getAllOptions() {
        return this.options;
    }
}
