package com.github.glowstone.io.core.persistence.repositories;

import com.github.glowstone.io.core.entities.PermissionEntity;
import com.google.common.base.Preconditions;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import javax.persistence.Query;
import java.util.List;
import java.util.Optional;

public class PermissionRepository extends EntityRepository<PermissionEntity> {

    private static PermissionRepository instance;

    /**
     * PermissionRepository constructor
     *
     * @param sessionFactory SessionFactory
     */
    public PermissionRepository(SessionFactory sessionFactory) {
        super(sessionFactory);

        instance = this;
    }

    /**
     * @return the PermissionRepository instance
     */
    public static PermissionRepository getInstance() {
        return instance;
    }

    /**
     * Get PermissionEntity by id
     *
     * @param id long
     * @return PermissionEntity
     */
    public Optional<PermissionEntity> get(long id) {
        Preconditions.checkNotNull(id);

        Session session = this.sessionFactory.openSession();
        session.beginTransaction();
        Query query = session.createNamedQuery("getPermission", PermissionEntity.class);
        query.setParameter("id", id);

        try {
            PermissionEntity result = (PermissionEntity) query.getSingleResult();
            session.close();
            return Optional.of(result);
        } catch (Exception e) {
            session.close();
            return Optional.empty();
        }
    }

    /**
     * Get PermissionEntity by permission and value
     *
     * @param permission String
     * @param value      Boolean
     * @return PermissionEntity
     */
    public Optional<PermissionEntity> get(String permission, Boolean value) {
        Preconditions.checkNotNull(permission);
        Preconditions.checkNotNull(value);

        Session session = this.sessionFactory.openSession();
        session.beginTransaction();
        Query query = session.createNamedQuery("getPermissionByPermissionAndValue", PermissionEntity.class);
        query.setParameter("permission", permission);
        query.setParameter("value", value);

        try {
            PermissionEntity result = (PermissionEntity) query.getSingleResult();
            session.close();
            return Optional.of(result);
        } catch (Exception e) {
            session.close();
            return Optional.empty();
        }
    }

    /**
     * Get all PermissionEntities
     *
     * @return List of PermissionEntities
     */
    public List<PermissionEntity> getAllPermissions() {
        Session session = this.sessionFactory.openSession();
        session.beginTransaction();

        Query query = session.createNamedQuery("getAllPermissions", PermissionEntity.class);
        List<PermissionEntity> results = query.getResultList();
        session.close();
        return results;
    }

}
