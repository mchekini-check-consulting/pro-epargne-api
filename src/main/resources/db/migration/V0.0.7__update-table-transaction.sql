ALTER TABLE IF EXISTS transaction
    ADD COLUMN next_amount float;
ALTER TABLE IF EXISTS transaction
    ADD COLUMN previous_amount float;