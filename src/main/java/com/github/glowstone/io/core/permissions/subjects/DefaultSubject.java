package com.github.glowstone.io.core.permissions.subjects;

import com.github.glowstone.io.core.permissions.GlowstonePermissionService;
import com.github.glowstone.io.core.permissions.GlowstoneSubject;
import com.github.glowstone.io.core.permissions.collections.PrivilegedSubjectCollection;
import org.spongepowered.api.service.permission.PermissionService;
import org.spongepowered.api.service.permission.Subject;

public class DefaultSubject extends GlowstoneSubject implements Subject {

    public static final DefaultSubject instance = new DefaultSubject();

    /**
     * DefaultSubject constructor
     */
    public DefaultSubject() {
        super(GlowstonePermissionService.DEFAULT_GROUP, GlowstonePermissionService.DEFAULT_GROUP, PermissionService.SUBJECTS_GROUP,
                PrivilegedSubjectCollection.instance);
    }
}