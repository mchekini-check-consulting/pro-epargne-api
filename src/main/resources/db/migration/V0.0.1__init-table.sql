CREATE table if not exists collaborator
(
    id           bigserial primary key,
    last_name    varchar,
    first_name   varchar,
    email        varchar,
    gender       varchar,
    birth_date   date,
    entry_date   date,
    gross_salary int
);