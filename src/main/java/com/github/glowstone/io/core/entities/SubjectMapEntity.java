package com.github.glowstone.io.core.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@org.hibernate.annotations.Entity(dynamicUpdate = true)
@Table(name = "subject_map", uniqueConstraints = {
        @UniqueConstraint(columnNames = "subject_map_id")
})
public class SubjectMapEntity implements Serializable {

    private static final long serialVersionUID = -8880506885089088236L;

    @Id
    @Column(name = "subject_map_id", unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long subjectMapId;

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
    public long getSubjectMapId() {
        return this.subjectMapId;
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

}
