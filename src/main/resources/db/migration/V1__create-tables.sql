CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE IF NOT EXISTS moderators (
    id UUID PRIMARY KEY,
    password VARCHAR(255) NOT NULL,
    email TEXT NOT NULL,
    role VARCHAR(50) NOT NULL,
    status VARCHAR(50) NOT NULL,
    password_updater_token VARCHAR(255),
    password_updaterexpiration TIMESTAMP,
    email_updater_token VARCHAR(255),
    email_updaterexpiration TIMESTAMP,
    enabled2fa BOOLEAN DEFAULT FALSE,
    secret2fa TEXT
    );

CREATE TABLE IF NOT EXISTS users (
    id UUID PRIMARY KEY,
    password VARCHAR(255) NOT NULL,
    email TEXT NOT NULL,
    role VARCHAR(50) NOT NULL,
    status VARCHAR(50) NOT NULL,
    password_updater_token VARCHAR(255),
    password_updaterexpiration TIMESTAMP,
    email_updater_token VARCHAR(255),
    email_updaterexpiration TIMESTAMP,
    enabled2fa BOOLEAN DEFAULT FALSE,
    secret2fa TEXT,
    username VARCHAR(255),
    photo_url TEXT,
    description TEXT
    );

CREATE TABLE IF NOT EXISTS user_entity_favorite_games (
    user_entity_id UUID NOT NULL,
    favorite_games UUID,
    CONSTRAINT fk_user_favorite FOREIGN KEY (user_entity_id) REFERENCES users (id) ON DELETE CASCADE
    );

CREATE TABLE IF NOT EXISTS user_entity_friends (
    user_entity_id UUID NOT NULL,
    friends UUID,
    CONSTRAINT fk_user_friends FOREIGN KEY (user_entity_id) REFERENCES users (id) ON DELETE CASCADE
    );

CREATE TABLE IF NOT EXISTS user_entity_followers (
    user_entity_id UUID NOT NULL,
    followers UUID,
    CONSTRAINT fk_user_followers FOREIGN KEY (user_entity_id) REFERENCES users (id) ON DELETE CASCADE
    );

CREATE INDEX IF NOT EXISTS idx_users_email ON users (email);
CREATE INDEX IF NOT EXISTS idx_moderators_email ON moderators (email);