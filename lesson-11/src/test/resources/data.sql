INSERT INTO author(id, name) VALUES(1, 'Илья Ильф');
INSERT INTO author(id, name) VALUES(2, 'Евгений Петров');
INSERT INTO author(id, name) VALUES(3, 'Сергей Есенин');
INSERT INTO genre(id, name) VALUES(1, 'Комедия');
INSERT INTO genre(id, name) VALUES(2, 'Приключения');
INSERT INTO book(id, title) VALUES(1, '12 стульев');
INSERT INTO book_author(book_id, author_id) VALUES(1, 1);
INSERT INTO book_author(book_id, author_id) VALUES(1, 2);
INSERT INTO book_genre(book_id, genre_id) VALUES(1, 1);
insert into comment(id, text_comment, book_id) values(1, 'Нужно сдать до праздников.', 1)