CREATE SEQUENCE transactions_id_seq START 1;

CREATE TABLE transactions
(
    id                  INTEGER PRIMARY KEY                    DEFAULT nextval('transactions_id_seq'),
    from_account        INTEGER references users (id) not null,
    to_account          INTEGER references users (id) not null,
    transaction_type_id INTEGER references transactions_type (id),
    amount              numeric(7, 2)                 not null,
    created_at          timestamp                     not null default now()
);