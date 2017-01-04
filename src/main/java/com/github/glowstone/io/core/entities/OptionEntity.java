package com.github.glowstone.io.core.entities;

import com.google.common.base.Preconditions;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@org.hibernate.annotations.Entity(dynamicUpdate = true)
@Table(name = "options", uniqueConstraints = {
        @UniqueConstraint(columnNames = "option_id"), @UniqueConstraint(columnNames = {"key", "value"})
})
public class OptionEntity implements Serializable {

    private static final long serialVersionUID = -3167867177856489914L;

    @Id
    @Column(name = "option_id", unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long optionId;

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
    public long getOptionId() {
        return this.optionId;
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
    public void setName(String value) {
        Preconditions.checkNotNull(value);

        this.value = value;
    }

}
