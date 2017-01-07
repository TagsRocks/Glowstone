package com.github.glowstone.io.core.entities;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Map;

@Entity
@DynamicUpdate
@Table(name = "options", uniqueConstraints = {
        @UniqueConstraint(columnNames = "option_id"), @UniqueConstraint(columnNames = {"key", "value"})
})
@NamedQueries({
        @NamedQuery(name = "getAllOptions", query = "from OptionEntity"),
        @NamedQuery(name = "getOption", query = "from OptionEntity o where o.id = :id"),
        @NamedQuery(name = "getOptionByKeyAndValue", query = "from OptionEntity o where o.key = :key and o.value = :value")
})
public class OptionEntity implements Serializable {

    private static final long serialVersionUID = -3167867177856489914L;

    @Id
    @Column(name = "option_id", unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "key", nullable = false)
    private String key;

    @Column(name = "value", nullable = false)
    private String value;

    /**
     * OptionEntity default constructor
     */
    public OptionEntity() {
    }

    /**
     * OptionEntity constructor
     *
     * @param key   String
     * @param value String
     */
    public OptionEntity(String key, String value) {
        Preconditions.checkNotNull(key);
        Preconditions.checkNotNull(value);

        this.key = key;
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
    public String getKey() {
        return this.key;
    }

    /**
     * @param key String
     */
    public void setKey(String key) {
        Preconditions.checkNotNull(key);

        this.key = key;
    }

    /**
     * @return String
     */
    public String getValue() {
        return this.value;
    }

    /**
     * @param value String
     */
    public void setValue(String value) {
        Preconditions.checkNotNull(value);

        this.value = value;
    }

    /**
     * @return Map entry
     */
    public Map.Entry<String, String> getEntry() {
        return Maps.immutableEntry(this.key, this.value);
    }

}
