CREATE TABLE platforms
(
    platform_id        uuid PRIMARY KEY NOT NULL,
    platform_urn       varchar(100)     NOT NULL,
    name               varchar(100)     NOT NULL,
    manufacturer       varchar(100)     NOT NULL,
    generation         varchar(100)     NOT NULL,
    type               varchar(100)     NOT NULL,
    release_eu         date,
    release_jp         date,
    release_na         date,
    discontinued_year  integer,
    discontinued       varchar(100)     NOT NULL,
    introductory_price decimal,
    units_sold         decimal,
    media              varchar(1000)    NOT NULL,
    cpu                varchar(1000)    NOT NULL,
    memory             varchar(1000)    NOT NULL,
    display            varchar(1000)    NOT NULL,
    sound              varchar(1000)    NOT NULL,
    created_date       timestamp        NOT NULL,
    last_modified_date timestamp        NOT NULL,
    version            INT              NOT NULL
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
    developer          varchar(100),
    publisher          varchar(100),
    release_eu         date,
    release_jp         date,
    release_na         date,
    year               integer            not null,
    created_date       timestamp          NOT NULL,
    last_modified_date timestamp          NOT NULL,
    version            INT                NOT NULL,
    CONSTRAINT "FK_games_platforms" FOREIGN KEY (platform_id)
        REFERENCES platforms (platform_id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
);

CREATE UNIQUE INDEX "Idx_games_urn"
    ON games USING btree
        (game_urn ASC NULLS LAST);
