/*
 * This file is generated by jOOQ.
 */
package com.aimelodies.models.generated.tables.pojos;


import java.io.Serializable;
import java.time.LocalDateTime;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Shedlock implements Serializable {

    private static final long serialVersionUID = 1L;

    private final String name;
    private final LocalDateTime lockUntil;
    private final LocalDateTime lockedAt;
    private final String lockedBy;

    public Shedlock(Shedlock value) {
        this.name = value.name;
        this.lockUntil = value.lockUntil;
        this.lockedAt = value.lockedAt;
        this.lockedBy = value.lockedBy;
    }

    public Shedlock(
        String name,
        LocalDateTime lockUntil,
        LocalDateTime lockedAt,
        String lockedBy
    ) {
        this.name = name;
        this.lockUntil = lockUntil;
        this.lockedAt = lockedAt;
        this.lockedBy = lockedBy;
    }

    /**
     * Getter for <code>public.shedlock.name</code>.
     */
    public String getName() {
        return this.name;
    }

    /**
     * Getter for <code>public.shedlock.lock_until</code>.
     */
    public LocalDateTime getLockUntil() {
        return this.lockUntil;
    }

    /**
     * Getter for <code>public.shedlock.locked_at</code>.
     */
    public LocalDateTime getLockedAt() {
        return this.lockedAt;
    }

    /**
     * Getter for <code>public.shedlock.locked_by</code>.
     */
    public String getLockedBy() {
        return this.lockedBy;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final Shedlock other = (Shedlock) obj;
        if (this.name == null) {
            if (other.name != null)
                return false;
        }
        else if (!this.name.equals(other.name))
            return false;
        if (this.lockUntil == null) {
            if (other.lockUntil != null)
                return false;
        }
        else if (!this.lockUntil.equals(other.lockUntil))
            return false;
        if (this.lockedAt == null) {
            if (other.lockedAt != null)
                return false;
        }
        else if (!this.lockedAt.equals(other.lockedAt))
            return false;
        if (this.lockedBy == null) {
            if (other.lockedBy != null)
                return false;
        }
        else if (!this.lockedBy.equals(other.lockedBy))
            return false;
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.name == null) ? 0 : this.name.hashCode());
        result = prime * result + ((this.lockUntil == null) ? 0 : this.lockUntil.hashCode());
        result = prime * result + ((this.lockedAt == null) ? 0 : this.lockedAt.hashCode());
        result = prime * result + ((this.lockedBy == null) ? 0 : this.lockedBy.hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Shedlock (");

        sb.append(name);
        sb.append(", ").append(lockUntil);
        sb.append(", ").append(lockedAt);
        sb.append(", ").append(lockedBy);

        sb.append(")");
        return sb.toString();
    }
}
