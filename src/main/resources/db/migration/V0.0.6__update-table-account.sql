ALTER TABLE IF EXISTS account
    ADD COLUMN risk_level varchar(255);

ALTER TABLE IF EXISTS account
    ADD COLUMN management_mode varchar(255);