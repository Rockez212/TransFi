CREATE SEQUENCE status_id_seq START 1;

CREATE TABLE statuses
(
    id   INTEGER PRIMARY KEY DEFAULT nextval('status_id_seq'),
    status_type varchar(155) not null
);