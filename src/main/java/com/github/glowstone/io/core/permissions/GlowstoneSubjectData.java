package com.github.glowstone.io.core.permissions;

import com.github.glowstone.io.core.entities.*;
import com.google.common.base.Preconditions;
import org.spongepowered.api.service.context.Context;
import org.spongepowered.api.service.permission.Subject;
import org.spongepowered.api.service.permission.SubjectData;
import org.spongepowered.api.util.Tristate;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class GlowstoneSubjectData implements SubjectData {

    protected final Subject subject;
    protected final Map<String, Boolean> globalPermissions = new ConcurrentHashMap<>();
    protected final Map<String, String> globalOptions = new ConcurrentHashMap<>();
    protected final Map<Set<Context>, Map<String, Boolean>> permissions = new ConcurrentHashMap<>();
    protected final Map<Set<Context>, List<Subject>> parents = new ConcurrentHashMap<>();
    protected final Map<Set<Context>, Map<String, String>> options = new ConcurrentHashMap<>();

    /**
     * GlowstoneSubjectData constructor
     *
     * @param subject Subject
     */
    public GlowstoneSubjectData(Subject subject) {
        this.subject = subject;
        this.permissions.put(GLOBAL_CONTEXT, this.globalPermissions);
        this.options.put(GLOBAL_CONTEXT, this.globalOptions);
    }

    @Override
    public Map<Set<Context>, Map<String, Boolean>> getAllPermissions() {
        return this.permissions;
    }

    @Override
    public Map<String, Boolean> getPermissions(Set<Context> contexts) {
        Preconditions.checkNotNull(contexts);

        if (this.permissions.containsKey(contexts)) {
            return this.permissions.get(contexts);
        }

        return Collections.emptyMap();
    }

    @Override
    public boolean setPermission(Set<Context> contexts, String permission, Tristate value) {
        Preconditions.checkNotNull(contexts);
        Preconditions.checkNotNull(permission);
        Preconditions.checkNotNull(value);

        if (value != Tristate.UNDEFINED) {

            Map<String, Boolean> contextPermissions = (this.permissions.containsKey(contexts)) ? this.permissions.get(contexts) : new ConcurrentHashMap<>();
            contextPermissions.put(permission, value.asBoolean());
            this.permissions.put(contexts, contextPermissions);

        } else if (this.permissions.containsKey(contexts)) {

            if (this.permissions.get(contexts).containsKey(permission)) {
                this.permissions.get(contexts).remove(permission);
            }
        }

        return true;
    }

    @Override
    public boolean clearPermissions() {
        this.permissions.clear();
        this.globalPermissions.clear();
        this.permissions.put(GLOBAL_CONTEXT, this.globalPermissions);
        return true;
    }

    @Override
    public boolean clearPermissions(Set<Context> contexts) {
        Preconditions.checkNotNull(contexts);

        if (this.permissions.containsKey(contexts)) {
            this.permissions.get(contexts).clear();
        }

        return true;
    }

    @Override
    public Map<Set<Context>, List<Subject>> getAllParents() {
        return this.parents;
    }

    @Override
    public List<Subject> getParents(Set<Context> contexts) {
        Preconditions.checkNotNull(contexts);

        if (this.parents.containsKey(contexts)) {
            return this.parents.get(contexts);
        }

        return Collections.emptyList();
    }

    @Override
    public boolean addParent(Set<Context> contexts, Subject parent) {
        Preconditions.checkNotNull(contexts);
        Preconditions.checkNotNull(parent);

        List<Subject> contextParents = (this.parents.containsKey(contexts)) ? this.parents.get(contexts) : new CopyOnWriteArrayList<>();
        if (!contextParents.contains(parent)) {
            contextParents.add(parent);
        }
        this.parents.put(contexts, contextParents);

        return true;
    }

    @Override
    public boolean removeParent(Set<Context> contexts, Subject parent) {
        Preconditions.checkNotNull(contexts);
        Preconditions.checkNotNull(parent);

        if (this.parents.containsKey(contexts)) {
            this.parents.get(contexts).remove(parent);
        }

        return true;
    }

    @Override
    public boolean clearParents() {
        this.parents.clear();
        return true;
    }

    @Override
    public boolean clearParents(Set<Context> contexts) {
        Preconditions.checkNotNull(contexts);

        if (this.parents.containsKey(contexts)) {
            this.parents.get(contexts).clear();
        }

        return true;
    }

    @Override
    public Map<Set<Context>, Map<String, String>> getAllOptions() {
        return this.options;
    }

    @Override
    public Map<String, String> getOptions(Set<Context> contexts) {
        Preconditions.checkNotNull(contexts);

        if (this.options.containsKey(contexts)) {
            return this.options.get(contexts);
        }

        return Collections.emptyMap();
    }

    @Override
    public boolean setOption(Set<Context> contexts, String key, String value) {
        Preconditions.checkNotNull(contexts);
        Preconditions.checkNotNull(key);
        Preconditions.checkNotNull(value);

        Map<String, String> contextOptions = (this.options.containsKey(contexts)) ? this.options.get(contexts) : new ConcurrentHashMap<>();
        contextOptions.put(key, value);
        this.options.put(contexts, contextOptions);

        return true;
    }

    @Override
    public boolean clearOptions(Set<Context> contexts) {
        Preconditions.checkNotNull(contexts);

        if (this.parents.containsKey(contexts)) {
            this.parents.get(contexts).clear();
        }

        return true;
    }

    @Override
    public boolean clearOptions() {
        this.options.clear();
        this.globalOptions.clear();
        this.options.put(GLOBAL_CONTEXT, this.globalOptions);
        return true;
    }

    /**
     * @return SubjectDataEntity prepared for saving
     */
    public SubjectDataEntity prepare() {
        SubjectDataEntity subjectDataEntity = new SubjectDataEntity();

        this.permissions.forEach((contexts, permissions) -> {
            PermissionMapEntity map = new PermissionMapEntity();
            contexts.forEach(context -> map.getContexts().add(new ContextEntity(context.getType(), context.getName())));
            permissions.forEach((permission, value) -> map.getPermissions().add(new PermissionEntity(permission, value)));
            subjectDataEntity.getAllPermissions().add(map);
        });

        this.parents.forEach((contexts, parents) -> {
            SubjectMapEntity map = new SubjectMapEntity();
            contexts.forEach(context -> map.getContexts().add(new ContextEntity(context.getType(), context.getName())));
            parents.forEach(subject ->
                    map.getSubjects().add(
                            new SubjectEntity(subject.getIdentifier(), ((GlowstoneSubject) subject).getName(), ((GlowstoneSubject) subject).getType())
                    )
            );
            subjectDataEntity.getAllParents().add(map);
        });

        this.options.forEach((contexts, options) -> {
            OptionMapEntity map = new OptionMapEntity();
            contexts.forEach(context -> map.getContexts().add(new ContextEntity(context.getType(), context.getName())));
            options.forEach((key, value) -> map.getOptions().add(new OptionEntity(key, value)));
            subjectDataEntity.getAllOptions().add(map);
        });

        return subjectDataEntity;
    }
}
