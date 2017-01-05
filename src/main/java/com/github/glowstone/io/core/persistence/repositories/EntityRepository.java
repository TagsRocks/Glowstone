package com.github.glowstone.io.core.persistence.repositories;

import com.google.common.base.Preconditions;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

public abstract class EntityRepository<E> {

    final SessionFactory sessionFactory;

    /**
     * EntityRepository constructor
     *
     * @param sessionFactory SessionFactory
     */
    EntityRepository(SessionFactory sessionFactory) {
        Preconditions.checkNotNull(sessionFactory);

        this.sessionFactory = sessionFactory;
    }

    /**
     * Save this entity
     *
     * @param entity Entity to save
     */
    public void save(E entity) {
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
    public void remove(E entity) {
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
