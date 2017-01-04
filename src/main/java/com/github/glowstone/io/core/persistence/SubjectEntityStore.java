package com.github.glowstone.io.core.persistence;

import com.github.glowstone.io.core.entities.SubjectEntity;
import com.github.glowstone.io.core.permissions.GlowstonePermissionService;
import com.github.glowstone.io.core.persistence.interfaces.Store;
import com.google.common.base.Preconditions;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.spongepowered.api.service.permission.PermissionService;

import javax.persistence.Query;
import java.util.List;
import java.util.Optional;

public class SubjectEntityStore extends EntityStore implements Store {

    /**
     * SubjectEntityStore constructor
     *
     * @param sessionFactory SessionFactory
     */
    public SubjectEntityStore(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    /**
     * Get a SubjectEntity by identifier
     *
     * @param identifier String
     * @return the SubjectEntity matching that identifier
     */
    public Optional<SubjectEntity> getSubjectEntity(String identifier) {
        Preconditions.checkNotNull(identifier);

        Session session = this.sessionFactory.openSession();
        session.beginTransaction();
        Query query = session.createNamedQuery("getSubjectByIdentifier", SubjectEntity.class);
        query.setParameter("identifier", identifier);

        try {
            SubjectEntity result = (SubjectEntity) query.getSingleResult();
            session.close();
            return Optional.of(result);
        } catch (Exception e) {
            session.close();
            return Optional.empty();
        }
    }

    /**
     * Get a SubjectEntity by identifier and type
     *
     * @param identifier String
     * @param type       String
     * @return the SubjectEntity matching that identifier and type
     */
    public Optional<SubjectEntity> getSubjectEntityByIdentifierAndType(String identifier, String type) {
        Preconditions.checkNotNull(identifier);
        Preconditions.checkNotNull(type);

        Session session = this.sessionFactory.openSession();
        session.beginTransaction();
        Query query = session.createNamedQuery("getSubjectByIdentifierAndType", SubjectEntity.class);
        query.setParameter("identifier", identifier);
        query.setParameter("type", type);

        try {
            SubjectEntity result = (SubjectEntity) query.getSingleResult();
            session.close();
            return Optional.of(result);
        } catch (Exception e) {
            session.close();
            return Optional.empty();
        }
    }

    /**
     * Get the SubjectEntity of the default group
     *
     * @return the SubjectEntity of the default group
     */
    public Optional<SubjectEntity> getDefaultGroup() {

        Session session = this.sessionFactory.openSession();
        session.beginTransaction();
        Query query = session.createNamedQuery("getDefaultGroup", SubjectEntity.class);
        query.setParameter("search", "default-group-%");

        try {
            SubjectEntity result = (SubjectEntity) query.getSingleResult();
            session.close();
            return Optional.of(result);
        } catch (Exception e) {
            session.close();
            return Optional.empty();
        }
    }

    /**
     * Get all SubjectEntities of a type
     *
     * @param type String
     * @return List of SubjectEntities
     */
    private List<SubjectEntity> getSubjectEntitiesByType(String type) {
        Preconditions.checkNotNull(type);

        Session session = this.sessionFactory.openSession();
        session.beginTransaction();

        Query query = session.createNamedQuery("getSubjectsByType", SubjectEntity.class);
        query.setParameter("type", type);
        List<SubjectEntity> results = query.getResultList();
        session.close();
        return results;
    }

    /**
     * Get all user SubjectEntities
     *
     * @return List of SubjectEntities
     */
    public List<SubjectEntity> getUserSubjectEntities() {
        return this.getSubjectEntitiesByType(PermissionService.SUBJECTS_USER);
    }

    /**
     * Get all group SubjectEntities
     *
     * @return List of SubjectEntities
     */
    public List<SubjectEntity> getGroupSubjectEntities() {
        return this.getSubjectEntitiesByType(PermissionService.SUBJECTS_GROUP);
    }

    /**
     * Get all system SubjectEntities
     *
     * @return List of SubjectEntities
     */
    public List<SubjectEntity> getSystemSubjectEntities() {
        return this.getSubjectEntitiesByType(PermissionService.SUBJECTS_SYSTEM);
    }

    /**
     * Get all commandblock SubjectEntities
     *
     * @return List of SubjectEntities
     */
    public List<SubjectEntity> getCommandBlockSubjectEntities() {
        return this.getSubjectEntitiesByType(PermissionService.SUBJECTS_COMMAND_BLOCK);
    }

    /**
     * Get all role-template SubjectEntities
     *
     * @return List of SubjectEntities
     */
    public List<SubjectEntity> getRoleTemplateSubjectEntities() {
        return this.getSubjectEntitiesByType(PermissionService.SUBJECTS_ROLE_TEMPLATE);
    }

    /**
     * Remove this entity
     *
     * @param entity Entity to remove
     */
    @Override
    public void remove(Object entity) {
        Preconditions.checkNotNull(entity);
        Preconditions.checkArgument(
                !(entity instanceof SubjectEntity && ((SubjectEntity) entity).getIdentifier().equals(GlowstonePermissionService.DEFAULT_GROUP))
        );

        Transaction transaction = null;
        try (Session session = this.sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.delete(entity);
            session.flush();
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
            if (transaction != null) {
                transaction.rollback();
            }
        }
    }

}
