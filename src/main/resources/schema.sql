CREATE TABLE if not exists token_entry
(
    processor_name varchar(255) not null,
    segment integer not null,
    owner varchar(255),
    timestamp varchar(255) not null,
    token BYTEA,
    token_type varchar(255),
    constraint token_entry_pkey
        primary key (processor_name, segment)
);