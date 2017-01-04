package com.github.glowstone.io.core.permissions.subjects;

import com.github.glowstone.io.core.permissions.GlowstoneSubject;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.service.context.Context;
import org.spongepowered.api.service.permission.Subject;
import org.spongepowered.api.service.permission.SubjectCollection;
import org.spongepowered.api.util.Tristate;

import java.util.Optional;
import java.util.Set;

public class PrivilegedSubject extends GlowstoneSubject implements Subject {

    /**
     * PrivilegedSubject constructor
     *
     * @param identifier String
     * @param collection SubjectCollection
     */
    public PrivilegedSubject(String identifier, SubjectCollection collection) {
        super(identifier, "", "privileged", collection);
    }

    @Override
    public Optional<CommandSource> getCommandSource() {
        return Optional.of(Sponge.getServer().getConsole());
    }

    @Override
    public Tristate getPermissionValue(Set<Context> set, String s) {
        return Tristate.TRUE;
    }

    @Override
    public Optional<String> getOption(Set<Context> set, String s) {
        return Optional.empty();
    }

}
