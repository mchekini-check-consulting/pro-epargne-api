create table if not exists company_signatory
(
    id                     bigserial PRIMARY KEY,
    last_name              VARCHAR(255),
    first_name             VARCHAR(255),
    email                  VARCHAR(255),
    date_of_birth          date,
    job_title              VARCHAR(255),
    social_security_number VARCHAR(255),
    country_of_birth       VARCHAR(255),
    country_of_residence   VARCHAR(255),
    executive              BOOLEAN,
    contract_id            bigserial,

    constraint fk_contract_company_signatory FOREIGN KEY (contract_id) references contract (contract_id)
);