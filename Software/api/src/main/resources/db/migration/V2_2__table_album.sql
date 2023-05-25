/* Album Table */

CREATE TABLE IF NOT EXISTS album
(
    id           BIGSERIAL,
    artist_id    BIGINT,
    image        VARCHAR(50),
    title        VARCHAR(100) DEFAULT 'My new album' NOT NULL,
    date_created DATE         DEFAULT CURRENT_DATE   NOT NULL,
    description  TEXT,

    PRIMARY KEY (id),
    FOREIGN KEY (artist_id) REFERENCES artist (id)
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
);