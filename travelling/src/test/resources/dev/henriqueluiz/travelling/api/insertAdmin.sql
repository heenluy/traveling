ALTER SEQUENCE user_id_seq RESTART WITH 1;

INSERT INTO app_user (user_id, first_name, last_name, email, password, authorities)
VALUES (nextval('user_id_seq'), 'Henrique', 'Luiz', 'admin@mail.dev', '$2a$10$QxpKycNRRzym/Wgl5OtVd.0/o3XKtBVR0Al0njbHoqZxU/f1vD6BG', ARRAY['admin']);