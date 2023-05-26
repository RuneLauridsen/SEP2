package test;

import booking.server.persistene.DatabaseHandler;
import booking.server.persistene.PersistenceException;
import booking.shared.objects.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static test.TestConstants.*;

// TODO(rune): Tjek at alle database funktioner er testet
// (kig i Persistence interfacet)

public class TestDatabase
{
    private DatabaseHandler database;

    @BeforeEach void setup()
    {
        database = TestDatabaseUtil.setup();
    }

    @AfterEach void setdown()
    {
        TestDatabaseUtil.setdown(database);
    }

    @Test void testGetUserTypes() throws PersistenceException
    {
        Map<Integer, UserType> userTypes = database.getUserTypes();

        assertEquals(userTypes.size(), 4);

        assertEquals(userTypes.get(1).getId(), 1);
        assertEquals(userTypes.get(1).getName(), "Skemalægger");
        assertEquals(userTypes.get(1).canEditUsers(), true);
        assertEquals(userTypes.get(1).canEditRooms(), true);
        assertEquals(userTypes.get(1).canEditBookings(), true);
        assertEquals(userTypes.get(1).canOverlapBookings(), true);
        assertEquals(userTypes.get(1).getMaxBookingCount(), 99999);
        assertEquals(userTypes.get(1).getAllowedRoomTypes().size(), 7);

        assertEquals(userTypes.get(3).getId(), 3);
        assertEquals(userTypes.get(3).getName(), "Studerende");
        assertEquals(userTypes.get(3).canEditUsers(), false);
        assertEquals(userTypes.get(3).canEditRooms(), false);
        assertEquals(userTypes.get(3).canEditBookings(), false);
        assertEquals(userTypes.get(3).canOverlapBookings(), true);
        assertEquals(userTypes.get(3).getMaxBookingCount(), 2);
        assertEquals(userTypes.get(3).getAllowedRoomTypes().size(), 1);
    }

    @Test void testGetRoomTypes() throws PersistenceException
    {
        Map<Integer, RoomType> roomTypes = database.getRoomTypes();

        assertEquals(roomTypes.size(), 7);

        assertEquals(roomTypes.get(3).getId(), 3);
        assertEquals(roomTypes.get(3).getName(), "Medarbejderrum");

        assertEquals(roomTypes.get(4).getId(), 4);
        assertEquals(roomTypes.get(4).getName(), "Klasselokale");
    }

    @Test void testGetUser() throws PersistenceException
    {
        User user = database.getUser(VIAID_GITTE);

        assertEquals(user.getId(), 5);
        assertEquals(user.getName(), "Gitte");
        assertEquals(user.getInitials(), "GITT");
        assertEquals(user.getViaId(), 555555); // TODO(rune): Har medarbejdere viaid ligesom studerende?
        assertEquals(user.getType().getId(), 1);
    }

    @Test void testGetUser_notFound() throws PersistenceException
    {
        User user = database.getUser(99999);
        assertEquals(user, null);
    }

    @Test void testGetUser_withPasswordHash() throws PersistenceException
    {
        User userCorrect = database.getUser(VIAID_RUNE, "1081D018BC52CF305616BFFDA861FFF2");
        User userIncorrect = database.getUser(VIAID_RUNE, "0123456789ABCDEF0123456789ABCDEF");
        assertNotEquals(userCorrect, null);
        assertEquals(userIncorrect, null);
    }

    @Test void testGetRoom() throws PersistenceException
    {
        User user = database.getUser(VIAID_RUNE);
        Room room = database.getRoom("C06.01", user);

        assertEquals(room.getId(), 20);
        assertEquals(room.getName(), "C06.01");
        assertEquals(room.getSize(), 1);
        assertEquals(room.getComfortCapacity(), 11);
        assertEquals(room.getFireCapacity(), 111);
        assertEquals(room.getComment(), "");
        assertEquals(room.getType().getId(), 3);
        assertEquals(room.getUserComment(), "");
        assertEquals(room.getUserColor(), -1);
    }

    @Test void testGetRoom_notFound() throws PersistenceException
    {
        User user = database.getUser(VIAID_RUNE);
        Room room = database.getRoom("AAAAA", user);

        assertEquals(room, null);
    }

