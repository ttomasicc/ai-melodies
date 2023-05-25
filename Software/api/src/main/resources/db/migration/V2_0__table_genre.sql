/* Genre Table */

CREATE TABLE IF NOT EXISTS genre
(
    id   BIGSERIAL,
    name VARCHAR(50) NOT NULL UNIQUE,

    PRIMARY KEY (id)
);