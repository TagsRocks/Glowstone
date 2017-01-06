package com.github.glowstone.io.core.persistence.repositories;

import com.github.glowstone.io.core.entities.ContextEntity;
import com.google.common.base.Preconditions;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import javax.persistence.Query;
import java.util.List;
import java.util.Optional;

public class ContextRepository extends EntityRepository<ContextEntity> {

    private static ContextRepository instance;

    /**
     * ContextRepository constructor
     *
     * @param sessionFactory SessionFactory
     */
    public ContextRepository(SessionFactory sessionFactory) {
        super(sessionFactory);

        instance = this;
    }

    /**
     * @return the ContextRepository instance
     */
    public static ContextRepository getInstance() {
        return instance;
    }

    /**
     * Get ContextEntity by id
     *
     * @param id long
     * @return ContextEntity
     */
    public Optional<ContextEntity> get(long id) {
        Preconditions.checkNotNull(id);

        Session session = this.sessionFactory.openSession();
        session.beginTransaction();
        Query query = session.createNamedQuery("getContext", ContextEntity.class);
        query.setParameter("id", id);

        try {
            ContextEntity result = (ContextEntity) query.getSingleResult();
            session.close();
            return Optional.of(result);
        } catch (Exception e) {
            session.close();
            return Optional.empty();
        }
    }

    /**
     * Get ContextEntity by type and name
     *
     * @param type String
     * @param name String
     * @return ContextEntity
     */
    public Optional<ContextEntity> get(String type, String name) {
        Preconditions.checkNotNull(type);
        Preconditions.checkNotNull(name);

        Session session = this.sessionFactory.openSession();
        session.beginTransaction();
        Query query = session.createNamedQuery("getContextByTypeAndName", ContextEntity.class);
        query.setParameter("type", type);
        query.setParameter("name", name);

        try {
            ContextEntity result = (ContextEntity) query.getSingleResult();
            session.close();
            return Optional.of(result);
        } catch (Exception e) {
            session.close();
            return Optional.empty();
        }
    }

    /**
     * Get all ContextEntities
     *
     * @return List of ContextEntities
     */
    public List<ContextEntity> getAllContexts() {
        Session session = this.sessionFactory.openSession();
        session.beginTransaction();

        Query query = session.createNamedQuery("getAllContexts", ContextEntity.class);
        List<ContextEntity> results = query.getResultList();
        session.close();
        return results;
    }

}
