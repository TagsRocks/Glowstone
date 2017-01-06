package com.github.glowstone.io.core.listeners;

import com.github.glowstone.io.core.entities.SubjectEntity;
import com.github.glowstone.io.core.permissions.GlowstoneSubject;
import com.github.glowstone.io.core.permissions.collections.UserSubjectCollection;
import com.github.glowstone.io.core.persistence.repositories.SubjectRepository;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.network.ClientConnectionEvent;
import org.spongepowered.api.service.permission.PermissionService;

import java.util.Optional;

public class PlayerListener {

    @Listener
    public void onClientConnectionEventLogin(ClientConnectionEvent.Login event) {
        String identifier = event.getTargetUser().getIdentifier();
        GlowstoneSubject subject = (GlowstoneSubject) UserSubjectCollection.instance.get(identifier);
        if (subject == null) {
            subject = new GlowstoneSubject(identifier, event.getTargetUser().getName(), PermissionService.SUBJECTS_USER, UserSubjectCollection.instance);
            UserSubjectCollection.instance.getSubjects().put(identifier, subject);
        }
        Optional<SubjectEntity> optional = SubjectRepository.getInstance().get(identifier);
        if (!optional.isPresent()) {
            SubjectRepository.getInstance().save(subject.getSubjectEntity());
        } else {
            SubjectEntity subjectEntity = optional.get();
            if (!subjectEntity.getName().equals(event.getTargetUser().getName())) {
                subjectEntity.setName(event.getTargetUser().getName());
                SubjectRepository.getInstance().save(subjectEntity);
            }
        }
    }

}
