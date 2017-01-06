package com.github.glowstone.io.core.entities;

import com.google.common.base.Preconditions;
import org.hibernate.annotations.DynamicUpdate;
import org.spongepowered.api.service.context.Context;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@DynamicUpdate
@Table(name = "contexts", uniqueConstraints = {
        @UniqueConstraint(columnNames = "context_id"), @UniqueConstraint(columnNames = {"type", "name"})
})
@NamedQueries({
        @NamedQuery(name = "getAllContexts", query = "from ContextEntity"),
        @NamedQuery(name = "getContext", query = "from ContextEntity c where c.id = :id"),
        @NamedQuery(name = "getContextByTypeAndName", query = "from ContextEntity c where c.type = :type and c.name = :name")
})
public class ContextEntity implements Serializable {

    private static final long serialVersionUID = -7242996825633478337L;

    @Id
    @Column(name = "context_id", unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "type", nullable = false)
    private String type;

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
    public long getId() {
        return this.id;
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
