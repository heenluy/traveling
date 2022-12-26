ALTER SEQUENCE role_id_seq RESTART WITH 1;

INSERT INTO app_role(role_id, name)
VALUES (nextval('role_id_seq'), 'test');