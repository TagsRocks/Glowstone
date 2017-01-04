package com.github.glowstone.io.core.permissions.collections;

import com.github.glowstone.io.core.permissions.GlowstoneSubjectCollection;
import org.spongepowered.api.service.permission.PermissionService;
import org.spongepowered.api.service.permission.SubjectCollection;

public class GroupSubjectCollection extends GlowstoneSubjectCollection implements SubjectCollection {

    public static final GroupSubjectCollection instance = new GroupSubjectCollection();

    /**
     * GroupSubjectCollection constructor
     */
    private GroupSubjectCollection() {
        super(PermissionService.SUBJECTS_GROUP);
    }

}
