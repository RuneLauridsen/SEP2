package test;

import booking.client.model.FileIO;
import booking.server.model.ServerModel;
import booking.server.model.ServerModelException;
import booking.server.model.ServerModelImpl;
import booking.server.persistene.DatabaseHandler;
import booking.server.persistene.PersistenceException;
import booking.shared.CreateBookingParameters;
import booking.shared.GetAvailableRoomsParameters;
import booking.shared.objects.*;
import booking.shared.socketMessages.ErrorResponseReason;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static test.TestConstants.*;
import static booking.shared.socketMessages.ErrorResponseReason.*;

// TODO(rune): Tjek at vi tester all fejlkoder

public class TestServerModel
{
    private DatabaseHandler database;
    private ServerModel model;
    private ServerModel modelBadPersistence;
    private FileIO fileIO;

    @BeforeEach void setup()
    {
        database = TestUtil.setupDatabase();
        model = new ServerModelImpl(database, new FakeNowProvider());
        modelBadPersistence = new ServerModelImpl(new FakePersistenceBad(), new FakeNowProvider());
        fileIO = new FakeFileIO();
    }

    @AfterEach void setdown()
    {
        TestUtil.setdownDatabase(database);
    }

    @Test void testGetUser() throws ServerModelException
    {
        assertEquals(model.getUser(VIAID_GITTE).getName(), "Gitte");
        assertEquals(model.getUser(99), null);
        assertErrorReason(() -> modelBadPersistence.getUser(0), ERROR_RESPONSE_REASON_INTERNAL_SERVER_ERROR);
    }

    @Test void testLogin() throws ServerModelException
    {
        assertEquals(model.login(VIAID_GITTE, "1234").getName(), "Gitte");
        assertErrorReason(() -> model.login(VIAID_GITTE, "123"), ERROR_RESPONSE_REASON_INVALID_CREDENTIALS);
        assertErrorReason(() -> modelBadPersistence.login(0, ""), ERROR_RESPONSE_REASON_INTERNAL_SERVER_ERROR);
    }

    @Test void testGetRoom() throws ServerModelException
    {
        assertEquals(model.getRoom("A02.01", null).getName(), "A02.01");
        assertErrorReason(() -> modelBadPersistence.getRoom("A02.01", null), ERROR_RESPONSE_REASON_INTERNAL_SERVER_ERROR);
    }

    @Test void testGetRooms() throws ServerModelException
    {
        assertEquals(model.getRooms(null).size(), 21);
        assertErrorReason(() -> modelBadPersistence.getRooms(null), ERROR_RESPONSE_REASON_INTERNAL_SERVER_ERROR);
    }

    @Test void testGetRoomTypes() throws ServerModelException
    {
        assertEquals(model.getRoomTypes().size(), 7);
        assertErrorReason(() -> modelBadPersistence.getRoomTypes(), ERROR_RESPONSE_REASON_INTERNAL_SERVER_ERROR);
    }

    @Test void testGetAvailableRooms() throws ServerModelException
    {
        User user = model.getUser(VIAID_MAJA);
        GetAvailableRoomsParameters parameters = new GetAvailableRoomsParameters(
            LocalDate.of(2023, 5, 12),
            LocalTime.of(10, 0),
            LocalTime.of(14, 0)
        );

        parameters.setMinCapacity(5);
        parameters.setMaxCapacity(7);
        parameters.setBuilding('B');
        parameters.setFloor(2);

        assertEquals(model.getAvailableRooms(user, parameters).size(), 1);
        assertErrorReason(() -> model.getAvailableRooms(null, parameters), ERROR_RESPONSE_REASON_NOT_LOGGED_IN);
        assertErrorReason(() -> modelBadPersistence.getAvailableRooms(user, parameters), ERROR_RESPONSE_REASON_INTERNAL_SERVER_ERROR);
    }

