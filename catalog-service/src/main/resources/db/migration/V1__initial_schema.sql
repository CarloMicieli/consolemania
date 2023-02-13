CREATE TABLE platforms (
    platform_id uuid NOT NULL,
    platform_urn varchar(100) NOT NULL,
    name varchar(100) NOT NULL,
    manufacturer varchar(100) NOT NULL,
    generation varchar(100) NOT NULL,
    type varchar(100) NOT NULL,
    release_eu date,
    release_jp date,
    release_na date,
    discontinued_year integer,
    discontinued varchar(100) NOT NULL,
    introductory_price decimal,
    units_sold decimal,
    media varchar(1000) NOT NULL,
    cpu varchar(1000) NOT NULL,
    memory varchar(1000) NOT NULL,
    display varchar(1000) NOT NULL,
    sound varchar(1000) NOT NULL,
    version INT NOT NULL,
    CONSTRAINT "PK_platforms" PRIMARY KEY (platform_id)
);

CREATE TABLE games (
    game_id uuid NOT NULL,
    game_urn varchar(500) NOT NULL,
    platform_id uuid NOT NULL,
    title varchar(250) NOT NULL,
    genre varchar(100) NOT NULL,
    modes varchar(100) NOT NULL,
    series varchar(250),
    developer varchar(100),
    publisher varchar(100),
    release_eu date,
    release_jp date,
    release_na date,
    year integer not null,
    version INT NOT NULL,
    CONSTRAINT "PK_games" PRIMARY KEY (game_id),
    CONSTRAINT "FK_games_platforms" FOREIGN KEY (platform_id)
        REFERENCES platforms (platform_id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
);