package com.github.glowstone.io.core.persistence.interfaces;

public interface Store {

    /**
     * Save this entity
     *
     * @param entity Entity to save
     */
    void save(Object entity);

    /**
     * Remove this entity
     *
     * @param entity Entity to remove
     */
    void remove(Object entity);

}
