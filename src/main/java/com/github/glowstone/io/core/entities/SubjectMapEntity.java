package com.github.glowstone.io.core.entities;

import com.google.common.collect.Maps;
import org.hibernate.annotations.DynamicUpdate;
import org.spongepowered.api.service.context.Context;
import org.spongepowered.api.service.permission.Subject;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

@Entity
@DynamicUpdate
@Table(name = "subject_map", uniqueConstraints = {
        @UniqueConstraint(columnNames = "subject_map_id")
})
public class SubjectMapEntity implements Serializable {

    private static final long serialVersionUID = -8880506885089088236L;

    @Id
    @Column(name = "subject_map_id", unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "subject_map_context",
            joinColumns = {@JoinColumn(name = "subject_map_id")},
            inverseJoinColumns = {@JoinColumn(name = "context_id")}
    )
    private Set<ContextEntity> contexts;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "subject_map_subject",
            joinColumns = {@JoinColumn(name = "subject_map_id")},
            inverseJoinColumns = {@JoinColumn(name = "subject_id")}
    )
    private Set<SubjectEntity> subjects;

    /**
     * SubjectMapEntity default constructor
     */
    public SubjectMapEntity() {
        this.contexts = new HashSet<>();
        this.subjects = new HashSet<>();
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
     * @return Set of SubjectEntity in this map
     */
    public Set<SubjectEntity> getSubjects() {
        return this.subjects;
    }

    /**
     * @return Map entry containing contexts and subjects
     */
    public Map.Entry<Set<Context>, List<Subject>> getEntry() {
        Set<Context> contexts = new HashSet<>();
        List<Subject> subjects = new CopyOnWriteArrayList<>();

        this.getContexts().forEach(c -> {
            contexts.add(c.getContext());
        });

        this.getSubjects().forEach(s -> {
            subjects.add(s.getSubject());
        });

        return Maps.immutableEntry(contexts, subjects);
    }

}
