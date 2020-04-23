INSERT INTO author(id, name) VALUES(1, 'Илья Ильф');
INSERT INTO author(id, name) VALUES(2, 'Евгений Петров');
INSERT INTO author(id, name) VALUES(3, 'Валентин Пикуль');
INSERT INTO genre(id, name) VALUES(1, 'Комедия');
INSERT INTO genre(id, name) VALUES(2, 'Приключения');
INSERT INTO genre(id, name) VALUES(3, 'Исторический роман');
INSERT INTO book(id, title) VALUES(1, '12 стульев');
INSERT INTO book(id, title) VALUES(2, 'Честь имею');
INSERT INTO book(id, title) VALUES(3, 'Слово и дело');
INSERT INTO book(id, title) VALUES(4, 'Пером и шпагой');
INSERT INTO book_author(book_id, author_id) VALUES(1, 1);
INSERT INTO book_author(book_id, author_id) VALUES(1, 2);
INSERT INTO book_author(book_id, author_id) VALUES(2, 3);
INSERT INTO book_author(book_id, author_id) VALUES(3, 3);
INSERT INTO book_author(book_id, author_id) VALUES(4, 3);
INSERT INTO book_genre(book_id, genre_id) VALUES(1, 1);
INSERT INTO book_genre(book_id, genre_id) VALUES(1, 2);
INSERT INTO book_genre(book_id, genre_id) VALUES(2, 3);
INSERT INTO book_genre(book_id, genre_id) VALUES(3, 3);
INSERT INTO book_genre(book_id, genre_id) VALUES(4, 3);