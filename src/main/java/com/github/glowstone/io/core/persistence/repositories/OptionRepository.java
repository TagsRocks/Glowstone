package com.github.glowstone.io.core.persistence.repositories;

import com.github.glowstone.io.core.entities.OptionEntity;
import com.google.common.base.Preconditions;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import javax.persistence.Query;
import java.util.List;
import java.util.Optional;

public class OptionRepository extends EntityRepository<OptionEntity> {

    private static OptionRepository instance;

    /**
     * OptionRepository constructor
     *
     * @param sessionFactory SessionFactory
     */
    public OptionRepository(SessionFactory sessionFactory) {
        super(sessionFactory);

        instance = this;
    }

    public static OptionRepository getInstance() {
        return instance;
    }

    /**
     * Get OptionEntity by id
     *
     * @param id long
     * @return OptionEntity
     */
    public Optional<OptionEntity> get(long id) {
        Preconditions.checkNotNull(id);

        Session session = this.sessionFactory.openSession();
        session.beginTransaction();
        Query query = session.createNamedQuery("getOption", OptionEntity.class);
        query.setParameter("id", id);

        try {
            OptionEntity result = (OptionEntity) query.getSingleResult();
            session.close();
            return Optional.of(result);
        } catch (Exception e) {
            session.close();
            return Optional.empty();
        }
    }

    /**
     * Get a OptionEntity by key and value
     *
     * @param key   String
     * @param value String
     * @return OptionEntity
     */
    public Optional<OptionEntity> get(String key, String value) {
        Preconditions.checkNotNull(key);
        Preconditions.checkNotNull(value);

        Session session = this.sessionFactory.openSession();
        session.beginTransaction();
        Query query = session.createNamedQuery("getOptionByKeyAndValue", OptionEntity.class);
        query.setParameter("key", key);
        query.setParameter("value", value);

        try {
            OptionEntity result = (OptionEntity) query.getSingleResult();
            session.close();
            return Optional.of(result);
        } catch (Exception e) {
            session.close();
            return Optional.empty();
        }
    }

    /**
     * Get all OptionEntities
     *
     * @return List of OptionEntities
     */
    public List<OptionEntity> getAllContexts() {
        Session session = this.sessionFactory.openSession();
        session.beginTransaction();

        Query query = session.createNamedQuery("getAllOptions", OptionEntity.class);
        List<OptionEntity> results = query.getResultList();
        session.close();
        return results;
    }

}
