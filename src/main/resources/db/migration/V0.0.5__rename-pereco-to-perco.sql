ALTER TABLE IF EXISTS pereco_contribution RENAME TO perco_contribution;

ALTER TABLE IF EXISTS perco_contribution RENAME COLUMN pereco_interest_accepted TO perco_interest_accepted;

ALTER TABLE IF EXISTS perco_contribution RENAME COLUMN pereco_voluntary_deposit_accepted TO perco_voluntary_deposit_accepted;

ALTER TABLE IF EXISTS perco_contribution RENAME COLUMN pereco_profit_sharing_accepted TO perco_profit_sharing_accepted;

ALTER TABLE IF EXISTS perco_contribution RENAME COLUMN pereco_time_saving_account_accepted TO perco_time_saving_account_accepted;
