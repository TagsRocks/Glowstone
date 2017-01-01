package com.github.glowstone.io.core.permissions;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import org.spongepowered.api.service.context.ContextCalculator;
import org.spongepowered.api.service.permission.PermissionDescription;
import org.spongepowered.api.service.permission.PermissionService;
import org.spongepowered.api.service.permission.Subject;
import org.spongepowered.api.service.permission.SubjectCollection;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class GlowstonePermissionService implements PermissionService {

    public static final String DEFAULT_GROUP = "default";
    public static GlowstonePermissionService instance = null;

    private final ConcurrentHashMap<String, SubjectCollection> collections = new ConcurrentHashMap<>();
    private final CopyOnWriteArrayList<ContextCalculator> contextCalculators = new CopyOnWriteArrayList<>();

    /**
     * GlowstonePermissionsService constructor
     *
     * @param users  SubjectCollection
     * @param groups SubjectCollection
     */
    private GlowstonePermissionService(SubjectCollection users, SubjectCollection groups) {
        Preconditions.checkNotNull(users);
        Preconditions.checkNotNull(groups);

        instance = this;
        this.collections.put(PermissionService.SUBJECTS_USER, users);
        this.collections.put(PermissionService.SUBJECTS_GROUP, groups);
    }

    @Override
    public SubjectCollection getUserSubjects() {
        return this.collections.get(PermissionService.SUBJECTS_USER);
    }

    @Override
    public SubjectCollection getGroupSubjects() {
        return this.collections.get(PermissionService.SUBJECTS_GROUP);
    }

    @Override
    public Subject getDefaults() {
        if (this.getGroupSubjects().hasRegistered(DEFAULT_GROUP)) {
            return this.getGroupSubjects().get(DEFAULT_GROUP);
        }

        return null;
    }

    @Override
    public SubjectCollection getSubjects(String identifier) {
        Preconditions.checkNotNull(identifier);

        if (!this.collections.containsKey(identifier)) {
            this.collections.put(identifier, new GlowstoneSubjectCollection(identifier));
        }

        return collections.get(identifier);
    }

    @Override
    public Map<String, SubjectCollection> getKnownSubjects() {
        return new HashMap<>(collections);
    }

    @Override
    public Optional<PermissionDescription.Builder> newDescriptionBuilder(Object plugin) {
        // TODO
        return Optional.empty();
    }

    @Override
    public Optional<PermissionDescription> getDescription(String permission) {
        // TODO
        return Optional.empty();
    }

    @Override
    public Collection<PermissionDescription> getDescriptions() {
        // TODO
        return new ArrayList<>();
    }

    @Override
    public void registerContextCalculator(ContextCalculator<Subject> calculator) {
        Preconditions.checkNotNull(calculator);

        if (!this.contextCalculators.contains(calculator)) {
            this.contextCalculators.add(calculator);
        }
    }

    /**
     * @return List<ContextCalculator>
     */
    public List<ContextCalculator> getContextCalculators() {
        return ImmutableList.copyOf(this.contextCalculators);
    }

}
