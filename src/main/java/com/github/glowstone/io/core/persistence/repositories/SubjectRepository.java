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

public class SubjectRepository extends EntityRepository<SubjectEntity> {

    private static SubjectRepository instance;

    /**
     * SubjectRepository constructor
     *
     * @param sessionFactory SessionFactory
     */
    public SubjectRepository(SessionFactory sessionFactory) {
        super(sessionFactory);
        instance = this;
    }

    /**
     * @return the SubjectRepository instance
     */
    public static SubjectRepository getInstance() {
        return instance;
    }

    /**
     * Get a SubjectEntity by id
     *
     * @param id long
     * @return the SubjectEntity by id
     */
    public Optional<SubjectEntity> get(long id) {
        Preconditions.checkNotNull(id);

        Session session = this.sessionFactory.openSession();
        session.beginTransaction();
        Query query = session.createNamedQuery("getSubject", SubjectEntity.class);
        query.setParameter("id", id);

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
    public Optional<SubjectEntity> getSubjectByIdentifierAndType(String identifier, String type) {
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
    private List<SubjectEntity> getSubjectsByType(String type) {
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
    public List<SubjectEntity> getUserSubjects() {
        return this.getSubjectsByType(PermissionService.SUBJECTS_USER);
    }

    /**
     * Get all group SubjectEntities
     *
     * @return List of SubjectEntities
     */
    public List<SubjectEntity> getGroupSubjects() {
        return this.getSubjectsByType(PermissionService.SUBJECTS_GROUP);
    }

    /**
     * Get all SubjectEntities
     *
     * @return List of SubjectEntities
     */
    public List<SubjectEntity> getAllSubjects() {
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
     * @return result of the removal
     */
    @Override
    public boolean remove(SubjectEntity entity) {
        Preconditions.checkNotNull(entity);
        Preconditions.checkArgument(!entity.getIdentifier().equals(GlowstonePermissionService.SUBJECT_DEFAULT));
        return super.remove(entity);
    }

}
