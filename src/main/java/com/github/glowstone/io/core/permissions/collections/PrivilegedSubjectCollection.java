package com.github.glowstone.io.core.permissions.collections;

import com.github.glowstone.io.core.permissions.GlowstonePermissionService;
import com.github.glowstone.io.core.permissions.GlowstoneSubjectCollection;
import com.github.glowstone.io.core.permissions.subjects.DefaultSubject;
import com.github.glowstone.io.core.permissions.subjects.PrivilegedSubject;
import com.google.common.base.Preconditions;
import org.spongepowered.api.service.context.Context;
import org.spongepowered.api.service.permission.Subject;
import org.spongepowered.api.service.permission.SubjectCollection;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class PrivilegedSubjectCollection extends GlowstoneSubjectCollection implements SubjectCollection {

    public static final PrivilegedSubjectCollection instance = new PrivilegedSubjectCollection();

    /**
     * PrivilegedSubjectCollection constructor
     */
    private PrivilegedSubjectCollection() {
        super(GlowstonePermissionService.SUBJECT_PRIVILEGED);
    }

    @Override
    public Subject get(String identifier) {
        Preconditions.checkNotNull(identifier);

        if (identifier.equals(DefaultSubject.instance.getIdentifier())) {
            return DefaultSubject.instance;
        }

        return new PrivilegedSubject(identifier, this);
    }

    @Override
    public boolean hasRegistered(String identifier) {
        return true;
    }

    @Override
    public Iterable<Subject> getAllSubjects() {
        return Arrays.asList(DefaultSubject.instance);
    }

    @Override
    public Map<Subject, Boolean> getAllWithPermission(String permission) {
        return new HashMap<>();
    }

    @Override
    public Map<Subject, Boolean> getAllWithPermission(Set<Context> contexts, String permission) {
        return new HashMap<>();
    }

    @Override
    public Subject getDefaults() {
        return new PrivilegedSubject("", this);
    }

}
