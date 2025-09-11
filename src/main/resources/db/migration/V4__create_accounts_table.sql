CREATE SEQUENCE account_id_seq START 1;

CREATE TABLE accounts
(
    id          INTEGER PRIMARY KEY DEFAULT nextval('account_id_seq'),
    user_id     INTEGER UNIQUE REFERENCES users (id),
    iban        varchar(255) unique not null,
    currency_id INTEGER references currencies (id),
    balance     numeric(7, 2)       not null,
    status      varchar(50)         not null,
    created_at  timestamp           not null
);