    @Test void testGetRooms() throws PersistenceException
    {
        User userGitte = database.getUser(VIAID_GITTE);
        User userSimon = database.getUser(VIAID_SIMON);

        List<Room> roomsGitte = database.getRooms(userGitte);
        List<Room> roomsSimon = database.getRooms(userSimon);

        assertEquals(roomsGitte.size(), 21);
        assertEquals(roomsSimon.size(), 21);

        assertEquals(roomsGitte.get(0).getId(), 1);
        assertEquals(roomsGitte.get(0).getName(), "A02.01");
        assertEquals(roomsGitte.get(0).getSize(), 1);
        assertEquals(roomsGitte.get(0).getComfortCapacity(), 11);
        assertEquals(roomsGitte.get(0).getFireCapacity(), 111);
        assertEquals(roomsGitte.get(0).getComment(), "global kommentar");
        assertEquals(roomsGitte.get(0).getType().getName(), "Grupperum");
        assertEquals(roomsGitte.get(0).getUserComment(), "jeg hedder gitte");
        assertEquals(roomsGitte.get(0).getUserColor(), -2715924);

        assertEquals(roomsSimon.get(0).getUserComment(), "");
        assertEquals(roomsSimon.get(0).getUserColor(), -1);
    }

    @Test void testGetBookingsForUser() throws PersistenceException
    {
        User user = database.getUser(VIAID_GITTE);

        // NOTE(rune): 2023-05-12 så vi tester at bookingen til 2023-05-11 ikke kommer med.
        List<Booking> bookings = database.getBookingsForUser(user, LocalDate.of(2023, 5, 12), LocalDate.MAX, user);

        assertEquals(bookings.size(), 3);

        assertEquals(bookings.get(2).getUser().getName(), "Gitte");
        assertEquals(bookings.get(2).getRoom().getName(), "A03.02");
        assertEquals(bookings.get(2).getInterval().getDate(), LocalDate.of(2023, 5, 12));
        assertEquals(bookings.get(2).getInterval().getStart(), LocalTime.of(16, 0));
        assertEquals(bookings.get(2).getInterval().getEnd(), LocalTime.of(16, 45));

        assertEquals(bookings.get(0).getUserGroup(), null);
        assertEquals(bookings.get(1).getUserGroup().getName(), "SDJ-2023");
    }

    @Test void testGetBookingsForRoom() throws PersistenceException
    {
        Room room = database.getRoom("A03.02", null);
        List<Booking> bookings = database.getBookingsForRoom(
            room,
            LocalDate.of(2023, 5, 12),
            LocalDate.MAX,
            null
        );

        assertEquals(bookings.size(), 2);

        assertEquals(bookings.get(1).getUser().getName(), "Gitte");
        assertEquals(bookings.get(1).getRoom().getName(), "A03.02");
        assertEquals(bookings.get(1).getInterval().getDate(), LocalDate.of(2023, 5, 12));
        assertEquals(bookings.get(1).getInterval().getStart(), LocalTime.of(16, 0));
        assertEquals(bookings.get(1).getInterval().getEnd(), LocalTime.of(16, 45));
    }

    @Test void testGetBookingsForUserGroupUser() throws PersistenceException
    {
        User user = database.getUser(VIAID_SIMON);

        List<Booking> bookings = database.getBookingsForUserGroupUser(user, LocalDate.MIN, LocalDate.MAX, null);

        assertEquals(bookings.size(), 2);

        assertEquals(bookings.get(0).getUserGroup().getName(), "SDJ-2023");
        assertEquals(bookings.get(1).getUserGroup().getName(), "SWE-2023");
    }

    @Test void testCreateBooking() throws PersistenceException
    {
        User user = database.getUser(VIAID_RUNE);
        Room room = database.getRoom("A02.02", user);

        List<Booking> bookingsBefore = database.getBookingsForRoom(
            room,
            LocalDate.MIN,
            LocalDate.MAX,
            null
        );

        BookingInterval interval = new BookingInterval(
            LocalDate.of(2023, 5, 11),
            LocalTime.of(14, 0),
            LocalTime.of(15, 30)
        );

        database.createBooking(user, room, interval, null);

        List<Booking> bookingsAfter = database.getBookingsForRoom(
            room,
            LocalDate.MIN,
            LocalDate.MAX,
            null
        );

        assertEquals(bookingsBefore.size(), 1);
        assertEquals(bookingsAfter.size(), 2);

        assertEquals(bookingsAfter.get(1).getRoom().getName(), "A02.02");
        assertEquals(bookingsAfter.get(1).getInterval(), interval);
        assertEquals(bookingsAfter.get(1).getUser().getName(), "Rune");
        assertEquals(bookingsAfter.get(1).getUserGroup(), null);
    }

    @Test void testCreateBooking_withUserGroup() throws PersistenceException
    {
        User user = database.getUser(VIAID_GITTE);
        Room room = database.getRoom("B03.05", user);
        UserGroup userGroup = database.getUserGroups().get(1);

        List<Booking> bookingsBefore = database.getBookingsForRoom(
            room,
            LocalDate.MIN,
            LocalDate.MAX,
            null
        );

        BookingInterval interval = new BookingInterval(
            LocalDate.of(2023, 5, 11),
            LocalTime.of(14, 0),
            LocalTime.of(15, 30)
        );

        database.createBooking(user, room, interval, userGroup);

        List<Booking> bookingsAfter = database.getBookingsForRoom(
            room,
            LocalDate.MIN,
            LocalDate.MAX,
            null
        );

        assertEquals(bookingsBefore.size(), 0);
        assertEquals(bookingsAfter.size(), 1);

        assertEquals(bookingsAfter.get(0).getRoom().getName(), "B03.05");
        assertEquals(bookingsAfter.get(0).getInterval(), interval);
        assertEquals(bookingsAfter.get(0).getUser().getName(), "Gitte");
        assertEquals(bookingsAfter.get(0).getUserGroup().getName(), "SDJ-2023");
    }

