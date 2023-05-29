package test;

import booking.client.model.FileIO;
import booking.server.model.ServerModel;
import booking.server.model.ServerModelException;
import booking.server.model.ServerModelImpl;
import booking.server.persistene.DatabaseHandler;
import booking.server.persistene.PersistenceException;
import booking.shared.CreateBookingParameters;
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

    @Test void testUpdateRoom() throws ServerModelException
    {
        User user1 = model.getUser(VIAID_HENRIK);   // can_edit_rooms = false
        User user2 = model.getUser(VIAID_GITTE);    // can_edit_rooms = true
        Room room = model.getRoom("A02.01", null);
        room.setComment("comment");

        assertErrorReason(() -> model.updateRoom(null, room), ERROR_RESPONSE_REASON_NOT_LOGGED_IN);
        assertErrorReason(() -> model.updateRoom(user1, room), ERROR_RESPONSE_REASON_INVALID_CREDENTIALS);
        assertErrorReason(() -> modelBadPersistence.updateRoom(user2, room), ERROR_RESPONSE_REASON_INTERNAL_SERVER_ERROR);
        model.updateRoom(user2, room);

        Room roomAfterUpdate = model.getRoom("A02.01", null); // Tjek om comment er gemt i databasen
        assertEquals(roomAfterUpdate.getComment(), "comment");
    }

    // Tester om server model returnere korrekte overlap, når man prøver at lave en ny booking.
    @Test void testOverlap() throws ServerModelException, PersistenceException
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
    @Test void testOverlap_withUserGroups() throws ServerModelException, PersistenceException
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

    @Test void testLogin() throws ServerModelException
    {
        User user = model.login(VIAID_GITTE, "1234");
        assertEquals(user.getName(), "Gitte");
        assertEquals(user.getViaId(), 555555);
        assertEquals(user.getInitials(), "GITT");
        assertEquals(user.getType().getName(), "Skemalægger");
    }

    @Test void testLogin_invalidCredentials()
    {
        assertThrows(ServerModelException.class, () -> model.login(VIAID_GITTE, "forkert password"), "Invalid credentials");
    }

    @Test void testCreateUser() throws ServerModelException
    {
        final int VIAID_TEST = 123456;

        List<UserType> userTypes = model.getUserTypes();

        User userBefore = model.getUser(VIAID_TEST);
        assertEquals(userBefore, null);

        model.createUser(
            "Test testesen",
            "abc123",
            "R2D2",
            VIAID_TEST,
            userTypes.get(2)
        );

        User userWithPassword = model.getUser(VIAID_TEST);
        User userWithoutPassword = model.login(VIAID_TEST, "abc123");

        assertEquals(userWithPassword.getName(), "Test testesen");
        assertEquals(userWithPassword.getInitials(), "R2D2");
        assertEquals(userWithPassword.getViaId(), VIAID_TEST);
        assertEquals(userWithPassword.getType(), userTypes.get(2));

        assertEquals(userWithoutPassword.getName(), "Test testesen");
        assertEquals(userWithoutPassword.getInitials(), "R2D2");
        assertEquals(userWithoutPassword.getViaId(), VIAID_TEST);
        assertEquals(userWithoutPassword.getType(), userTypes.get(2));
    }

    void assertErrorReason(Executable executable, ErrorResponseReason reason)
    {
        assertThrows(ServerModelException.class, executable, reason.getMessage());
    }
}
