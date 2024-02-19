CREATE TABLE IF NOT EXISTS request_withdrawal
(
    id   bigserial PRIMARY KEY,
    type_account            VARCHAR(255),
    reason_unblocking       VARCHAR(255),
    attached_file           VARCHAR(255),
    rib                     VARCHAR(255),
    amount                  float,
    created_at              TIMESTAMP,

    collaborator_id         bigserial,

    CONSTRAINT fk_collaborator_request_withdrawal FOREIGN KEY (collaborator_id) REFERENCES collaborator (id)
    );
