const { Client } = require('pg');

const pgclient = new Client({
    host: process.env.POSTGRES_HOST,
    port: process.env.POSTGRES_PORT,
    user: 'postgres',
    password: 'postgres',
    database: 'recipeorganizer'
});

pgclient.connect();

const createTablesQuery = `
    CREATE TABLE users (
        id SERIAL PRIMARY KEY,
        username VARCHAR(20) NOT NULL,
        email VARCHAR(50) NOT NULL,
        password VARCHAR(120) NOT NULL
    );

    CREATE TABLE roles (
        role_id SERIAL PRIMARY KEY,
        name VARCHAR(20) NOT NULL
    );

    CREATE TABLE user_roles (
        user_id INTEGER REFERENCES users(id),
        role_id INTEGER REFERENCES roles(role_id),
        PRIMARY KEY (user_id, role_id)
    );

    CREATE TABLE recipes (
        id SERIAL PRIMARY KEY,
        name VARCHAR(255) NOT NULL,
        ingredients TEXT,
        description TEXT
    );
`;

const insertDataQuery = `
    INSERT INTO roles (name) VALUES ('ROLE_ADMIN');
    INSERT INTO roles (name) VALUES ('ROLE_USER');

    INSERT INTO users (username, email, password)
    VALUES ('admin', 'admin@email.com', '$2a$12$d.dFoMghFSDjhu9d8uupHuU0Qx2FWikldBrGa4yuXz68YEPk/sWjm');

    INSERT INTO users (username, email, password)
    VALUES ('testUser', 'testUser@email.com', '$2a$12$v9ykV0/PH0EOAC12pfqWlu4YzsykY8u0TLcd1hnex0I0oGES.htoO');

    INSERT INTO user_roles (user_id, role_id)
    VALUES ((SELECT id FROM users WHERE username = 'admin'), (SELECT role_id FROM roles WHERE name = 'ROLE_ADMIN'));

    INSERT INTO user_roles (user_id, role_id)
    VALUES ((SELECT id FROM users WHERE username = 'testUser'), (SELECT role_id FROM roles WHERE name = 'ROLE_USER'));
`;

const selectDataQuery = 'SELECT * FROM users;';

pgclient.query(createTablesQuery, (err, res) => {
    if (err) throw err;

    pgclient.query(insertDataQuery, (err, res) => {
        if (err) throw err;

        pgclient.query(selectDataQuery, (err, res) => {
            if (err) throw err;
            console.log(err, res.rows); // Print the data in the users table
            pgclient.end();
        });
    });
});
