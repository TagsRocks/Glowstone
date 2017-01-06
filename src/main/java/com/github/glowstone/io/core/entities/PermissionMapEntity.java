package com.github.glowstone.io.core.entities;

import com.google.common.collect.Maps;
import org.hibernate.annotations.DynamicUpdate;
import org.spongepowered.api.service.context.Context;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Entity
@DynamicUpdate
@Table(name = "permission_map", uniqueConstraints = {
        @UniqueConstraint(columnNames = "permission_map_id")
})
public class PermissionMapEntity implements Serializable {

    private static final long serialVersionUID = 5231783380740555286L;

    @Id
    @Column(name = "permission_map_id", unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "permission_map_context",
            joinColumns = {@JoinColumn(name = "permission_map_id")},
            inverseJoinColumns = {@JoinColumn(name = "context_id")}
    )
    private Set<ContextEntity> contexts;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "permission_map_permission",
            joinColumns = {@JoinColumn(name = "permission_map_id")},
            inverseJoinColumns = {@JoinColumn(name = "permission_id")}
    )
    private Set<PermissionEntity> permissions;

    /**
     * PermissionMapEntity default constructor
     */
    public PermissionMapEntity() {
        this.contexts = new HashSet<>();
        this.permissions = new HashSet<>();
    }

    /**
     * @return long
     */
    public long getId() {
        return this.id;
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

    /**
     * @return Map entry containing contexts and permissions
     */
    public Map.Entry<Set<Context>, Map<String, Boolean>> getEntry() {
        Set<Context> contexts = new HashSet<>();
        Map<String, Boolean> permissions = new ConcurrentHashMap<>();

        this.getContexts().forEach(c -> {
            contexts.add(c.getContext());
        });

        this.getPermissions().forEach(p -> {
            permissions.put(p.getEntry().getKey(), p.getEntry().getValue());
        });

        return Maps.immutableEntry(contexts, permissions);
    }

}
