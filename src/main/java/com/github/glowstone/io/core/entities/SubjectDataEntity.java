package com.github.glowstone.io.core.entities;

import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@DynamicUpdate
@Table(name = "subject_data", uniqueConstraints = {
        @UniqueConstraint(columnNames = "subject_data_id")
})
public class SubjectDataEntity implements Serializable {

    private static final long serialVersionUID = -3317877691585905050L;

    @Id
    @Column(name = "subject_data_id", unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "subject_data_permissions",
            joinColumns = {@JoinColumn(name = "subject_data_id")},
            inverseJoinColumns = {@JoinColumn(name = "permission_map_id")}
    )
    private Set<PermissionMapEntity> permissions;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "subject_data_subjects",
            joinColumns = {@JoinColumn(name = "subject_data_id")},
            inverseJoinColumns = {@JoinColumn(name = "subject_map_id")}
    )
    private Set<SubjectMapEntity> parents;

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
    public long getId() {
        return this.id;
    }

    /**
     * @param id long
     */
    public void setId(long id) {
        this.id = id;
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
