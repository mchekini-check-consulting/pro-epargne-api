create table if not exists contribution
(
    transaction_id bigserial primary key,
    amount         float,

    constraint fk_contribution_transaction FOREIGN KEY (transaction_id) references transaction (transaction_id)

);