CREATE SEQUENCE card_id_seq START 1;

CREATE TABLE cards
(
    id              INTEGER PRIMARY KEY DEFAULT nextval('card_id_seq'),
    account_id      INTEGER references accounts (id),
    card_number     varchar(16) not null,
    expiration_date timestamp   not null,
    cvv_hash        INTEGER     not null,
    card_type       INTEGER not null references cards_type(id),
    status          varchar(50) not null
);