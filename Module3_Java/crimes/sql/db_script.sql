/*
sudo -i -u postgres     -   enter as user postgres
psql -f db_script.sql   -   run script with psql from script-file
\l                      -   show databases
\c database             -   connect to target database
\dt                     -   show tables
*/

DROP DATABASE IF EXISTS police_crimes;
CREATE DATABASE police_crimes ENCODING 'UTF8';
\c police_crimes;

CREATE TABLE street
(
    id_street   int primary key CHECK (id_street > 0),
    name_street varchar NOT NULL
);

CREATE TABLE location
(
    latitude     varchar,
    id_street_fk int references street (id_street) NOT NULL,
    longitude    varchar,
    PRIMARY KEY (latitude, longitude)
);

CREATE TABLE crime
(
    id_crime         int primary key NOT NULL CHECK (id_crime > 0),
    category         varchar         NOT NULL,
    persistent_id    varchar(64),
    month            varchar         NOT NULL,
    latitude_fk      varchar         NOT NULL,
    longitude_fk     varchar         NOT NULL,
    context          varchar,
    location_type    varchar         NOT NULL,
    location_subtype varchar,
    category_outcome varchar,
    date_outcome     varchar,
    FOREIGN KEY (latitude_fk, longitude_fk) REFERENCES location (latitude, longitude)
);

\dt
\l