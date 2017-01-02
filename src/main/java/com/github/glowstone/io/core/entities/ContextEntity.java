package com.github.glowstone.io.core.entities;

import com.google.common.base.Preconditions;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@Entity
@org.hibernate.annotations.Entity(dynamicUpdate = true)
@Table(name = "contexts", uniqueConstraints = {
        @UniqueConstraint(columnNames = "context_id"), @UniqueConstraint(columnNames = {"type", "name"})
})
public class ContextEntity implements Serializable {

    private static final long serialVersionUID = -7242996825633478337L;

    @Id
    @Column(name = "context_id", unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int contextId;

    @Column(name = "type", nullable = false)
    private String type;

    @Column(name = "name", nullable = false)
    private String name;

    @ManyToMany(mappedBy = "contexts")
    private Set<PermissionMapEntity> permissionMappings;

    @ManyToMany(mappedBy = "contexts")
    private Set<SubjectMapEntity> subjectMappings;

    @ManyToMany(mappedBy = "contexts")
    private Set<OptionMapEntity> optionMappings;

    /**
     * ContextEntity constructor
     *
     * @param type String
     * @param name String
     */
    public ContextEntity(String type, String name) {
        Preconditions.checkNotNull(type);
        Preconditions.checkNotNull(name);

        this.type = type;
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

}
