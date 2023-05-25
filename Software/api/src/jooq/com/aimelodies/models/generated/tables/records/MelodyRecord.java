/*
 * This file is generated by jOOQ.
 */
package com.aimelodies.models.generated.tables.records;


import com.aimelodies.models.generated.tables.Melody;

import java.time.LocalDate;

import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record6;
import org.jooq.Row6;
import org.jooq.impl.UpdatableRecordImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class MelodyRecord extends UpdatableRecordImpl<MelodyRecord> implements Record6<Long, Long, Long, String, String, LocalDate> {

    private static final long serialVersionUID = 1L;

    /**
     * Setter for <code>public.melody.id</code>.
     */
    public MelodyRecord setId(Long value) {
        set(0, value);
        return this;
    }

    /**
     * Getter for <code>public.melody.id</code>.
     */
    public Long getId() {
        return (Long) get(0);
    }

    /**
     * Setter for <code>public.melody.author_id</code>.
     */
    public MelodyRecord setAuthorId(Long value) {
        set(1, value);
        return this;
    }

    /**
     * Getter for <code>public.melody.author_id</code>.
     */
    public Long getAuthorId() {
        return (Long) get(1);
    }

    /**
     * Setter for <code>public.melody.genre_id</code>.
     */
    public MelodyRecord setGenreId(Long value) {
        set(2, value);
        return this;
    }

    /**
     * Getter for <code>public.melody.genre_id</code>.
     */
    public Long getGenreId() {
        return (Long) get(2);
    }

    /**
     * Setter for <code>public.melody.audio</code>.
     */
    public MelodyRecord setAudio(String value) {
        set(3, value);
        return this;
    }

    /**
     * Getter for <code>public.melody.audio</code>.
     */
    public String getAudio() {
        return (String) get(3);
    }

    /**
     * Setter for <code>public.melody.name</code>.
     */
    public MelodyRecord setName(String value) {
        set(4, value);
        return this;
    }

    /**
     * Getter for <code>public.melody.name</code>.
     */
    public String getName() {
        return (String) get(4);
    }

    /**
     * Setter for <code>public.melody.date_added</code>.
     */
    public MelodyRecord setDateAdded(LocalDate value) {
        set(5, value);
        return this;
    }

    /**
     * Getter for <code>public.melody.date_added</code>.
     */
    public LocalDate getDateAdded() {
        return (LocalDate) get(5);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    @Override
    public Record1<Long> key() {
        return (Record1) super.key();
    }

    // -------------------------------------------------------------------------
    // Record6 type implementation
    // -------------------------------------------------------------------------

    @Override
    public Row6<Long, Long, Long, String, String, LocalDate> fieldsRow() {
        return (Row6) super.fieldsRow();
    }

    @Override
    public Row6<Long, Long, Long, String, String, LocalDate> valuesRow() {
        return (Row6) super.valuesRow();
    }

    @Override
    public Field<Long> field1() {
        return Melody.MELODY.ID;
    }

    @Override
    public Field<Long> field2() {
        return Melody.MELODY.AUTHOR_ID;
    }

    @Override
    public Field<Long> field3() {
        return Melody.MELODY.GENRE_ID;
    }

    @Override
    public Field<String> field4() {
        return Melody.MELODY.AUDIO;
    }

    @Override
    public Field<String> field5() {
        return Melody.MELODY.NAME;
    }

    @Override
    public Field<LocalDate> field6() {
        return Melody.MELODY.DATE_ADDED;
    }

    @Override
    public Long component1() {
        return getId();
    }

    @Override
    public Long component2() {
        return getAuthorId();
    }

    @Override
    public Long component3() {
        return getGenreId();
    }

    @Override
    public String component4() {
        return getAudio();
    }

    @Override
    public String component5() {
        return getName();
    }

    @Override
    public LocalDate component6() {
        return getDateAdded();
    }

    @Override
    public Long value1() {
        return getId();
    }

    @Override
    public Long value2() {
        return getAuthorId();
    }

    @Override
    public Long value3() {
        return getGenreId();
    }

    @Override
    public String value4() {
        return getAudio();
    }

    @Override
    public String value5() {
        return getName();
    }

    @Override
    public LocalDate value6() {
        return getDateAdded();
    }

    @Override
    public MelodyRecord value1(Long value) {
        setId(value);
        return this;
    }

    @Override
    public MelodyRecord value2(Long value) {
        setAuthorId(value);
        return this;
    }

    @Override
    public MelodyRecord value3(Long value) {
        setGenreId(value);
        return this;
    }

    @Override
    public MelodyRecord value4(String value) {
        setAudio(value);
        return this;
    }

    @Override
    public MelodyRecord value5(String value) {
        setName(value);
        return this;
    }

    @Override
    public MelodyRecord value6(LocalDate value) {
        setDateAdded(value);
        return this;
    }

    @Override
    public MelodyRecord values(Long value1, Long value2, Long value3, String value4, String value5, LocalDate value6) {
        value1(value1);
        value2(value2);
        value3(value3);
        value4(value4);
        value5(value5);
        value6(value6);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached MelodyRecord
     */
    public MelodyRecord() {
        super(Melody.MELODY);
    }

    /**
     * Create a detached, initialised MelodyRecord
     */
    public MelodyRecord(Long id, Long authorId, Long genreId, String audio, String name, LocalDate dateAdded) {
        super(Melody.MELODY);

        setId(id);
        setAuthorId(authorId);
        setGenreId(genreId);
        setAudio(audio);
        setName(name);
        setDateAdded(dateAdded);
    }

    /**
     * Create a detached, initialised MelodyRecord
     */
    public MelodyRecord(com.aimelodies.models.generated.tables.pojos.Melody value) {
        super(Melody.MELODY);

        if (value != null) {
            setId(value.getId());
            setAuthorId(value.getAuthorId());
            setGenreId(value.getGenreId());
            setAudio(value.getAudio());
            setName(value.getName());
            setDateAdded(value.getDateAdded());
        }
    }
}