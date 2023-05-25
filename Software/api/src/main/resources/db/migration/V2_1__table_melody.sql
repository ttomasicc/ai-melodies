/* Melody Table */

CREATE TABLE IF NOT EXISTS melody
(
    id           BIGSERIAL,
    author_id    BIGINT,
    genre_id     BIGINT,
    audio        VARCHAR(50)                            NOT NULL,
    name         VARCHAR(100) DEFAULT 'My new melody'   NOT NULL,
    date_created TIMESTAMP    DEFAULT CURRENT_TIMESTAMP NOT NULL,

    PRIMARY KEY (id),
    FOREIGN KEY (author_id) REFERENCES artist (id)
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    FOREIGN KEY (genre_id) REFERENCES genre (id)
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
);