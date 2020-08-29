drop table if exists book_reader;
drop table if exists shelf_reader;
drop table if exists authorities;
drop table if exists users;

create table users
(
    id       bigserial,
    username varchar(100) not null,
    email    varchar(255),
    password varchar(255) not null,
    enabled  boolean      not null,
    primary key (id)
);

create table authorities
(
    id        bigserial,
    username  varchar(255) not null,
    authority varchar(255) not null,
    primary key (id)
);

create table shelf_reader
(
    id      bigserial,
    user_id bigint references users (id),
    primary key (id)
);

create table book_reader
(
    id       bigserial,
    title    varchar(255) not null,
    shelf_id bigint references shelf_reader (id) on delete cascade,
    primary key (id)
);