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
    -- TODO: Andre privilegier?
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



INSERT INTO user_type
    (user_type_name, can_edit_users)
VALUES
    /* 1 */ ('Admin', true),
    /* 2 */ ('Medarbejder', true),
    /* 3 */ ('Studerende', true),
    /* 4 */ ('Studerende (Bachelor)', true);

INSERT INTO room_type
    (room_type_name)
VALUES
    /* 1 */ ('Grupperum'),
    /* 2 */ ('Bachelorrum'),
    /* 3 */ ('Medarbejderum'),
    /* 4 */ ('Klasselokale'),
    /* 5 */ ('Auditorium'),
    /* 6 */ ('Laboratorium'),
    /* 7 */ ('Hub');

INSERT INTO user_type_allowed_room_type
    (user_type_id, room_type_id)
VALUES
    (1, 1), -- Admin, Grupperum
    (1, 2), -- Admin, Bachelorum
    (1, 3), -- Admin, Medarbejderum
    (1, 4), -- Admin, Klasselokale
    (1, 5), -- Admin, Auditorium
    (1, 6), -- Admin, Laboratorium
    (1, 7), -- Admin, Hub

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

SELECT ut.user_type_name, rt.room_type_name
FROM user_type_allowed_room_type utart
INNER JOIN user_type ut ON utart.user_type_id = ut.user_type_id
INNER JOIN room_type rt ON utart.room_type_id = rt.room_type_id;