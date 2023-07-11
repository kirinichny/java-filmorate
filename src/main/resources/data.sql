INSERT INTO mpa_ratings (id, name) VALUES (1, 'G');
INSERT INTO mpa_ratings (id, name) VALUES (2, 'PG');
INSERT INTO mpa_ratings (id, name) VALUES (3, 'PG-13');
INSERT INTO mpa_ratings (id, name) VALUES (4, 'R');
INSERT INTO mpa_ratings (id, name) VALUES (5, 'NC-17');

INSERT INTO genres (id, name) VALUES (1, 'Комедия');
INSERT INTO genres (id, name) VALUES (2, 'Драма');
INSERT INTO genres (id, name) VALUES (3, 'Мультфильм');
INSERT INTO genres (id, name) VALUES (4, 'Триллер');
INSERT INTO genres (id, name) VALUES (5, 'Документальный');
INSERT INTO genres (id, name) VALUES (6, 'Боевик');

//INSERT INTO films ("id", "name", "description", "release_date", "duration_minutes", "mpa_rating_id") VALUES (1, 'Как работает Java', 'Документальный фильм, который погружает зрителя в удивительный мир программирования на языке Java.', '2022-08-02', 120, 1);
//INSERT INTO films ("id", "name", "description", "release_date", "duration_minutes", "mpa_rating_id") VALUES (2, 'Интересное программирование', '"Интересное программирование" от Яндекс Практикум: Молодой программист погружается в мир таинственных багов и зловещих алгоритмов, где каждая строка кода может привести к смертельным последствиям.', '2023-07-08', 260, 5);

//INSERT INTO film_genres ("film_id", "genre_id") VALUES (1, 5);
//INSERT INTO film_genres ("film_id", "genre_id") VALUES (2, 4);
//INSERT INTO film_genres ("film_id", "genre_id") VALUES (2, 1);

//INSERT INTO users ("id", "email", "login", "name", "birthday") VALUES (1, 'user@user.ru', 'user-login', 'Пользователь 1', '1983-01-01');
//INSERT INTO users ("id", "email", "login", "name", "birthday") VALUES (2, 'user2@user.ru', 'user2-login', 'Пользователь 2', '1984-03-02');

//INSERT INTO film_likes ("user_id", "film_id", "created_at") VALUES (1, 2, '2023-07-09 00:51:15.000000');
//INSERT INTO film_likes ("user_id", "film_id", "created_at") VALUES (2, 2, '2023-07-09 00:51:27.000000');