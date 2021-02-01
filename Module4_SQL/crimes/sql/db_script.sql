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

CREATE TABLE street_crime
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

CREATE TABLE outcome_object
(
    id_outcome_object   varchar primary key,
    name_outcome_object varchar NOT NULL
);

CREATE TABLE stop_and_search
(
    id_stop_and_search                  SERIAL PRIMARY KEY CHECK (id_stop_and_search > 0),
    force                               varchar                                               NOT NULL,
    type                                varchar                                               NOT NULL,
    involved_person                     boolean                                               NOT NULL,
    datetime                            varchar                                               NOT NULL,
    operation                           varchar,
    operation_name                      varchar,
    latitude_fk                         varchar,
    longitude_fk                        varchar,
    gender                              varchar,
    age_range                           varchar,
    self_defined_ethnicity              varchar,
    officer_defined_ethnicity           varchar,
    legislation                         varchar,
    object_of_search                    varchar,
    outcome                             varchar                                               NOT NULL,
    outcome_linked_to_object_of_search  varchar,
    removal_of_more_than_outer_clothing boolean,
    id_outcome_object_fk                varchar references outcome_object (id_outcome_object) NOT NULL,
    FOREIGN KEY (latitude_fk, longitude_fk) REFERENCES location (latitude, longitude),
    UNIQUE (force, datetime, latitude_fk, longitude_fk, age_range,outcome,
    object_of_search, type, involved_person, operation, operation_name, gender, self_defined_ethnicity,
    officer_defined_ethnicity, legislation, outcome_linked_to_object_of_search, removal_of_more_than_outer_clothing,
    id_outcome_object_fk)
);

\dt
\l