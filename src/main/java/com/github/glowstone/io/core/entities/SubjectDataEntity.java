package com.github.glowstone.io.core.entities;

import com.google.gson.annotations.Expose;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@org.hibernate.annotations.Entity(dynamicUpdate = true)
@Table(name = "subject_data", uniqueConstraints = {
        @UniqueConstraint(columnNames = "subject_data_id")
})
public class SubjectDataEntity implements Serializable {

    private static final long serialVersionUID = -3317877691585905050L;

    @Id
    @Expose(serialize = false, deserialize = false)
    @Column(name = "subject_data_id", unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long subjectDataId;

    @Expose
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "subject_data_permissions",
            joinColumns = {@JoinColumn(name = "subject_data_id")},
            inverseJoinColumns = {@JoinColumn(name = "permission_map_id")}
    )
    private Set<PermissionMapEntity> permissions;

    @Expose
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "subject_data_subjects",
            joinColumns = {@JoinColumn(name = "subject_data_id")},
            inverseJoinColumns = {@JoinColumn(name = "subject_map_id")}
    )
    private Set<SubjectMapEntity> parents;

    @Expose
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "subject_data_options",
            joinColumns = {@JoinColumn(name = "subject_data_id")},
            inverseJoinColumns = {@JoinColumn(name = "option_map_id")}
    )
    private Set<OptionMapEntity> options;

    /**
     * SubjectDataEntity default constructor
     */
    public SubjectDataEntity() {
        this.permissions = new HashSet<>();
        this.parents = new HashSet<>();
        this.options = new HashSet<>();
    }

    /**
     * @return long
     */
    public long getSubjectDataId() {
        return this.subjectDataId;
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
