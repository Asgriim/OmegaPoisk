CREATE SEQUENCE IF NOT EXISTS content_id_seq;

CREATE TABLE IF NOT EXISTS role_type (
                                         id SERIAL PRIMARY KEY,
                                         name TEXT NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS content_type (
                                            id SERIAL PRIMARY KEY,
                                            type TEXT NOT NULL UNIQUE
);

-- Insert initial roles into the role_enum table, only if they don't already exist
INSERT INTO role_type (name)
VALUES ('admin'), ('creator'), ('user')
    ON CONFLICT (name) DO NOTHING;


INSERT INTO content_type (type)
VALUES ('anime'), ('game'), ('tv_show'), ('movie'), ('comic')
    ON CONFLICT (type) DO NOTHING;


-- Таблица user
CREATE TABLE IF NOT EXISTS user_ (
                                     id SERIAL PRIMARY KEY,
                                     email TEXT NOT NULL UNIQUE,
                                     login TEXT NOT NULL UNIQUE,
                                     password TEXT NOT NULL,
                                     role_id INT,
                                     FOREIGN KEY (role_id) REFERENCES role_type (id)
    );

CREATE TABLE IF NOT EXISTS game (
                                    id INT PRIMARY KEY,
                                    title TEXT,
                                    description TEXT,
                                    is_free BOOL NOT NULL
);

-- Таблица tv_show
CREATE TABLE IF NOT EXISTS tv_show (
                                       id INT PRIMARY KEY,
                                       title TEXT,
                                       description TEXT,
                                       series_num INT NOT NULL CHECK ( tv_show.series_num > 0)
    );

CREATE TABLE IF NOT EXISTS comic (
                                     id INT PRIMARY KEY,
                                     title TEXT,
                                     description TEXT,
                                     is_colored BOOL NOT NULL,
                                     chapters_count INT
);

CREATE TABLE IF NOT EXISTS movie (
                                     id INT PRIMARY KEY,
                                     title TEXT,
                                     description TEXT,
                                     duration INT NOT NULL CHECK ( duration > 0 )
    );

-- Таблица anime
CREATE TABLE IF NOT EXISTS anime (
                                     id INT PRIMARY KEY,
                                     title TEXT,
                                     description TEXT,
                                     series_num INT NOT NULL CHECK ( series_num > 0 )
    );

-- Таблица content
CREATE TABLE IF NOT EXISTS content (
                                       id INT PRIMARY KEY,
                                       type_id INT NOT NULL,
                                       FOREIGN KEY (type_id) REFERENCES content_type (id) ON DELETE CASCADE
    );

-- Таблица studio
CREATE TABLE IF NOT EXISTS studio (
                                      id SERIAL PRIMARY KEY,
                                      name TEXT NOT NULL
);

CREATE TABLE IF NOT EXISTS studio_contents (
                                               studio_id INT,
                                               content_id INT,
                                               FOREIGN KEY (studio_id) REFERENCES studio(id) ON DELETE CASCADE,
    FOREIGN KEY (content_id) REFERENCES content(id) ON DELETE CASCADE
    );


-- function to insert to content table
CREATE OR REPLACE FUNCTION add_to_content()
    RETURNS TRIGGER AS $$
BEGIN
INSERT INTO content
VALUES (
           NEW.id,
           (SELECT id FROM content_type WHERE type = TG_ARGV[0] LIMIT 1)
    );
RETURN NEW;
END;
    $$ LANGUAGE plpgsql;

CREATE OR REPLACE  TRIGGER add_anime
    AFTER INSERT ON anime
    FOR EACH ROW EXECUTE PROCEDURE add_to_content('anime');

CREATE OR REPLACE  TRIGGER add_game
    AFTER INSERT ON game
    FOR EACH ROW EXECUTE PROCEDURE add_to_content('game');

CREATE OR REPLACE TRIGGER add_tv_show
    AFTER INSERT ON tv_show
    FOR EACH ROW EXECUTE PROCEDURE add_to_content('tv_show');

CREATE OR REPLACE  TRIGGER add_comic
    AFTER INSERT ON comic
    FOR EACH ROW EXECUTE PROCEDURE add_to_content('comic');

CREATE OR REPLACE TRIGGER add_movies
    AFTER INSERT ON movie
    FOR EACH ROW EXECUTE PROCEDURE add_to_content('movie');


CREATE OR REPLACE FUNCTION remove_from_content()
    RETURNS TRIGGER AS $$
BEGIN
DELETE FROM content
WHERE (OLD.id = content.id);
RETURN OLD;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE TRIGGER remove_anime_trig
    AFTER DELETE ON anime
    FOR EACH ROW EXECUTE PROCEDURE remove_from_content();

CREATE OR REPLACE TRIGGER remove_comic_trig
    AFTER DELETE ON comic
    FOR EACH ROW EXECUTE PROCEDURE remove_from_content();

CREATE OR REPLACE TRIGGER remove_game_trig
    AFTER DELETE ON game
    FOR EACH ROW EXECUTE PROCEDURE remove_from_content();

CREATE OR REPLACE TRIGGER remove_movie_trig
    AFTER DELETE ON movie
    FOR EACH ROW EXECUTE PROCEDURE remove_from_content();

CREATE OR REPLACE TRIGGER remove_tb_show_trig
    AFTER DELETE ON tv_show
    FOR EACH ROW EXECUTE PROCEDURE remove_from_content();


INSERT INTO anime (id, title, description, series_num)
VALUES
    (1, 'Naruto', 'A story about a young ninja', 220),
    (2, 'Attack on Titan', 'Humans fighting against Titans', 87);