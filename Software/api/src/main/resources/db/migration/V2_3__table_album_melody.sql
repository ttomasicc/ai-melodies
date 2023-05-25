/* Album Melody mapping Table */

CREATE TABLE IF NOT EXISTS album_melody
(
    album_id  BIGINT,
    melody_id BIGINT,

    PRIMARY KEY (album_id, melody_id),
    FOREIGN KEY (album_id) REFERENCES album (id)
        ON UPDATE NO ACTION
        ON DELETE CASCADE,
    FOREIGN KEY (melody_id) REFERENCES melody (id)
        ON UPDATE NO ACTION
        ON DELETE CASCADE
);