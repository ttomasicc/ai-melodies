/*
 * This file is generated by jOOQ.
 */
package com.aimelodies.models.generated.tables;


import com.aimelodies.models.generated.Keys;
import com.aimelodies.models.generated.Public;
import com.aimelodies.models.generated.tables.records.MelodyRecord;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Function6;
import org.jooq.Identity;
import org.jooq.Name;
import org.jooq.Record;
import org.jooq.Records;
import org.jooq.Row6;
import org.jooq.Schema;
import org.jooq.SelectField;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.TableOptions;
import org.jooq.UniqueKey;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;
import org.jooq.impl.TableImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Melody extends TableImpl<MelodyRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>public.melody</code>
     */
    public static final Melody MELODY = new Melody();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<MelodyRecord> getRecordType() {
        return MelodyRecord.class;
    }

    /**
     * The column <code>public.melody.id</code>.
     */
    public final TableField<MelodyRecord, Long> ID = createField(DSL.name("id"), SQLDataType.BIGINT.nullable(false).identity(true), this, "");

    /**
     * The column <code>public.melody.author_id</code>.
     */
    public final TableField<MelodyRecord, Long> AUTHOR_ID = createField(DSL.name("author_id"), SQLDataType.BIGINT, this, "");

    /**
     * The column <code>public.melody.genre_id</code>.
     */
    public final TableField<MelodyRecord, Long> GENRE_ID = createField(DSL.name("genre_id"), SQLDataType.BIGINT, this, "");

    /**
     * The column <code>public.melody.audio</code>.
     */
    public final TableField<MelodyRecord, String> AUDIO = createField(DSL.name("audio"), SQLDataType.VARCHAR(50).nullable(false), this, "");

    /**
     * The column <code>public.melody.name</code>.
     */
    public final TableField<MelodyRecord, String> NAME = createField(DSL.name("name"), SQLDataType.VARCHAR(100).nullable(false).defaultValue(DSL.field("'My new melody'::character varying", SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>public.melody.date_added</code>.
     */
    public final TableField<MelodyRecord, LocalDate> DATE_ADDED = createField(DSL.name("date_added"), SQLDataType.LOCALDATE.nullable(false).defaultValue(DSL.field("CURRENT_DATE", SQLDataType.LOCALDATE)), this, "");

    private Melody(Name alias, Table<MelodyRecord> aliased) {
        this(alias, aliased, null);
    }

    private Melody(Name alias, Table<MelodyRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table());
    }

    /**
     * Create an aliased <code>public.melody</code> table reference
     */
    public Melody(String alias) {
        this(DSL.name(alias), MELODY);
    }

    /**
     * Create an aliased <code>public.melody</code> table reference
     */
    public Melody(Name alias) {
        this(alias, MELODY);
    }

    /**
     * Create a <code>public.melody</code> table reference
     */
    public Melody() {
        this(DSL.name("melody"), null);
    }

    public <O extends Record> Melody(Table<O> child, ForeignKey<O, MelodyRecord> key) {
        super(child, key, MELODY);
    }

    @Override
    public Schema getSchema() {
        return aliased() ? null : Public.PUBLIC;
    }

    @Override
    public Identity<MelodyRecord, Long> getIdentity() {
        return (Identity<MelodyRecord, Long>) super.getIdentity();
    }

    @Override
    public UniqueKey<MelodyRecord> getPrimaryKey() {
        return Keys.MELODY_PKEY;
    }

    @Override
    public List<ForeignKey<MelodyRecord, ?>> getReferences() {
        return Arrays.asList(Keys.MELODY__MELODY_AUTHOR_ID_FKEY, Keys.MELODY__MELODY_GENRE_ID_FKEY);
    }

    private transient Artist _artist;
    private transient Genre _genre;

    /**
     * Get the implicit join path to the <code>public.artist</code> table.
     */
    public Artist artist() {
        if (_artist == null)
            _artist = new Artist(this, Keys.MELODY__MELODY_AUTHOR_ID_FKEY);

        return _artist;
    }

    /**
     * Get the implicit join path to the <code>public.genre</code> table.
     */
    public Genre genre() {
        if (_genre == null)
            _genre = new Genre(this, Keys.MELODY__MELODY_GENRE_ID_FKEY);

        return _genre;
    }

    @Override
    public Melody as(String alias) {
        return new Melody(DSL.name(alias), this);
    }

    @Override
    public Melody as(Name alias) {
        return new Melody(alias, this);
    }

    @Override
    public Melody as(Table<?> alias) {
        return new Melody(alias.getQualifiedName(), this);
    }

    /**
     * Rename this table
     */
    @Override
    public Melody rename(String name) {
        return new Melody(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public Melody rename(Name name) {
        return new Melody(name, null);
    }

    /**
     * Rename this table
     */
    @Override
    public Melody rename(Table<?> name) {
        return new Melody(name.getQualifiedName(), null);
    }

    // -------------------------------------------------------------------------
    // Row6 type methods
    // -------------------------------------------------------------------------

    @Override
    public Row6<Long, Long, Long, String, String, LocalDate> fieldsRow() {
        return (Row6) super.fieldsRow();
    }

    /**
     * Convenience mapping calling {@link SelectField#convertFrom(Function)}.
     */
    public <U> SelectField<U> mapping(Function6<? super Long, ? super Long, ? super Long, ? super String, ? super String, ? super LocalDate, ? extends U> from) {
        return convertFrom(Records.mapping(from));
    }

    /**
     * Convenience mapping calling {@link SelectField#convertFrom(Class,
     * Function)}.
     */
    public <U> SelectField<U> mapping(Class<U> toType, Function6<? super Long, ? super Long, ? super Long, ? super String, ? super String, ? super LocalDate, ? extends U> from) {
        return convertFrom(toType, Records.mapping(from));
    }
}