package com.github.glowstone.io.core.permissions;

import com.github.glowstone.io.core.permissions.collections.*;
import com.github.glowstone.io.core.permissions.subjects.DefaultSubject;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import org.spongepowered.api.service.context.ContextCalculator;
import org.spongepowered.api.service.permission.*;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class GlowstonePermissionService implements PermissionService {

    public static final String DEFAULT_GROUP = "default";
    public static final String SUBJECT_PRIVILEGED = "privileged";
    public static final GlowstonePermissionService instance = new GlowstonePermissionService();

    private final ConcurrentHashMap<String, SubjectCollection> collections = new ConcurrentHashMap<>();
    private final CopyOnWriteArrayList<ContextCalculator> contextCalculators = new CopyOnWriteArrayList<>();

    /**
     * GlowstonePermissionsService constructor
     */
    private GlowstonePermissionService() {
        this.collections.put(UserSubjectCollection.instance.getIdentifier(), UserSubjectCollection.instance);
        this.collections.put(GroupSubjectCollection.instance.getIdentifier(), GroupSubjectCollection.instance);
        this.collections.put(PermissionService.SUBJECTS_SYSTEM, PrivilegedSubjectCollection.instance);
        this.collections.put(PermissionService.SUBJECTS_COMMAND_BLOCK, PrivilegedSubjectCollection.instance);
        this.collections.put(PermissionService.SUBJECTS_ROLE_TEMPLATE, PrivilegedSubjectCollection.instance);
    }

    @Override
    public SubjectCollection getUserSubjects() {
        return UserSubjectCollection.instance;
    }

    @Override
    public SubjectCollection getGroupSubjects() {
        return GroupSubjectCollection.instance;
    }

    @Override
    public Subject getDefaults() {
        return DefaultSubject.instance;
    }

    // Sponge API 4
    public SubjectData getDefaultData() {
        return DefaultSubject.instance.getSubjectData();
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
