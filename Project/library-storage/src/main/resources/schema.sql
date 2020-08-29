drop table if exists library_storage.book_author;
drop table if exists library_storage.book_genre;
drop table if exists library_storage.author;
drop table if exists library_storage.genre;
drop table if exists library_storage.comment;
drop table if exists library_storage.book;
drop schema if exists library_storage;

create schema library_storage;

create table library_storage.book(
    id bigserial,
    title varchar(255) not null,
    description varchar(1000) not null,
    primary key (id)
);

create table library_storage.author(
    id bigserial,
    name varchar(255) not null,
    birth_day date not null,
    primary key (id)
);

create table library_storage.book_author(
    book_id bigint not null references library_storage.book (id) on delete cascade,
    author_id bigint not null references library_storage.author (id),
    primary key (book_id, author_id)
);

create table library_storage.genre(
    id bigserial,
    name varchar(255) not null,
    description varchar(1000) not null,
    primary key (id)
);

create table library_storage.book_genre(
    book_id bigint not null references library_storage.book (id) on delete cascade,
    genre_id bigint not null references library_storage.genre (id),
    primary key (book_id, genre_id)
);

create table library_storage.comment(
    id bigserial,
    text_comment varchar(1000) not null,
    book_id bigint references library_storage.book (id),
    primary key (id)
);
