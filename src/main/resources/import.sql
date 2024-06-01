create table if not exists "user"
(
    user_id  varchar(255) not null primary key,
    document varchar(255) not null unique,
    email    varchar(255) not null unique,
    name     varchar(255) not null,
    password varchar(255) not null,
    type     varchar(255) not null constraint user_type_check check ((type)::text = ANY ((ARRAY ['USER'::character varying, 'SHOPKEEPER'::character varying])::text[]))
    );

create table if not exists "transfer"
(
    transfer_id    varchar(255)     not null primary key,
    amount         double precision not null,
    date           timestamp(6)     not null,
    description    varchar(255)     not null,
    "payer_id" varchar(255) constraint  payer_userId references "user",
    "payee_id" varchar(255) constraint  payee_userId references "user"
);

create table if not exists "balance"
(
    balance_id    varchar(255)     not null primary key,
    balance       double precision not null,
    date          date             not null,
    "user_id"     varchar(255) constraint user_id references "user"
);
