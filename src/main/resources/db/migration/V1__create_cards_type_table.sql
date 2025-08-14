CREATE SEQUENCE card_type_id_seq START 1;

CREATE TABLE cards_type
(
    id   INTEGER PRIMARY KEY DEFAULT nextval('card_type_id_seq'),
    card_type varchar(155) not null
);