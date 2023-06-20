-- DROP TABLE users CASCADE;
-- DROP TABLE items CASCADE;
-- DROP TABLE bookings CASCADE;
-- DROP TABLE comments CASCADE;

CREATE TABLE IF NOT EXISTS users
(
    id       bigint GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name     varchar(50),
    email    varchar(50),
    CONSTRAINT uq_user_email UNIQUE (email)
);

CREATE TABLE IF NOT EXISTS items
(
    id                bigint GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name              varchar(150),
    description       varchar(512),
    is_available      boolean,
    owner_id          bigint,
    request_id        bigint,
    FOREIGN KEY (owner_id) REFERENCES users (id)
);

CREATE TABLE IF NOT EXISTS bookings
(
    id                bigint GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    start_date        TIMESTAMP WITHOUT TIME ZONE,
    end_date          TIMESTAMP WITHOUT TIME ZONE,
    item_id           bigint,
    booker_id         bigint,
    status            varchar(20),
    FOREIGN KEY (item_id) REFERENCES items (id),
    FOREIGN KEY (booker_id) REFERENCES users (id)
);

CREATE TABLE IF NOT EXISTS comments
(
    id                bigint GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    text              varchar(512),
    item_id           bigint,
    author_id         bigint,
    created           TIMESTAMP WITHOUT TIME ZONE,
    FOREIGN KEY (item_id) REFERENCES items (id),
    FOREIGN KEY (author_id) REFERENCES users (id)
);
