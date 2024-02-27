truncate table news cascade;
truncate table comments cascade;

INSERT INTO news (id, time, update_time, title, text)
VALUES (1, NOW(), NOW(), 'Новость 1', 'Текст новости 1'),
       (2, NOW(), NOW(), 'Новость 22', 'Текст новости 2'),
       (3, NOW(), NOW(), 'Новинки', 'привет текст 2')
RETURNING id;



INSERT INTO comments (id, time, update_time, text, username, news_id)
VALUES (1,NOW(), NOW(), 'Комментарий 1', 'Пользователь 1', 1),
       (2,NOW(), NOW(), 'Комментарий 2', 'Пользователь 2', 1),
       (3,NOW(), NOW(), 'Комментарий 1', 'Пользователь 1', 2),
       (4,NOW(), NOW(), 'Комментарий 2', 'Пользователь 2', 2)
RETURNING id;
