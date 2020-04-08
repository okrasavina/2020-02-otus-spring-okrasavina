drop table if exists book;
create table book(
    id bigserial,
    title varchar(255) not null,
    primary key (id)
);

drop table if exists author;
create table author(
    id bigserial,
    name varchar(255) not null,
    primary key (id)
);

drop table if exists book_author;
create table book_author(
    book_id bigint not null references book (id) on delete cascade,
    author_id bigint not null references author (id),
    primary key (book_id, author_id)
);

drop table if exists genre;
create table genre(
    id bigserial,
    name varchar(255) not null,
    primary key (id)
);

drop table if exists book_genre;
create table book_genre(
    book_id bigint not null references book (id) on delete cascade,
    genre_id bigint not null references genre (id),
    primary key (book_id, genre_id)
);
