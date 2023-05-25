/*
 * This file is generated by jOOQ.
 */
package com.aimelodies.models.generated.tables.records;


import com.aimelodies.models.generated.tables.AlbumMelody;

import org.jooq.Field;
import org.jooq.Record2;
import org.jooq.Row2;
import org.jooq.impl.UpdatableRecordImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class AlbumMelodyRecord extends UpdatableRecordImpl<AlbumMelodyRecord> implements Record2<Long, Long> {

    private static final long serialVersionUID = 1L;

    /**
     * Setter for <code>public.album_melody.album_id</code>.
     */
    public AlbumMelodyRecord setAlbumId(Long value) {
        set(0, value);
        return this;
    }

    /**
     * Getter for <code>public.album_melody.album_id</code>.
     */
    public Long getAlbumId() {
        return (Long) get(0);
    }

    /**
     * Setter for <code>public.album_melody.melody_id</code>.
     */
    public AlbumMelodyRecord setMelodyId(Long value) {
        set(1, value);
        return this;
    }

    /**
     * Getter for <code>public.album_melody.melody_id</code>.
     */
    public Long getMelodyId() {
        return (Long) get(1);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    @Override
    public Record2<Long, Long> key() {
        return (Record2) super.key();
    }

    // -------------------------------------------------------------------------
    // Record2 type implementation
    // -------------------------------------------------------------------------

    @Override
    public Row2<Long, Long> fieldsRow() {
        return (Row2) super.fieldsRow();
    }

    @Override
    public Row2<Long, Long> valuesRow() {
        return (Row2) super.valuesRow();
    }

    @Override
    public Field<Long> field1() {
        return AlbumMelody.ALBUM_MELODY.ALBUM_ID;
    }

    @Override
    public Field<Long> field2() {
        return AlbumMelody.ALBUM_MELODY.MELODY_ID;
    }

    @Override
    public Long component1() {
        return getAlbumId();
    }

    @Override
    public Long component2() {
        return getMelodyId();
    }

    @Override
    public Long value1() {
        return getAlbumId();
    }

    @Override
    public Long value2() {
        return getMelodyId();
    }

    @Override
    public AlbumMelodyRecord value1(Long value) {
        setAlbumId(value);
        return this;
    }

    @Override
    public AlbumMelodyRecord value2(Long value) {
        setMelodyId(value);
        return this;
    }

    @Override
    public AlbumMelodyRecord values(Long value1, Long value2) {
        value1(value1);
        value2(value2);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached AlbumMelodyRecord
     */
    public AlbumMelodyRecord() {
        super(AlbumMelody.ALBUM_MELODY);
    }

    /**
     * Create a detached, initialised AlbumMelodyRecord
     */
    public AlbumMelodyRecord(Long albumId, Long melodyId) {
        super(AlbumMelody.ALBUM_MELODY);

        setAlbumId(albumId);
        setMelodyId(melodyId);
    }

    /**
     * Create a detached, initialised AlbumMelodyRecord
     */
    public AlbumMelodyRecord(com.aimelodies.models.generated.tables.pojos.AlbumMelody value) {
        super(AlbumMelody.ALBUM_MELODY);

        if (value != null) {
            setAlbumId(value.getAlbumId());
            setMelodyId(value.getMelodyId());
        }
    }
}
