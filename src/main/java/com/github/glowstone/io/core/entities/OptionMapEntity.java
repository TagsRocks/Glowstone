package com.github.glowstone.io.core.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@Entity
@org.hibernate.annotations.Entity(dynamicUpdate = true)
@Table(name = "option_map", uniqueConstraints = {
        @UniqueConstraint(columnNames = "option_map_id")
})
public class OptionMapEntity implements Serializable {

    private static final long serialVersionUID = -6635633183090314703L;

    @Id
    @Column(name = "option_map_id", unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int optionMapId;

    @ManyToMany(cascade = {CascadeType.ALL})
    @JoinTable(name = "option_map_context",
            joinColumns = {@JoinColumn(name = "option_map_id")},
            inverseJoinColumns = {@JoinColumn(name = "context_id")}
    )
    private Set<ContextEntity> contexts;

    @ManyToMany(cascade = {CascadeType.ALL})
    @JoinTable(name = "option_map_option",
            joinColumns = {@JoinColumn(name = "option_map_id")},
            inverseJoinColumns = {@JoinColumn(name = "option_id")}
    )
    private Set<OptionEntity> options;

    @ManyToMany(mappedBy = "options")
    private Set<SubjectDataEntity> subjectData;

    /**
     * OptionMapEntity constructor
     */
    public OptionMapEntity() {
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

}
