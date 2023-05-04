-- Ryder alt og starter på en frisk
DROP SCHEMA IF EXISTS sep2 CASCADE ;
CREATE SCHEMA sep2;
SET SEARCH_PATH = "sep2";

-- Lokaletype, grupperum, 6. semester grupperum, auditorium osv.
CREATE TABLE room_type
(
    room_type_id            serial      NOT NULL PRIMARY KEY,
    room_type_name          varchar(50) NOT NULL
);

-- Brugertyper, medarbejder, studerende, 6. semester studerende osv.
CREATE TABLE user_type
(
    user_type_id            serial      NOT NULL PRIMARY KEY ,
    user_type_name          varchar(50) NOT NULL ,
    can_edit_users          bool        NOT NULL
);

-- Hvilke brugertyper har mulighed for at booke hvilke lokaletyper
CREATE TABLE user_type_allowed_room_type
(
    user_type_id            int         NOT NULL REFERENCES user_type(user_type_id),
    room_type_id            int         NOT NULL REFERENCES room_type(room_type_id),

    PRIMARY KEY (user_type_id, room_type_id)
);

-- Brugere, både studerende og medarbejdere
CREATE TABLE "user"
(
    user_id                 serial      NOT NULL PRIMARY KEY,
    user_name               varchar(99) NOT NULL,
    user_initials           varchar(10) NULL, -- null hvis ikke medarbejder
    user_viaid              int         NULL, -- null hvis ikke studerende
    user_type_id            int         NOT NULL REFERENCES user_type(user_type_id),

    user_password_hash      varchar(40) NOT NULL
);

-- Fag
CREATE TABLE course
(
    course_id               serial      NOT NULL PRIMARY KEY ,
    course_name             varchar(50) NOT NULL,
    course_time_slot_count  int         NOT NULL, -- antal timer der skal bookes til dette fag
    course_teacher_user_id  int         NOT NULL REFERENCES "user"(user_id)
);

-- Hold eller klasser
CREATE TABLE user_group
(
    user_group_id           serial      NOT NULL PRIMARY KEY,
    user_group_name         varchar(50) NOT NULL
);

-- Lokaler, alle typer
CREATE TABLE room
(
    room_id                 serial      NOT NULL PRIMARY KEY ,
    room_name               varchar(50) NOT NULL ,
    room_size               int         NOT NULL ,
    room_comfort_capacity   int         NOT NULL,
    room_fire_capacity      int         NOT NULL,

    room_type_id            int         NOT NULL REFERENCES room_type(room_type_id)

    -- TODO: Rum med skillevægge
);

-- Farveopdeling af lokaler pr. bruger
CREATE TABLE user_room_category
(
    user_id                 int         NOT NULL REFERENCES "user"(user_id),
    room_id                 int         NOT NULL REFERENCES room(room_id),
    color                   int         NOT NULL -- argb
);


-- Faste tidsintervaller til booking f.eks. 8:20-11:40
CREATE TABLE time_slot
(
    time_slot_id            serial      NOT NULL PRIMARY KEY ,
    time_slot_start         time        NOT NULL,
    time_slot_end           time        NOT NULL
);

