package com.github.glowstone.io.core.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@Entity
@org.hibernate.annotations.Entity(dynamicUpdate = true)
@Table(name = "permission_map", uniqueConstraints = {
        @UniqueConstraint(columnNames = "permission_map_id")
})
public class PermissionMapEntity implements Serializable {

    private static final long serialVersionUID = 5231783380740555286L;

    @Id
    @Column(name = "permission_map_id", unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int permissionMapId;

    @ManyToMany(cascade = {CascadeType.ALL})
    @JoinTable(name = "permission_map_context",
            joinColumns = {@JoinColumn(name = "permission_map_id")},
            inverseJoinColumns = {@JoinColumn(name = "context_id")}
    )
    private Set<ContextEntity> contexts;

    @ManyToMany(cascade = {CascadeType.ALL})
    @JoinTable(name = "permission_map_permission",
            joinColumns = {@JoinColumn(name = "permission_map_id")},
            inverseJoinColumns = {@JoinColumn(name = "permission_id")}
    )
    private Set<PermissionEntity> permissions;

    @ManyToMany(mappedBy = "permissions")
    private Set<SubjectDataEntity> subjectData;

    /**
     * PermissionMapEntity constructor
     */
    public PermissionMapEntity() {
    }

    /**
     * @return Set of ContextEntity in this map
     */
    public Set<ContextEntity> getContexts() {
        return this.contexts;
    }

    /**
     * @return Set of PermissionEntity in this map
     */
    public Set<PermissionEntity> getPermissions() {
        return this.permissions;
    }

}
