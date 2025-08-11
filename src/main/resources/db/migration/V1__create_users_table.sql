CREATE SEQUENCE user_id_seq START 1;

CREATE TABLE users (
    id INTEGER PRIMARY KEY DEFAULT nextval('user_id_seq'),
    username varchar(155) not null,
    email varchar(70) not null unique,
    password varchar(16) not null,
    role varchar(30) not null,
    created_at timestamp not null
);