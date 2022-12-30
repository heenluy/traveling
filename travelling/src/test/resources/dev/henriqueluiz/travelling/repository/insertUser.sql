ALTER SEQUENCE user_id_seq RESTART WITH 1;

INSERT INTO app_user (user_id, first_name, last_name, email, password)
VALUES (nextval('user_id_seq'), 'Henrique', 'Luiz', 'test@mail.dev', 'developer');