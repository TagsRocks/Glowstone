package com.github.glowstone.io.core.permissions;

import com.github.glowstone.io.core.permissions.subjects.DefaultSubject;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import org.spongepowered.api.service.context.Context;
import org.spongepowered.api.service.permission.Subject;
import org.spongepowered.api.service.permission.SubjectCollection;
import org.spongepowered.api.service.permission.SubjectData;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class GlowstoneSubjectCollection implements SubjectCollection {

    private final String identifier;
    private final Map<String, Subject> subjects = new ConcurrentHashMap<>();

    /**
     * GlowstoneSubjectCollection constructor
     *
     * @param identifier String
     */
    public GlowstoneSubjectCollection(String identifier) {
        Preconditions.checkNotNull(identifier);

        this.identifier = identifier;
    }

    /**
     * Get all subjects
     *
     * @return Map of subjects in this collection
     */
    public Map<String, Subject> getSubjects() {
        return this.subjects;
    }

    @Override
    public String getIdentifier() {
        return this.identifier;
    }

    @Override
    public Subject get(String identifier) {
        Preconditions.checkNotNull(identifier);

        if (this.subjects.containsKey(identifier)) {
            return this.subjects.get(identifier);
        }

        return null;
    }

    @Override
    public Iterable<Subject> getAllSubjects() {
        return ImmutableList.copyOf(this.subjects.values());
    }

    @Override
    public boolean hasRegistered(String identifier) {
        Preconditions.checkNotNull(identifier);

        return this.subjects.containsKey(identifier);
    }

    @Override
    public Map<Subject, Boolean> getAllWithPermission(String permission) {
        Preconditions.checkNotNull(permission);

        return this.getAllWithPermission(SubjectData.GLOBAL_CONTEXT, permission);
    }

    @Override
    public Map<Subject, Boolean> getAllWithPermission(Set<Context> contexts, String permission) {
        Preconditions.checkNotNull(contexts);
        Preconditions.checkNotNull(permission);

        Map<Subject, Boolean> subjectsWithPermission = new ConcurrentHashMap<>();
        getAllSubjects().forEach(subject -> {
            if (subject.hasPermission(contexts, permission)) {
                subjectsWithPermission.put(subject, subject.getPermissionValue(contexts, permission).asBoolean());
            }
        });

        return subjectsWithPermission;
    }

    @Override
    public Subject getDefaults() {
        return DefaultSubject.instance;
    }
}
