package com.github.glowstone.io.core.permissions.collections;

import com.github.glowstone.io.core.permissions.GlowstoneSubjectCollection;
import org.spongepowered.api.service.permission.PermissionService;
import org.spongepowered.api.service.permission.SubjectCollection;

public class UserSubjectCollection extends GlowstoneSubjectCollection implements SubjectCollection {

    public static final UserSubjectCollection instance = new UserSubjectCollection();

    /**
     * UserSubjectCollection constructor
     */
    private UserSubjectCollection() {
        super(PermissionService.SUBJECTS_USER);
    }

}
