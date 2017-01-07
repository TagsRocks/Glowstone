package com.github.glowstone.io.core.entities;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Map;

@Entity
@DynamicUpdate
@Table(name = "permissions", uniqueConstraints = {
        @UniqueConstraint(columnNames = "permission_id"), @UniqueConstraint(columnNames = {"permission", "value"})
})
@NamedQueries({
        @NamedQuery(name = "getAllPermissions", query = "from PermissionEntity"),
        @NamedQuery(name = "getPermission", query = "from PermissionEntity p where p.id = :id"),
        @NamedQuery(name = "getPermissionByPermissionAndValue", query = "from PermissionEntity p where p.permission = :permission and p.value = :value")
})
public class PermissionEntity implements Serializable {

    private static final long serialVersionUID = 7547442109758306839L;

    @Id
    @Column(name = "permission_id", unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "permission", nullable = false)
    private String permission;

    @Column(name = "value", nullable = false)
    private boolean value;

    /**
     * PermissionEntity default constructor
     */
    public PermissionEntity() {
    }

    /**
     * PermissionEntity constructor
     *
     * @param permission String
     * @param value      boolean
     */
    public PermissionEntity(String permission, boolean value) {
        Preconditions.checkNotNull(permission);
        Preconditions.checkNotNull(value);

        this.permission = permission;
        this.value = value;
    }

    /**
     * @return long
     */
    public long getId() {
        return this.id;
    }

    /**
     * @param id long
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * @return String
     */
    public String getPermission() {
        return this.permission;
    }

    /**
     * @param permission String
     */
    public void setPermission(String permission) {
        Preconditions.checkNotNull(permission);

        this.permission = permission;
    }

    /**
     * @return boolean
     */
    public boolean getValue() {
        return this.value;
    }

    /**
     * @param value boolean
     */
    public void setValue(boolean value) {
        Preconditions.checkNotNull(value);

        this.value = value;
    }

    /**
     * @return Map entry
     */
    public Map.Entry<String, Boolean> getEntry() {
        return Maps.immutableEntry(this.permission, this.value);
    }

}
