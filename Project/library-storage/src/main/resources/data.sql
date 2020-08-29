INSERT INTO library_storage.author(id, name, birth_day)
VALUES (1, 'Илья Ильф', '1897-10-15'),
       (2, 'Евгений Петров', '1902-12-13'),
       (3, 'Валентин Пикуль', '1928-07-13');

INSERT INTO library_storage.genre(id, name, description)
VALUES (1, 'Комедия', 'Драматический жанр литературы, в центре которого стоит комическое смешное событие'),
       (2, 'Приключения',
        'Один из видов художественной литературы,
        основным содержанием которой является захватывающий рассказ о реальных или вымышленных событиях.'),
       (3, 'Исторический роман',
        'Произведение художественной литературы,
        события которого разворачиваются на фоне исторических событий и с участием реальных исторических личностей.');

INSERT INTO library_storage.book(id, title, description)
VALUES (1, '12 стульев',
        'Герой романа Остап Бендер из мелкого жулика превращается в романтического героя, лишнего человека,
        глубоко укорененного в русской традиции.'),
       (2, 'Честь имею', 'Исповедь офицера Российского Генштаба.'),
       (3, 'Слово и дело', 'Роман-хроника времен Анны Иоановны.'),
       (4, 'Пером и шпагой', 'История жизни Шарля де Бомона, шевалье де Еона - воистину великого дипломата и шпиона.');

INSERT INTO library_storage.book_author(book_id, author_id)
VALUES (1, 1),
       (1, 2),
       (2, 3),
       (3, 3),
       (4, 3);

INSERT INTO library_storage.book_genre(book_id, genre_id)
VALUES (1, 1),
       (1, 2),
       (2, 3),
       (3, 3),
       (4, 3);