create table if not exists contract
(
    contract_id   bigserial primary key,
    closing_month int,
    eligibility   varchar

);


create table if not exists pee_contribution
(
    rate_simple_contribution                    int,
    ceiling_simple_contribution                 int,
    rate_seniority_contribution                 int,
    ceiling_seniority_contribution_less_year    int,
    ceiling_seniority_contribution_between1and3 int,
    ceiling_seniority_contribution_between3and5 int,
    ceiling_seniority_contribution_greater5     int,
    ceiling_interval_contribution_first         int,
    rate_interval_contribution_first            int,
    interval_contribution_first                 int,
    ceiling_interval_contribution_second        int,
    rate_interval_contribution_second           int,
    interval_contribution_second                int,
    ceiling_interval_contribution_third         int,
    rate_interval_contribution_third            int,
    interval_contribution_third                 int,
    pee_interest_accepted                       boolean,
    pee_voluntary_deposit_accepted              boolean,
    pee_profit_sharing_accepted                 boolean,
    contract_id                                 bigserial,

    constraint fk_contract_pee_contribution FOREIGN KEY (contract_id) references contract (contract_id)
);

create table if not exists pereco_contribution
(
    rate_simple_contribution                    int,
    ceiling_simple_contribution                 int,
    rate_seniority_contribution                 int,
    ceiling_seniority_contribution_less_year    int,
    ceiling_seniority_contribution_between1and3 int,
    ceiling_seniority_contribution_between3and5 int,
    ceiling_seniority_contribution_greater5     int,
    ceiling_interval_contribution_first         int,
    rate_interval_contribution_first            int,
    interval_contribution_first                 int,
    ceiling_interval_contribution_second        int,
    rate_interval_contribution_second           int,
    interval_contribution_second                int,
    ceiling_interval_contribution_third         int,
    rate_interval_contribution_third            int,
    interval_contribution_third                 int,
    pereco_interest_accepted                    boolean,
    pereco_voluntary_deposit_accepted           boolean,
    pereco_profit_sharing_accepted              boolean,
    pereco_time_saving_account_accepted         boolean,
    contract_id                                 bigserial,

    constraint fk_contract_pereco_contribution FOREIGN KEY (contract_id) references contract (contract_id)
);



create table if not exists company
(
    siren             varchar primary key,
    company_name      varchar,
    legal_form        varchar,
    siret             varchar,
    business_activity varchar,
    business_address  varchar,
    workforce         int,
    total_wages       bigint,
    contract_id       bigserial,

    constraint fk_contract_company FOREIGN KEY (contract_id) references contract (contract_id)
);
