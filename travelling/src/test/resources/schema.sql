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
DROP TABLE IF EXISTS app_user CASCADE;

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

-- CREATES TRAVEL TABLE
DROP SEQUENCE IF EXISTS travel_id_seq CASCADE;
CREATE SEQUENCE travel_id_seq;
DROP TABLE IF EXISTS travel;

CREATE TABLE travel (
   travel_id INT8 NOT NULL DEFAULT nextval('travel_id_seq'),
   destination VARCHAR(255),
   departure_date DATE,
   return_date DATE,
   budget DECIMAL,
   user_id INT8,
   CONSTRAINT pk_travel PRIMARY KEY (travel_id),
   CONSTRAINT fk_travel_on_user FOREIGN KEY (user_id) REFERENCES app_user (user_id)
);