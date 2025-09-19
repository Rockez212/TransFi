CREATE SEQUENCE card_id_seq START 1;

CREATE TABLE cards
(
    id              INTEGER PRIMARY KEY DEFAULT nextval('card_id_seq'),
    account_id      INTEGER references accounts (id),
    card_balance    numeric(7, 2) not null,
    card_number     varchar(16)   not null,
    expiration_date date          not null,
    cvv_hash        INTEGER       not null,
    card_type       varchar(50)  not null,
    status_type      varchar(50)   not null,
    currency_type   varchar(50)   not null
);