INSERT INTO author(id, name) VALUES(1, 'Илья Ильф');
INSERT INTO author(id, name) VALUES(2, 'Евгений Петров');
INSERT INTO genre(id, name) VALUES(1, 'Комедия');
INSERT INTO book(id, title) VALUES(1, '12 стульев');
INSERT INTO book_author_link(book_id, author_id) VALUES(1, 1);
INSERT INTO book_author_link(book_id, author_id) VALUES(1, 2);
INSERT INTO book_genre_link(book_id, genre_id) VALUES(1, 1);