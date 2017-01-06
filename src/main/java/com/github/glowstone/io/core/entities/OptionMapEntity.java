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
@Table(name = "option_map", uniqueConstraints = {
        @UniqueConstraint(columnNames = "option_map_id")
})
public class OptionMapEntity implements Serializable {

    private static final long serialVersionUID = -6635633183090314703L;

    @Id
    @Column(name = "option_map_id", unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "option_map_context",
            joinColumns = {@JoinColumn(name = "option_map_id")},
            inverseJoinColumns = {@JoinColumn(name = "context_id")}
    )
    private Set<ContextEntity> contexts;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "option_map_option",
            joinColumns = {@JoinColumn(name = "option_map_id")},
            inverseJoinColumns = {@JoinColumn(name = "option_id")}
    )
    private Set<OptionEntity> options;

    /**
     * OptionMapEntity default constructor
     */
    public OptionMapEntity() {
        this.contexts = new HashSet<>();
        this.options = new HashSet<>();
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
     * @return Set of OptionEntity in this map
     */
    public Set<OptionEntity> getOptions() {
        return this.options;
    }

    /**
     * @return Map entry containing contexts and options
     */
    public Map.Entry<Set<Context>, Map<String, String>> getEntry() {
        Set<Context> contexts = new HashSet<>();
        Map<String, String> options = new ConcurrentHashMap<>();

        this.getContexts().forEach(c -> {
            contexts.add(c.getContext());
        });

        this.getOptions().forEach(o -> {
            options.put(o.getEntry().getKey(), o.getEntry().getValue());
        });

        return Maps.immutableEntry(contexts, options);
    }

}
