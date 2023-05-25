/* Artist Table */

CREATE TABLE IF NOT EXISTS artist
(
    id           BIGSERIAL,
    role_name    VARCHAR(30)               NOT NULL,
    username     VARCHAR(50)               NOT NULL UNIQUE,
    email        VARCHAR(70)               NOT NULL UNIQUE,
    password     VARCHAR(60)               NOT NULL,
    first_name   VARCHAR(70),
    last_name    VARCHAR(70),
    bio          TEXT,
    image        VARCHAR(50),
    date_created DATE DEFAULT CURRENT_DATE NOT NULL,

    PRIMARY KEY (id)
);