    @Test void testDeleteBooking() throws PersistenceException
    {
        Room room = database.getRoom("A03.01", null);

        List<Booking> bookingsBefore = database.getBookingsForRoom(room, LocalDate.MIN, LocalDate.MAX, null);
        database.deleteBooking(bookingsBefore.get(0));
        List<Booking> bookingsAfter = database.getBookingsForRoom(room, LocalDate.MIN, LocalDate.MAX, null);

        assertEquals(bookingsBefore.size(), 2);
        assertEquals(bookingsAfter.size(), 1);
    }

    @Test void testGetAvailableRooms() throws PersistenceException
    {
        // TODO(rune): Gør testGetAvailableRooms færdig. Der er mange parametre!!!

        User user = database.getUser(VIAID_JULIE);

        // NOTE(rune): Tester kun med dato tid
        List<Room> rooms1 = database.getAvailableRooms(user, new BookingInterval(LocalDate.of(2023, 5, 12), LocalTime.of(10, 0), LocalTime.of(14, 0)), 4, null, null, null);

        // NOTE(rune): Tester med filtre, måde hver for sig og kombineret
        List<Room> rooms2 = database.getAvailableRooms(user, new BookingInterval(LocalDate.of(2023, 5, 12), LocalTime.of(10, 0), LocalTime.of(14, 0)), null, 4, null, null);
        List<Room> rooms3 = database.getAvailableRooms(user, new BookingInterval(LocalDate.of(2023, 5, 12), LocalTime.of(10, 0), LocalTime.of(14, 0)), null, null, 'B', null);
        List<Room> rooms4 = database.getAvailableRooms(user, new BookingInterval(LocalDate.of(2023, 5, 12), LocalTime.of(10, 0), LocalTime.of(14, 0)), null, null, null, 3);
        List<Room> rooms5 = database.getAvailableRooms(user, new BookingInterval(LocalDate.of(2023, 5, 12), LocalTime.of(10, 0), LocalTime.of(14, 0)), null, null, null, null);
        List<Room> rooms6 = database.getAvailableRooms(user, new BookingInterval(LocalDate.of(2023, 5, 12), LocalTime.of(10, 0), LocalTime.of(14, 0)), 5, 7, 'A', 2);

        // NOTE(rune): Tester at 11:00-13:00 overlapped med 11:00-13:00
        List<Room> rooms7 = database.getAvailableRooms(user, new BookingInterval(LocalDate.of(2023, 5, 12), LocalTime.of(11, 0), LocalTime.of(13, 0)), null, null, 'A', 2);

        // NOTE(rune): Tester at 11:00-13:00 ikke overlapper med 10:00-11:00 eller 13:00-14:00
        List<Room> rooms8 = database.getAvailableRooms(user, new BookingInterval(LocalDate.of(2023, 5, 12), LocalTime.of(10, 0), LocalTime.of(11, 0)), null, null, 'A', 2);
        List<Room> rooms9 = database.getAvailableRooms(user, new BookingInterval(LocalDate.of(2023, 5, 12), LocalTime.of(13, 0), LocalTime.of(14, 0)), null, null, 'A', 2);
    }

    @Test void testCreateUser() throws PersistenceException
    {
        final int VIAID_TEST = 123456;

        Map<Integer, UserType> userTypes = database.getUserTypes();

        User userBefore = database.getUser(VIAID_TEST);
        assertEquals(userBefore, null);

        database.createUser(
            "Test testesen",
            "R2D2",
            VIAID_TEST,
            "0123456789abcdef0123456789abcdef",
            userTypes.get(2)
        );

        User userAfter = database.getUser(VIAID_TEST);

        assertEquals(userAfter.getName(), "Test testesen");
        assertEquals(userAfter.getInitials(), "R2D2");
        assertEquals(userAfter.getViaId(), VIAID_TEST);
        assertEquals(userAfter.getType(), userTypes.get(2));
    }

    @Test void testGetUserGroups() throws PersistenceException
    {
        List<UserGroup> userGroups = database.getUserGroups();

        assertEquals(userGroups.size(), 3);
        assertEquals(userGroups.get(0).getName(), "DBS-2023");
        assertEquals(userGroups.get(1).getName(), "SDJ-2023");
        assertEquals(userGroups.get(2).getName(), "SWE-2023");
    }

