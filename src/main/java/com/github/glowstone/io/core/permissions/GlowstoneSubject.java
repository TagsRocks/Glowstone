package com.github.glowstone.io.core.permissions;

import com.google.common.base.Preconditions;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.service.context.Context;
import org.spongepowered.api.service.permission.Subject;
import org.spongepowered.api.service.permission.SubjectCollection;
import org.spongepowered.api.service.permission.SubjectData;
import org.spongepowered.api.util.Tristate;

import java.util.*;

public class GlowstoneSubject implements Subject {

    private final String identifier;
    private final String name;
    private final String type;
    private final SubjectCollection collection;
    private final SubjectData subjectData;
    private final SubjectData transientSubjectData;
    private final Set<Context> activeContexts;

    /**
     * GlowstoneSubject constructor
     *
     * @param identifier String
     * @param name       String
     * @param type       String
     * @param collection SubjectCollection
     */
    public GlowstoneSubject(String identifier, String name, String type, SubjectCollection collection) {
        Preconditions.checkNotNull(identifier);
        Preconditions.checkNotNull(name);
        Preconditions.checkNotNull(type);

        this.identifier = identifier;
        this.name = name;
        this.type = type;
        this.collection = collection;
        this.subjectData = new GlowstoneSubjectData(this);
        this.transientSubjectData = new GlowstoneSubjectData(this);
        this.activeContexts = new HashSet<>();
    }

    @Override
    public String getIdentifier() {
        return this.identifier;
    }

    /**
     * @return String
     */
    public String getName() {
        return this.name;
    }

    /**
     * @return String
     */
    public String getType() {
        return this.type;
    }

    @Override
    public SubjectCollection getContainingCollection() {
        return this.collection;
    }

    @Override
    public SubjectData getSubjectData() {
        return this.subjectData;
    }

    @Override
    public SubjectData getTransientSubjectData() {
        return this.transientSubjectData;
    }

    @Override
    public Optional<CommandSource> getCommandSource() {
        return Optional.empty();
    }

    @Override
    public Tristate getPermissionValue(Set<Context> contexts, String permission) {
        Preconditions.checkNotNull(contexts);
        Preconditions.checkNotNull(permission);

        if (this.transientSubjectData.getPermissions(contexts).containsKey(permission)) {
            return Tristate.fromBoolean(this.transientSubjectData.getPermissions(contexts).get(permission));
        }

        if (this.subjectData.getPermissions(contexts).containsKey(permission)) {
            return Tristate.fromBoolean(this.subjectData.getPermissions(contexts).get(permission));
        }

        return Tristate.UNDEFINED;
    }

    @Override
    public boolean isChildOf(Set<Context> contexts, Subject parent) {
        Preconditions.checkNotNull(contexts);
        Preconditions.checkNotNull(parent);

        return this.transientSubjectData.getParents(contexts).contains(parent) || this.subjectData.getParents(contexts).contains(parent);
    }

    @Override
    public List<Subject> getParents(Set<Context> contexts) {
        Preconditions.checkNotNull(contexts);

        Set<Subject> parents = new HashSet<>(this.transientSubjectData.getParents(contexts));
        parents.addAll(this.subjectData.getParents(contexts));

        return new ArrayList<>(parents);
    }

    @Override
    public Optional<String> getOption(Set<Context> contexts, String key) {
        Preconditions.checkNotNull(contexts);
        Preconditions.checkNotNull(key);

        if (this.transientSubjectData.getOptions(contexts).containsKey(key)) {
            return Optional.of(this.transientSubjectData.getOptions(contexts).get(key));
        }

        if (this.subjectData.getOptions(contexts).containsKey(key)) {
            return Optional.of(this.subjectData.getOptions(contexts).get(key));
        }

        return Optional.empty();
    }

    @Override
    public Set<Context> getActiveContexts() {
        return this.activeContexts;
    }

}
