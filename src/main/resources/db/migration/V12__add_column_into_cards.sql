ALTER TABLE cards
    ADD COLUMN currency_type INTEGER REFERENCES currencies (id);