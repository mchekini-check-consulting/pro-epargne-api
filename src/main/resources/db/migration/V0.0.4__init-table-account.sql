create table if not exists account
(
    account_id      bigserial primary key,
    amount          float,
    type            varchar,
    collaborator_id bigserial,
    contract_id     bigserial,

    constraint fk_account_contract FOREIGN KEY (contract_id) references contract (contract_id),
    constraint fk_account_collaborator FOREIGN KEY (collaborator_id) references collaborator (id)
);



create table if not exists transaction
(
    transaction_id bigserial primary key,
    amount         float,
    type           varchar,
    comment        varchar,
    account_id     bigserial,

    constraint fk_transaction_account FOREIGN KEY (account_id) references account (account_id)

);


ALTER TABLE account ALTER COLUMN contract_id DROP NOT NULL;