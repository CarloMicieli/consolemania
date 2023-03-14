CREATE TABLE platforms
(
    platform_id        uuid PRIMARY KEY   NOT NULL,
    platform_urn       varchar(100)       NOT NULL,
    name               varchar(100)       NOT NULL,
    manufacturer       varchar(100)       NOT NULL,
    generation         integer            NOT NULL,
    type               varchar(100)       NOT NULL,
    year               integer            NOT NULL,
    release_eu         date,
    release_jp         date,
    release_na         date,
    discontinued_year  integer,
    discontinued       boolean            NOT NULL DEFAULT false,
    introductory_price decimal,
    units_sold         decimal,
    media              varchar(100) ARRAY NOT NULL,
    cpu                varchar(1000),
    memory             varchar(1000),
    display            varchar(1000),
    sound              varchar(1000),
    created_date       timestamptz        NOT NULL,
    last_modified_date timestamptz        NOT NULL,
    version            integer            NOT NULL
);

CREATE UNIQUE INDEX "Idx_platforms_urn"
    ON platforms USING btree
        (platform_urn ASC NULLS LAST);

CREATE TABLE games
(
    game_id            uuid PRIMARY KEY   NOT NULL,
    game_urn           varchar(500)       NOT NULL,
    platform_id        uuid               NOT NULL,
    title              varchar(250)       NOT NULL,
    genres             varchar(100) ARRAY NOT NULL,
    modes              varchar(100) ARRAY NOT NULL,
    series             varchar(250),
    developer          varchar(250),
    publisher          varchar(250),
    plot               varchar(5000),
    rating             varchar(100),
    year               integer            NOT NULL,
    created_date       timestamptz        NOT NULL,
    last_modified_date timestamptz        NOT NULL,
    version            integer            NOT NULL,
    CONSTRAINT "FK_games_platforms" FOREIGN KEY (platform_id)
        REFERENCES platforms (platform_id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
);

CREATE UNIQUE INDEX "Idx_games_urn"
    ON games USING btree
        (game_urn ASC NULLS LAST);
