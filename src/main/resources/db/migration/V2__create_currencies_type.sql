CREATE SEQUENCE currency_id_seq START 1;

CREATE TABLE currencies
(
    id   INTEGER PRIMARY KEY DEFAULT nextval('currency_id_seq'),
    currency_type varchar(155) not null
);