    @Test void testGetUserGroupUsers() throws PersistenceException
    {
        List<UserGroup> userGroups = database.getUserGroups();
        List<User> users = database.getUserGroupUsers(userGroups.get(1));

        assertEquals(users.size(), 4);
        assertEquals(users.get(1).getName(), "Maja");
        assertEquals(users.get(1).getInitials(), null);
        assertEquals(users.get(1).getViaId(), VIAID_MAJA);
        assertEquals(users.get(1).getType().getName(), "Studerende");
    }

    @Test void testUpdateRoom() throws PersistenceException
    {
        User user = database.getUser(VIAID_GITTE);
        Room roomToUpdate = database.getRoom("A02.03", user);

        assertEquals(roomToUpdate.getName(), "A02.03");
        assertEquals(roomToUpdate.getSize(), 3);
        assertEquals(roomToUpdate.getComfortCapacity(), 33);
        assertEquals(roomToUpdate.getFireCapacity(), 333);
        assertEquals(roomToUpdate.getComment(), "");
        assertEquals(roomToUpdate.getType().getId(), 1);

        roomToUpdate.setName("A02.99");
        roomToUpdate.setSize(90);
        roomToUpdate.setComfortCapacity(91);
        roomToUpdate.setComment("new global comment");

        // TODO(rune): Test kører igennem selvom updateRoom er udkommenteret
        // database.updateRoom(roomToUpdate);

        assertEquals(roomToUpdate.getName(), "A02.99");
        assertEquals(roomToUpdate.getSize(), 90);
        assertEquals(roomToUpdate.getComfortCapacity(), 91);
        assertEquals(roomToUpdate.getFireCapacity(), 333);
        assertEquals(roomToUpdate.getComment(), "new global comment");
        assertEquals(roomToUpdate.getType().getId(), 1);
    }

    @Test void testUpdateUserRoomData() throws PersistenceException
    {
        User user = database.getUser(VIAID_GITTE);
        Room roomBefore = database.getRoom("A02.01", user);
        database.updateUserRoomData(user, roomBefore, "jeg hedder gitte 2", 0x00ff00ff);
        Room roomAfter = database.getRoom("A02.01", user);

        assertEquals(roomBefore.getUserComment(), "jeg hedder gitte");
        assertEquals(roomBefore.getUserColor(), -2715924);
        assertEquals(roomAfter.getUserComment(), "jeg hedder gitte 2");
        assertEquals(roomAfter.getUserColor(), 0x00ff00ff);
    }

    @Test void testUpdateUserRoomData_insert() throws PersistenceException
    {
        // NOTE(rune): A02.03 har ikke Gitte-specifik data i forvejen, så her
        // tester vi også at indsætte en ny user_room_data række.

        User user = database.getUser(VIAID_GITTE);
        Room roomBefore = database.getRoom("A02.03", user);
        database.updateUserRoomData(user, roomBefore, "test comment", 0xff00ff00);
        Room roomAfter = database.getRoom("A02.03", user);

        assertEquals(roomBefore.getUserComment(), "");
        assertEquals(roomBefore.getUserColor(), -1);
        assertEquals(roomAfter.getUserComment(), "test comment");
        assertEquals(roomAfter.getUserColor(), 0xff00ff00);
    }

    @Test void testGetTimeSlots() throws PersistenceException
    {
        List<TimeSlot> timeSlots = database.getTimeSlots();

        assertEquals(timeSlots.size(), 2);
        assertEquals(timeSlots.get(0).getStart(), LocalTime.of(8, 20));
        assertEquals(timeSlots.get(0).getEnd(), LocalTime.of(11, 50));
        assertEquals(timeSlots.get(1).getStart(), LocalTime.of(12, 45));
        assertEquals(timeSlots.get(1).getEnd(), LocalTime.of(16, 5));
    }

    @Test void testCreateRoom() throws PersistenceException
    {
        Map<Integer, RoomType> roomTypes = database.getRoomTypes();

        Room roomBefore = database.getRoom("A02.99", null);

        database.createRoom(
            "A02.99",
            roomTypes.get(1),
            42,
            43,
            44,
            "kommentar",
            false,
            ""
        );

        Room roomAfter = database.getRoom("A02.99", null);

        assertEquals(roomBefore, null);
        assertEquals(roomAfter.getName(), "A02.99");
        assertEquals(roomAfter.getType().getName(), "Grupperum");
        assertEquals(roomAfter.getComfortCapacity(), 42);
        assertEquals(roomAfter.getFireCapacity(), 43);
        assertEquals(roomAfter.getSize(), 44);
        assertEquals(roomAfter.getComment(), "kommentar");
        assertEquals(roomAfter.getUserComment(), "");
        assertEquals(roomAfter.getUserColor(), -1);
    }
}

