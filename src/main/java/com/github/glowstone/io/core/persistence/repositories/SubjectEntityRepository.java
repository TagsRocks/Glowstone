package com.github.glowstone.io.core.persistence.repositories;

import com.github.glowstone.io.core.entities.SubjectEntity;
import com.github.glowstone.io.core.permissions.GlowstonePermissionService;
import com.google.common.base.Preconditions;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.spongepowered.api.service.permission.PermissionService;

import javax.persistence.Query;
import java.util.List;
import java.util.Optional;

public class SubjectEntityRepository extends EntityRepository<SubjectEntity> {

    private static SubjectEntityRepository instance;

    /**
     * SubjectEntityRepository constructor
     *
     * @param sessionFactory SessionFactory
     */
    public SubjectEntityRepository(SessionFactory sessionFactory) {
        super(sessionFactory);
        instance = this;
    }

    /**
     * @return the SubjectEntityRepository instance
     */
    public static SubjectEntityRepository getInstance() {
        return instance;
    }

    /**
     * Get a SubjectEntity by identifier
     *
     * @param identifier String
     * @return the SubjectEntity matching that identifier
     */
    public Optional<SubjectEntity> get(String identifier) {
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
     * Get the default SubjectEntity
     *
     * @return Optional<SubjectEntity>
     */
    public Optional<SubjectEntity> getDefault() {
        return this.get(GlowstonePermissionService.SUBJECT_DEFAULT);
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
     * Get all SubjectEntities
     *
     * @return List of SubjectEntities
     */
    public List<SubjectEntity> getAllSubjectEntities() {
        Session session = this.sessionFactory.openSession();
        session.beginTransaction();

        Query query = session.createNamedQuery("getAllSubjects", SubjectEntity.class);
        List<SubjectEntity> results = query.getResultList();
        session.close();
        return results;
    }

    /**
     * Remove this entity
     *
     * @param entity Entity to remove
     */
    @Override
    public void remove(SubjectEntity entity) {
        Preconditions.checkNotNull(entity);
        Preconditions.checkArgument(!entity.getIdentifier().equals(GlowstonePermissionService.SUBJECT_DEFAULT));
        super.remove(entity);
    }

}