    @Test void testCreateBooking() throws ServerModelException
    {
        User maja = model.getUser(VIAID_MAJA);
        User gitte = model.getUser(VIAID_GITTE);

        Room C0309 = model.getRoom("C03.09", null);
        Room C0515 = model.getRoom("C05.15", null);

        assertErrorReason(() -> model.createBooking(maja, new CreateBookingParameters(C0309, new BookingInterval(LocalDate.of(2023, 5, 13), LocalTime.of(10, 0), LocalTime.of(11, 0)), false, null)), ERROR_RESPONSE_REASON_TOO_MANY_ACTIVE_BOOKINGS);
        assertErrorReason(() -> model.createBooking(maja, new CreateBookingParameters(C0515, new BookingInterval(LocalDate.of(2023, 5, 13), LocalTime.of(10, 0), LocalTime.of(11, 0)), false, null)), ERROR_RESPONSE_REASON_ROOM_TYPE_NOT_ALLOWED);
        assertErrorReason(() -> model.createBooking(null, new CreateBookingParameters(C0515, new BookingInterval(LocalDate.of(2023, 5, 13), LocalTime.of(10, 0), LocalTime.of(11, 0)), false, null)), ERROR_RESPONSE_REASON_NOT_LOGGED_IN);
        assertErrorReason(() -> modelBadPersistence.createBooking(maja, new CreateBookingParameters(C0309, new BookingInterval(LocalDate.of(2023, 5, 13), LocalTime.of(10, 0), LocalTime.of(11, 0)), false, null)), ERROR_RESPONSE_REASON_INTERNAL_SERVER_ERROR);

        assertEquals(model.getBookingsForRoom(gitte, "C03.09", LocalDate.MIN, LocalDate.MAX).size(), 0);
        model.createBooking(gitte, new CreateBookingParameters(C0309, new BookingInterval(LocalDate.of(2023, 5, 13), LocalTime.of(10, 0), LocalTime.of(11, 0)), false, null));
        assertEquals(model.getBookingsForRoom(gitte, "C03.09", LocalDate.MIN, LocalDate.MAX).size(), 1);
    }

    // Tester om server model returnere korrekte overlap, når man prøver at lave en ny booking.
    @Test void testCreateBooking_withOverlap() throws ServerModelException, PersistenceException
    {
        User user = database.getUser(VIAID_HENRIK);
        Room room = database.getRoom("A03.01", user);

        BookingInterval interval0 = new BookingInterval(LocalDate.of(2023, 5, 12), LocalTime.of(8, 0), LocalTime.of(18, 0));
        BookingInterval interval1 = new BookingInterval(LocalDate.of(2023, 5, 12), LocalTime.of(10, 0), LocalTime.of(13, 0));
        BookingInterval interval2 = new BookingInterval(LocalDate.of(2023, 5, 12), LocalTime.of(14, 0), LocalTime.of(15, 0));

        CreateBookingParameters parameters0 = new CreateBookingParameters(room, interval0, false, null);
        CreateBookingParameters parameters1 = new CreateBookingParameters(room, interval1, false, null);
        CreateBookingParameters parameters2 = new CreateBookingParameters(room, interval2, false, null);

        List<Overlap> overlaps0 = model.createBooking(user, parameters0);
        List<Overlap> overlaps1 = model.createBooking(user, parameters1);
        List<Overlap> overlaps2 = model.createBooking(user, parameters2);

        assertEquals(overlaps0.size(), 3); // Overlap med begge Majas bookinger + SWE-2023
        assertEquals(overlaps1.size(), 1); // Overlap med en af Majas bookinger
        assertEquals(overlaps2.size(), 0);

        assertEquals(overlaps0.get(0).getUsers().size(), 0);
        assertEquals(overlaps0.get(1).getUsers().size(), 0);
        assertEquals(overlaps0.get(2).getUsers().size(), 1);

        assertEquals(overlaps1.get(0).getUsers().size(), 0);
    }

    // Tester om server model returnerer korrekte overlap med korrekte brugere
    // når man prøver at lave en ny booking.
    @Test void testCreateBooking_withOverlapUserGroups() throws ServerModelException, PersistenceException
    {
        User user = database.getUser(VIAID_GITTE);
        UserGroup userGroup = database.getUserGroups().get(1);

        Room room = database.getRoom("A03.02", user);

        BookingInterval interval0 = new BookingInterval(LocalDate.of(2023, 5, 12), LocalTime.of(8, 0), LocalTime.of(18, 0));
        BookingInterval interval1 = new BookingInterval(LocalDate.of(2023, 5, 12), LocalTime.of(10, 0), LocalTime.of(13, 0));
        BookingInterval interval2 = new BookingInterval(LocalDate.of(2023, 5, 12), LocalTime.of(14, 0), LocalTime.of(15, 0));

        CreateBookingParameters parameters0 = new CreateBookingParameters(room, interval0, false, userGroup);
        CreateBookingParameters parameters1 = new CreateBookingParameters(room, interval1, false, userGroup);
        CreateBookingParameters parameters2 = new CreateBookingParameters(room, interval2, false, userGroup);

        List<Overlap> overlaps0 = model.createBooking(user, parameters0);
        List<Overlap> overlaps1 = model.createBooking(user, parameters1);
        List<Overlap> overlaps2 = model.createBooking(user, parameters2);

        assertEquals(overlaps0.size(), 4);
        assertEquals(overlaps1.size(), 2);
        assertEquals(overlaps2.size(), 0);

        assertEquals(overlaps0.get(0).getUsers().size(), 1); // Overlap med Majas booking
        assertEquals(overlaps0.get(1).getUsers().size(), 1); // Overlap med Majas booking
        assertEquals(overlaps0.get(2).getUsers().size(), 4); // Overlap med SDJ-2023
        assertEquals(overlaps0.get(3).getUsers().size(), 2); // Overlap med SWE-2023
    }

