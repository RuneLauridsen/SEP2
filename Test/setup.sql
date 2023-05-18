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
    can_edit_bookings       bool        NOT NULL , -- kan redigere/slette andre folks bookinger
    can_overlap_bookings    bool        NOT NULL , -- kan lave bookinger som overlapper med andre bookinger
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
CREATE TABLE user_group_user
(
    user_group_id           int         NOT NULL REFERENCES user_group(user_group_id),
    user_id                 int         NOT NULL REFERENCES "user"(user_id),

    PRIMARY KEY (user_group_id, user_id)
);

-- Til dobbeltlokaler, triplelokaler, quadruppellokaler osv.
CREATE TABLE multi_room
(
    multi_room_id           serial      NOT NULL PRIMARY KEY
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

    room_type_id            int         NOT NULL REFERENCES room_type(room_type_id),
    multi_room_id           int         NULL     REFERENCES multi_room(multi_room_id)
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
    booking_date            date        NOT NULL,
    booking_start_time      time        NOT NULL,
    booking_end_time        time        NOT NULL,
    room_id                 int         NOT NULL REFERENCES "room"(room_id) ,
    user_id                 int         NOT NULL REFERENCES "user"(user_id) ,
    user_group_id           int         NULL     REFERENCES "user_group"(user_group_id)
);


INSERT INTO user_type
    (user_type_name, can_edit_users, can_edit_rooms, can_edit_bookings, can_overlap_bookings, max_booking_count)
VALUES
    /* id = 1 */ ('Skemalægger',            true,  true,  true,  true,  99999),
    /* id = 2 */ ('Medarbejder',            false, false, false, false, 99999),
    /* id = 3 */ ('Studerende',             false, false, false, false, 2),
    /* id = 4 */ ('Studerende (Bachelor)',  false, false, false, false, 2);

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


INSERT INTO room
    (room_name, room_size, room_comfort_capacity, room_fire_capacity, room_comment, room_type_id, multi_room_id)
VALUES
    /* id = 1 */ ('A02.01', 1, 11, 111, '', 1, NULL),
    /* id = 2 */ ('A02.02', 2, 22, 222, '', 1, NULL),
    /* id = 3 */ ('A02.03', 3, 33, 333, '', 1, NULL),
    /* id = 4 */ ('B02.04', 4, 44, 444, '', 1, NULL),
    /* id = 5 */ ('B02.05', 5, 55, 555, '', 1, NULL),
    /* id = 6 */ ('B02.06', 6, 66, 666, '', 1, NULL),
    /* id = 7 */ ('C02.07', 7, 77, 777, '', 1, NULL),
    /* id = 8 */ ('C02.08', 8, 88, 888, '', 1, NULL),
    /* id = 9 */ ('C02.09', 9, 99, 999, '', 1, NULL),

    /* id = 11 */ ('A03.00', 0, 00, 000, '', 1, NULL),
    /* id = 11 */ ('A03.01', 1, 11, 111, '', 1, NULL),
    /* id = 12 */ ('A03.02', 2, 22, 222, '', 1, NULL),
    /* id = 13 */ ('A03.03', 3, 33, 333, '', 1, NULL),
    /* id = 14 */ ('B03.04', 4, 44, 444, '', 1, NULL),
    /* id = 15 */ ('B03.05', 5, 55, 555, '', 1, NULL),
    /* id = 16 */ ('B03.06', 6, 66, 666, '', 1, NULL),
    /* id = 17 */ ('C03.07', 7, 77, 777, '', 1, NULL),
    /* id = 18 */ ('C03.08', 8, 88, 888, '', 1, NULL),
    /* id = 19 */ ('C03.09', 9, 99, 999, '', 1, NULL),

    -- bachelor lokale
    /* id = 20 */ ('C06.01', 1, 11, 111, '', 3, NULL),

    -- klasslokale
    /* id = 20 */ ('C05.15', 2, 22, 222, '', 4, NULL);

INSERT INTO "user"
    (user_name, user_initials, user_viaid, user_password_hash, user_type_id)
VALUES
    /* id = 1 */ ('Maja',   null,  111111, 0, 3),
    /* id = 2 */ ('Julie',  null,  222222, 0, 3),
    /* id = 3 */ ('Simon',  null,  333333, 0, 3),
    /* id = 4 */ ('Rune',   null,  444444, 0, 3),
    /* id = 5 */ ('Gitte', 'GITT', 555555, 0, 1);

INSERT INTO user_room_data
    (user_id, room_id, color, comment)
VALUES
    /* Gitte, A02.01 */ (5, 1, 99, 'jeg hedder gitte');

INSERT INTO course
    (course_name, course_time_slot_count)
VALUES
    ('SDJ', 10),
    ('DBS', 20),
    ('SWE', 30);

INSERT INTO user_group
    (user_group_name, course_id)
VALUES
    ('SDJ-2023', 1),
    ('DBS-2023', 2);

INSERT INTO user_group_user
    (user_group_id, user_id)
VALUES
    /* SDJ Maja  */ (1, 1),
    /* SDJ Julie */ (1, 2),
    /* SDJ Simon */ (1, 3),

    /* DBS Julie */ (2, 2),
    /* DBS Simon */ (2, 3),
    /* DBS Rune  */ (2, 4);

INSERT INTO time_slot
    (time_slot_start, time_slot_end)
VALUES
    ('8:20', '11:50'),
    ('12:45', '16:05');

INSERT INTO booking
    (user_id, room_id, booking_date, booking_start_time, booking_end_time, user_group_id)
VALUES
    /* Rune,  A02.01 */ (4, 1,  '2023-05-08', '10:00', '16:00', NULL),
    /* Rune,  A02.02 */ (4, 2,  '2023-05-09', '10:00', '16:00', NULL),
    /* Rune,  A02.03 */ (4, 3,  '2023-05-10', '10:00', '16:00', NULL),
    /* Gitte, B02.04 */ (5, 4,  '2023-05-11', '10:00', '16:00', NULL),
    /* Gitte, B02.05 */ (5, 5,  '2023-05-12', '10:00', '16:00', NULL),

    -- til testOverlap
    /* Maja,  A03.01 */ (5, 11, '2023-05-12', '11:00', '13:00', NULL),
    /* Maja,  A03.01 */ (5, 11, '2023-05-12', '16:00', '16:45', NULL),


    -- til testOverlap_withUserGroups
    /* Gitte,  A03.02 SDJ */ (5, 12, '2023-05-12', '11:00', '13:00', 1),
    /* Gitte,  A03.02 DBS */ (5, 12, '2023-05-12', '16:00', '16:45', 2);
