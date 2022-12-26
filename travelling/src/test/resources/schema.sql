-- CREATES ROLE TABLE
DROP SEQUENCE IF EXISTS role_id_seq CASCADE;
CREATE SEQUENCE role_id_seq;
DROP TABLE IF EXISTS app_role;

CREATE TABLE app_role (
    role_id INT8 NOT NULL DEFAULT nextval('role_id_seq'),
    name VARCHAR(16) NOT NULL,
    PRIMARY KEY(role_id)
);

ALTER SEQUENCE role_id_seq
OWNED BY app_role.role_id;

-- CREATES USER TABLE
DROP SEQUENCE IF EXISTS user_id_seq CASCADE;
CREATE SEQUENCE user_id_seq;
DROP TABLE IF EXISTS app_user;

CREATE TABLE app_user (
    user_id INT8 NOT NULL DEFAULT nextval('user_id_seq'),
    first_name VARCHAR(32) NOT NULL,
    last_name VARCHAR(32) NOT NULL,
    email VARCHAR(48) NOT NULL,
    password VARCHAR(255) NOT NULL,
    authorities TEXT [] NOT NULL DEFAULT ARRAY[''],
    PRIMARY KEY(user_id)
);

ALTER SEQUENCE user_id_seq
OWNED BY app_user.user_id;