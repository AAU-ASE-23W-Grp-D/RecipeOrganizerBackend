-- script to add all tables and relations to the database recipeorganizer
CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(20) NOT NULL,
    email VARCHAR(50) NOT NULL,
    password VARCHAR(120) NOT NULL
);

CREATE TABLE roles (
    role_id BIGSERIAL PRIMARY KEY,
    name VARCHAR(20) NOT NULL
);

CREATE TABLE user_roles (
    user_id BIGINT REFERENCES users(id),
    role_id BIGINT REFERENCES roles(role_id),
    PRIMARY KEY (user_id, role_id)
);

CREATE TABLE recipes (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    ingredients TEXT,
    description TEXT
);

CREATE TABLE own_recipes (
    user_id BIGINT REFERENCES users(id),
    recipe_id BIGINT REFERENCES recipes(id),
    PRIMARY KEY (user_id, recipe_id)
);

CREATE TABLE liked_recipes (
    user_id BIGINT REFERENCES users(id),
    recipe_id BIGINT REFERENCES recipes(id),
    PRIMARY KEY (user_id, recipe_id)
);

CREATE TABLE liked_by_user (
    recipe_id BIGINT REFERENCES recipes(id),
    user_id BIGINT REFERENCES users(id),
    PRIMARY KEY (user_id, recipe_id)
);

INSERT INTO roles (name) VALUES ('ROLE_ADMIN');

INSERT INTO roles (name) VALUES ('ROLE_USER');

INSERT INTO users (username, email, password)
VALUES ('admin', 'admin@email.com', '$2a$12$d.dFoMghFSDjhu9d8uupHuU0Qx2FWikldBrGa4yuXz68YEPk/sWjm');

INSERT INTO users (username, email, password)
VALUES ('testUSer', 'testUser@email.com', '$2a$12$v9ykV0/PH0EOAC12pfqWlu4YzsykY8u0TLcd1hnex0I0oGES.htoO');

INSERT INTO user_roles (user_id, role_id)
VALUES ('1', (SELECT role_id FROM roles WHERE name = 'ROLE_ADMIN'));

INSERT INTO user_roles (user_id, role_id)
VALUES ('2', (SELECT role_id FROM roles WHERE name = 'ROLE_USER'));
