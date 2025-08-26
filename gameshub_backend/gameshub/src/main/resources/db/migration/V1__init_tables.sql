-- V1__create_schema.sql
-- ======================================================
-- PostgreSQL schema for the Spring‑Boot entities
-- ------------------------------------------------------
-- 1️⃣ Sequences (for @GeneratedValue SEQUENCE)
-- 2️⃣ Tables: CATEGORY, GAME, GAME_IMAGE, NOTIFICATION_ENQUIRY
-- 3️⃣ Join table for Category ↔ Game (many-to-many)
-- 4️⃣ Indexes defined in the annotations
-- ======================================================

--------------------------------------------------------------
-- 1. Sequences ------------------------------------------------
CREATE SEQUENCE IF NOT EXISTS category_seq START WITH 1 INCREMENT BY 1 NO MINVALUE NO MAXVALUE CACHE 1;
CREATE SEQUENCE IF NOT EXISTS genre_seq START WITH 1 INCREMENT BY 1 NO MINVALUE NO MAXVALUE CACHE 1;
CREATE SEQUENCE IF NOT EXISTS game_seq     START WITH 1 INCREMENT BY 1 NO MINVALUE NO MAXVALUE CACHE 1;
CREATE SEQUENCE IF NOT EXISTS game_image_seq START WITH 1 INCREMENT BY 1 NO MINVALUE NO MAXVALUE CACHE 1;
CREATE SEQUENCE IF NOT EXISTS notification_enq_seq
    START WITH 1 INCREMENT BY 1 NO MINVALUE NO MAXVALUE CACHE 1;

--------------------------------------------------------------
-- 2. Tables --------------------------------------------------
-- 2a. CATEGORY
CREATE TABLE IF NOT EXISTS CATEGORY (
    id                BIGINT PRIMARY KEY DEFAULT nextval('category_seq'),
    name              TEXT,
    marketplace_name  TEXT,
    external_id       BIGINT
);

CREATE TABLE IF NOT EXISTS GENRE (
    id                BIGINT PRIMARY KEY DEFAULT nextval('genre_seq'),
    name              TEXT,
    marketplace_name  TEXT,
    external_id       BIGINT
);

-- 2b. GAME
CREATE TABLE IF NOT EXISTS GAME (
    id                        BIGINT PRIMARY KEY DEFAULT nextval('game_seq'),
    name                      VARCHAR(2000),
    external_app_id           TEXT,
    marketplace_name          TEXT,
    price_initial             NUMERIC,
    price_final               NUMERIC,
    discount_percent          NUMERIC,
    loaded_details_from_external_api BOOLEAN,
    added_at                  TIMESTAMP WITH TIME ZONE,
    updated_at                TIMESTAMP WITH TIME ZONE,
    currency                  TEXT,
    is_released               BOOLEAN,
    is_game                   BOOLEAN,
    main_image_url            TEXT,
    description               TEXT,
    short_description         VARCHAR(4000),
    developer                 VARCHAR(2000),
    publisher                 VARCHAR(2000),
    slug                      VARCHAR(2000),
    redirect_url              VARCHAR(4000)
);

-- 2c. GAME_IMAGE
CREATE TABLE IF NOT EXISTS GAME_IMAGE (
    id                     BIGINT PRIMARY KEY DEFAULT nextval('game_image_seq'),
    game_id                BIGINT REFERENCES GAME(id) ON DELETE CASCADE,
    image_url              TEXT,
    image_thumbnail_url    TEXT
);

-- 2d. NOTIFICATION_ENQUIRY
CREATE TABLE IF NOT EXISTS NOTIFICATION_ENQUIRY (
    id            BIGINT PRIMARY KEY DEFAULT nextval('notification_enq_seq'),
    email         TEXT,
    game_id       BIGINT REFERENCES GAME(id) ON DELETE SET NULL,
    price_goal    NUMERIC,
    status        TEXT CHECK (status IN ('SENT', 'NOT_SENT'))
);

--------------------------------------------------------------
-- 3. Join table for Category ↔ Game (Many‑to‑Many)
CREATE TABLE IF NOT EXISTS CATEGORY_GAME (
    category_id BIGINT REFERENCES CATEGORY(id) ON DELETE CASCADE,
    game_id     BIGINT REFERENCES GAME(id) ON DELETE CASCADE,
    PRIMARY KEY (category_id, game_id)
);


CREATE TABLE IF NOT EXISTS GENRE_GAME (
    genre_id BIGINT REFERENCES GENRE(id) ON DELETE CASCADE,
    game_id     BIGINT REFERENCES GAME(id) ON DELETE CASCADE,
    PRIMARY KEY (genre_id, game_id)
);

--------------------------------------------------------------
-- 4. Indexes ------------------------------------------------
-- 4a. GAME indexes (from @Index annotations)
CREATE INDEX IF NOT EXISTS game_name_idx          ON GAME (name);
CREATE INDEX IF NOT EXISTS game_loaded_idx       ON GAME (marketplace_name, loaded_details_from_external_api);

-- 4b. GAME_IMAGE index
CREATE INDEX IF NOT EXISTS game_image_idx         ON GAME_IMAGE (game_id);

-- 4c. CATEGORY_GAME composite PK already acts as an index

--------------------------------------------------------------
-- Done -------------------------------------------------------