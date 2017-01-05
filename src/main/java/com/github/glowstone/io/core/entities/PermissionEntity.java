package com.github.glowstone.io.core.entities;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import com.google.gson.annotations.Expose;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Map;

@Entity
@org.hibernate.annotations.Entity(dynamicUpdate = true)
@Table(name = "permissions", uniqueConstraints = {
        @UniqueConstraint(columnNames = "permission_id"), @UniqueConstraint(columnNames = {"permission", "value"})
})
public class PermissionEntity implements Serializable {

    private static final long serialVersionUID = 7547442109758306839L;

    @Id
    @Expose(serialize = false, deserialize = false)
    @Column(name = "permission_id", unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long permissionId;

    @Expose
    @Column(name = "permission", nullable = false)
    private String permission;

    @Expose
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
    public long getPermissionId() {
        return this.permissionId;
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