    @Test void testCreateRoom() throws ServerModelException
    {
        User julie = model.getUser(VIAID_JULIE);    // can_edit_rooms = false
        User gitte = model.getUser(VIAID_GITTE);    // can_edit_rooms = true
        RoomType type = model.getRoomTypes().get(1);

        assertErrorReason(() -> model.createRoom(julie, "A02.99", type, 1, 2, 3, "", false, null), ERROR_RESPONSE_REASON_ACCESS_DENIED);
        assertErrorReason(() -> model.createRoom(null, "A02.99", type, 1, 2, 3, "", false, null), ERROR_RESPONSE_REASON_NOT_LOGGED_IN);
        assertErrorReason(() -> modelBadPersistence.createRoom(gitte, "A02.99", type, 1, 2, 3, "", false, null), ERROR_RESPONSE_REASON_INTERNAL_SERVER_ERROR);

        assertEquals(model.getRoom("A02.99", null), null);
        model.createRoom(gitte, "A02.99", type, 1, 2, 3, "", false, null);
        assertEquals(model.getRoom("A02.99", null).getName(), "A02.99");
    }

    @Test void testDeleteBooking() throws ServerModelException
    {
        User rune = model.getUser(VIAID_RUNE);    // can_edit_bookings = false
        User gitte = model.getUser(VIAID_GITTE);    // can_edit_bookings = true

        List<Booking> runeBookings = model.getBookingsForUser(rune, rune, LocalDate.MIN, LocalDate.MAX);
        List<Booking> gitteBookings = model.getBookingsForUser(gitte, gitte, LocalDate.MIN, LocalDate.MAX);

        assertErrorReason(() -> model.deleteBooking(rune, gitteBookings.get(0)), ERROR_RESPONSE_REASON_ACCESS_DENIED);
        assertErrorReason(() -> model.deleteBooking(null, gitteBookings.get(0)), ERROR_RESPONSE_REASON_NOT_LOGGED_IN);
        assertErrorReason(() -> modelBadPersistence.deleteBooking(rune, runeBookings.get(0)), ERROR_RESPONSE_REASON_INTERNAL_SERVER_ERROR);

        assertEquals(model.getBookingsForUser(rune, rune, LocalDate.MIN, LocalDate.MAX).size(), 3);
        model.deleteBooking(rune, runeBookings.get(0));
        assertEquals(model.getBookingsForUser(rune, rune, LocalDate.MIN, LocalDate.MAX).size(), 2);

    }

    @Test void testUpdateRoom() throws ServerModelException
    {
        User henrik = model.getUser(VIAID_HENRIK);  // can_edit_rooms = false
        User gitte = model.getUser(VIAID_GITTE);    // can_edit_rooms = true
        Room room = model.getRoom("A02.01", null);
        room.setComment("comment");

        assertErrorReason(() -> model.updateRoom(null, room), ERROR_RESPONSE_REASON_NOT_LOGGED_IN);
        assertErrorReason(() -> model.updateRoom(henrik, room), ERROR_RESPONSE_REASON_ACCESS_DENIED);
        assertErrorReason(() -> modelBadPersistence.updateRoom(gitte, room), ERROR_RESPONSE_REASON_INTERNAL_SERVER_ERROR);
        model.updateRoom(gitte, room);

        Room roomAfterUpdate = model.getRoom("A02.01", null); // Tjek om comment er gemt i databasen
        assertEquals(roomAfterUpdate.getComment(), "comment");
    }

    private static void assertErrorReason(Executable executable, ErrorResponseReason reason)
    {
        try
        {
            executable.execute();
            assertTrue(false);
        }
        catch (ServerModelException e)
        {
            assertEquals(e.getReason(), reason);
        }
        catch (Throwable e)
        {
            throw new RuntimeException(e);
        }
    }
}
