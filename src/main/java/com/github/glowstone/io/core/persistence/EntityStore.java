package com.github.glowstone.io.core.persistence;

import com.github.glowstone.io.core.persistence.interfaces.Store;
import com.google.common.base.Preconditions;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

public class EntityStore implements Store {

    protected static Store instance;
    protected final SessionFactory sessionFactory;

    /**
     * EntityStore constructor
     *
     * @param sessionFactory SessionFactory
     */
    public EntityStore(SessionFactory sessionFactory) {
        Preconditions.checkNotNull(sessionFactory);

        instance = this;
        this.sessionFactory = sessionFactory;
    }

    /**
     * @return get the Store instance
     */
    public static Store getInstance() {
        return instance;
    }

    /**
     * Save this entity
     *
     * @param entity Entity to save
     */
    public void save(Object entity) {
        Preconditions.checkNotNull(entity);

        Transaction transaction = null;
        try (Session session = this.sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.saveOrUpdate(entity);
            session.flush();
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
            if (transaction != null) {
                transaction.rollback();
            }
        }
    }

    /**
     * Remove this entity
     *
     * @param entity Entity to remove
     */
    public void remove(Object entity) {
        Preconditions.checkNotNull(entity);

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
