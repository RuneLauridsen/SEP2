-- Ryder alt og starter på en frisk
DROP SCHEMA IF EXISTS sep2 CASCADE ;
CREATE SCHEMA sep2;
SET SEARCH_PATH = "sep2";

-- Lokaletype, grupperum, 6. semester grupperum, auditorium osv.
CREATE TABLE room_type
(
    room_type_id            serial      NOT NULL PRIMARY KEY,
    room_type_name          varchar(50) NOT NULL UNIQUE
);

-- Brugertyper, medarbejder, studerende, 6. semester studerende osv.
CREATE TABLE user_type
(
    user_type_id            serial      NOT NULL PRIMARY KEY ,
    user_type_name          varchar(50) NOT NULL UNIQUE ,
    can_edit_users          bool        NOT NULL ,
    can_edit_rooms          bool        NOT NULL ,
    max_booking_count       int         NOT NULL
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
    user_password_hash      varchar(40) NOT NULL,
    user_type_id            int         NOT NULL REFERENCES user_type(user_type_id)

);

-- Fag
CREATE TABLE course
(
    course_id               serial      NOT NULL PRIMARY KEY ,
    course_name             varchar(50) NOT NULL UNIQUE ,
    course_time_slot_count  int         NOT NULL -- antal timer der skal bookes til dette fag
);

-- Hold eller klasser
CREATE TABLE user_group
(
    user_group_id           serial      NOT NULL PRIMARY KEY,
    user_group_name         varchar(50) NOT NULL UNIQUE ,
    course_id               int         NULL REFERENCES course(course_id)
);

-- Hvilke brugere hører til hvilke hold/klasser
CREATE TABLE user_group_users
(
    user_group_id           int         NOT NULL REFERENCES user_group(user_group_id),
    user_id                 int         NOT NULL REFERENCES "user"(user_id),
    is_teacher              bool        NOT NULL,
    
    PRIMARY KEY (user_group_id, user_id)
);

-- Lokaler, alle typer
CREATE TABLE room
(
    room_id                 serial      NOT NULL PRIMARY KEY ,
    room_name               varchar(50) NOT NULL UNIQUE ,
    room_size               int         NOT NULL ,
    room_comfort_capacity   int         NOT NULL,
    room_fire_capacity      int         NOT NULL,
    room_comment            varchar(99) NOT NULL,

    room_type_id            int         NOT NULL REFERENCES room_type(room_type_id)

    -- TODO: Rum med skillevægge
);

-- Farveopdeling af lokaler pr. bruger + personlig bemærkning
CREATE TABLE user_room_data
(
    user_id                 int         NOT NULL REFERENCES "user"(user_id),
    room_id                 int         NOT NULL REFERENCES room(room_id),
    color                   int         NOT NULL , -- argb
    comment                 varchar(99) NOT NULL ,

    PRIMARY KEY (user_id, room_id)
);


-- Faste tidsintervaller til booking f.eks. 8:20-11:40
CREATE TABLE time_slot
(
    time_slot_id            serial      NOT NULL PRIMARY KEY ,
    time_slot_start         time        NOT NULL,
    time_slot_end           time        NOT NULL
);

CREATE TABLE booking
(
    booking_id              serial      NOT NULL PRIMARY KEY ,
    date                    date        NOT NULL,
    start_time              time        NOT NULL,
    end_time                time        NOT NULL,
    room_id                 int         NOT NULL REFERENCES "room"(room_id) ,
    user_id                 int         NOT NULL REFERENCES "user"(user_id)
);


INSERT INTO user_type
    (user_type_name, can_edit_users, can_edit_rooms, max_booking_count)
VALUES
    /* id = 1 */ ('Skemalægger', true, true, 0),
    /* id = 2 */ ('Medarbejder', false, false, 0),
    /* id = 3 */ ('Studerende', false, false, 2),
    /* id = 4 */ ('Studerende (Bachelor)', false, false, 2);

INSERT INTO room_type
    (room_type_name)
VALUES
    /* id = 1 */ ('Grupperum'),
    /* id = 2 */ ('Bachelorrum'),
    /* id = 3 */ ('Medarbejderum'),
    /* id = 4 */ ('Klasselokale'),
    /* id = 5 */ ('Auditorium'),
    /* id = 6 */ ('Laboratorium'),
    /* id = 7 */ ('Hub');

INSERT INTO user_type_allowed_room_type
    (user_type_id, room_type_id)
VALUES
    (1, 1), -- Skemalægger, Grupperum
    (1, 2), -- Skemalægger, Bachelorum
    (1, 3), -- Skemalægger, Medarbejderum
    (1, 4), -- Skemalægger, Klasselokale
    (1, 5), -- Skemalægger, Auditorium
    (1, 6), -- Skemalægger, Laboratorium
    (1, 7), -- Skemalægger, Hub

    (2, 1), -- Medarbejder, Grupperum
    (2, 2), -- Medarbejder, Bachelorum
    (2, 3), -- Medarbejder, Medarbejderum
    (2, 4), -- Medarbejder, Klasselokale
    -- (2, 5), -- Medarbejder, Auditorium
    (2, 6), -- Medarbejder, Laboratorium
    -- (2, 7), -- Medarbejder, Hub

    (3, 1), -- Studerende, Grupperum
    -- (3, 2), -- Studerende, Bachelorum
    -- (3, 3), -- Studerende, Medarbejderum
    -- (3, 4), -- Studerende, Klasselokale
    -- (3, 5), -- Studerende, Auditorium
    -- (3, 6), -- Studerende, Laboratorium
    -- (3, 7), -- Studerende, Hub

    (4, 1), -- Studerende (Bachelor), Grupperum
    (4, 2)  -- Studerende (Bachelor), Bachelorum
    -- (4, 3), -- Studerende (Bachelor), Medarbejderum
    -- (4, 4), -- Studerende (Bachelor), Klasselokale
    -- (4, 5), -- Studerende (Bachelor), Auditorium
    -- (4, 6), -- Studerende (Bachelor), Laboratorium
    -- (4, 7), -- Studerende (Bachelor), Hub
    ;


