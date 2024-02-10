CREATE TABLE IF NOT EXISTS asset (
    id BIGSERIAL PRIMARY KEY,
    plans varchar (255),
    isin varchar (255),
    support_label varchar(255),
    management_company varchar(255)
);

CREATE TABLE IF NOT EXISTS asset_year(
    id BIGSERIAL PRIMARY KEY,
    year int,
    value float,
    asset_id BIGSERIAL,
    constraint fk_asset_year  foreign key (asset_id) references asset(id)
)