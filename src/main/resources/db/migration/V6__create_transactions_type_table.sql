CREATE SEQUENCE transaction_type_id_seq START 1;

CREATE TABLE transactions_type
(
    id   INTEGER PRIMARY KEY DEFAULT nextval('transaction_type_id_seq'),
    name varchar(50) not null
);