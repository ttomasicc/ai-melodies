/*
 * This file is generated by jOOQ.
 */
package com.aimelodies.models.generated.tables.pojos;


import java.io.Serializable;
import java.time.LocalDate;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Melody implements Serializable {

    private static final long serialVersionUID = 1L;

    private final Long id;
    private final Long authorId;
    private final Long genreId;
    private final String audio;
    private final String name;
    private final LocalDate dateAdded;

    public Melody(Melody value) {
        this.id = value.id;
        this.authorId = value.authorId;
        this.genreId = value.genreId;
        this.audio = value.audio;
        this.name = value.name;
        this.dateAdded = value.dateAdded;
    }

    public Melody(
        Long id,
        Long authorId,
        Long genreId,
        String audio,
        String name,
        LocalDate dateAdded
    ) {
        this.id = id;
        this.authorId = authorId;
        this.genreId = genreId;
        this.audio = audio;
        this.name = name;
        this.dateAdded = dateAdded;
    }

    /**
     * Getter for <code>public.melody.id</code>.
     */
    public Long getId() {
        return this.id;
    }

    /**
     * Getter for <code>public.melody.author_id</code>.
     */
    public Long getAuthorId() {
        return this.authorId;
    }

    /**
     * Getter for <code>public.melody.genre_id</code>.
     */
    public Long getGenreId() {
        return this.genreId;
    }

    /**
     * Getter for <code>public.melody.audio</code>.
     */
    public String getAudio() {
        return this.audio;
    }

    /**
     * Getter for <code>public.melody.name</code>.
     */
    public String getName() {
        return this.name;
    }

    /**
     * Getter for <code>public.melody.date_added</code>.
     */
    public LocalDate getDateAdded() {
        return this.dateAdded;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final Melody other = (Melody) obj;
        if (this.id == null) {
            if (other.id != null)
                return false;
        }
        else if (!this.id.equals(other.id))
            return false;
        if (this.authorId == null) {
            if (other.authorId != null)
                return false;
        }
        else if (!this.authorId.equals(other.authorId))
            return false;
        if (this.genreId == null) {
            if (other.genreId != null)
                return false;
        }
        else if (!this.genreId.equals(other.genreId))
            return false;
        if (this.audio == null) {
            if (other.audio != null)
                return false;
        }
        else if (!this.audio.equals(other.audio))
            return false;
        if (this.name == null) {
            if (other.name != null)
                return false;
        }
        else if (!this.name.equals(other.name))
            return false;
        if (this.dateAdded == null) {
            if (other.dateAdded != null)
                return false;
        }
        else if (!this.dateAdded.equals(other.dateAdded))
            return false;
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.id == null) ? 0 : this.id.hashCode());
        result = prime * result + ((this.authorId == null) ? 0 : this.authorId.hashCode());
        result = prime * result + ((this.genreId == null) ? 0 : this.genreId.hashCode());
        result = prime * result + ((this.audio == null) ? 0 : this.audio.hashCode());
        result = prime * result + ((this.name == null) ? 0 : this.name.hashCode());
        result = prime * result + ((this.dateAdded == null) ? 0 : this.dateAdded.hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Melody (");

        sb.append(id);
        sb.append(", ").append(authorId);
        sb.append(", ").append(genreId);
        sb.append(", ").append(audio);
        sb.append(", ").append(name);
        sb.append(", ").append(dateAdded);

        sb.append(")");
        return sb.toString();
    }
}
