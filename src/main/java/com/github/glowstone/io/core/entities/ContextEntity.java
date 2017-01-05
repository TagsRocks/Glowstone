package com.github.glowstone.io.core.entities;

import com.google.common.base.Preconditions;
import com.google.gson.annotations.Expose;
import org.spongepowered.api.service.context.Context;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@org.hibernate.annotations.Entity(dynamicUpdate = true)
@Table(name = "contexts", uniqueConstraints = {
        @UniqueConstraint(columnNames = "context_id"), @UniqueConstraint(columnNames = {"type", "name"})
})
public class ContextEntity implements Serializable {

    private static final long serialVersionUID = -7242996825633478337L;

    @Id
    @Expose(serialize = false, deserialize = false)
    @Column(name = "context_id", unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long contextId;

    @Expose
    @Column(name = "type", nullable = false)
    private String type;

    @Expose
    @Column(name = "name", nullable = false)
    private String name;

    /**
     * ContextEntity default constructor
     */
    public ContextEntity() {
    }

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
     * @return long
     */
    public long getContextId() {
        return this.contextId;
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

    /**
     * @return Context
     */
    public Context getContext() {
        return new Context(this.type, this.name);
    }

}
