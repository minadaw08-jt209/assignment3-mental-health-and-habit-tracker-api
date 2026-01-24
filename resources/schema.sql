DROP TABLE IF EXISTS activities;
DROP TABLE IF EXISTS users;
DROP TYPE IF EXISTS activity_type;

CREATE TYPE activity_type AS ENUM ('HABIT', 'MOOD');

CREATE TABLE users (
                       id    INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                       name  TEXT NOT NULL CHECK (length(trim(name)) > 0),
                       email TEXT NOT NULL UNIQUE CHECK (position('@' in email) > 1)
);

CREATE TABLE activities (
                            id         INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                            user_id    INT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
                            name       TEXT NOT NULL CHECK (length(trim(name)) > 0),
                            type       activity_type NOT NULL,
                            entry_date DATE NOT NULL,

                            habit_streak INT,
                            mood_level   INT,

    -- business rule: daily summary per type (1 HABIT + 1 MOOD per day)
                            CONSTRAINT uq_user_date_type UNIQUE (user_id, entry_date, type),

    -- subtype rules
                            CONSTRAINT ck_subtype CHECK (
                                (type='HABIT' AND habit_streak IS NOT NULL AND habit_streak >= 0 AND mood_level IS NULL)
                                    OR
                                (type='MOOD'  AND mood_level   IS NOT NULL AND mood_level BETWEEN 1 AND 10 AND habit_streak IS NULL)
                                )
);

INSERT INTO users(name, email) VALUES ('Aruzhan','aruzhan@example.com');
