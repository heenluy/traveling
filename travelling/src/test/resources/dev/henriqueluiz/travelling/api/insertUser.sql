ALTER SEQUENCE user_id_seq RESTART WITH 1;

INSERT INTO app_user (user_id, first_name, last_name, email, password, authorities)
VALUES (nextval('user_id_seq'), 'Henrique', 'Luiz', 'test@mail.dev', '$2a$10$y1da7Uk286xbQNxGUVWlNePWq9bY/3aDddA08U41XBIlLherTZv96', ARRAY['write']);