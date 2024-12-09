CREATE TABLE IF NOT EXISTS role_type (
                                         id SERIAL PRIMARY KEY,
                                         name TEXT NOT NULL UNIQUE
);

-- Insert initial roles into the role_enum table, only if they don't already exist
INSERT INTO role_type (name)
VALUES ('ADMIN'), ('CREATOR'), ('USER')
    ON CONFLICT (name) DO NOTHING;


-- Таблица user
CREATE TABLE IF NOT EXISTS user_ (
                                     id SERIAL PRIMARY KEY,
                                     email TEXT NOT NULL UNIQUE,
                                     login TEXT NOT NULL UNIQUE,
                                     password TEXT NOT NULL,
                                     role_id INT,
                                     FOREIGN KEY (role_id) REFERENCES role_type (id)
    );


INSERT INTO user_ (id, email, login, password, role_id)
VALUES
    (7, 'email@a.ru', 'test', '$2a$10$fY/el4KfuakBq.llJD/cbuX8G6OVTZa5yh5rhvYEUvYiS7pvhnKEm', 1);
--     (2, 'Attack@mail.ru', 'Attack', 'Attack', 3);