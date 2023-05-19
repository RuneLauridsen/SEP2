import booking.database.DatabaseHandler;
import booking.server.model.ServerModel;
import booking.server.model.ServerModelImpl;
import booking.shared.CreateBookingParameters;
import booking.shared.objects.*;
import booking.shared.socketMessages.ErrorResponseReason;

import static booking.shared.socketMessages.ErrorResponseReason.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class TestServerModel
{
    private DatabaseHandler database;
    private ServerModel model;

    @BeforeEach void setup()
    {
        database = TestDatabaseUtil.setup();
        model = new ServerModelImpl(database);
    }

    @AfterEach void setdown()
    {
        TestDatabaseUtil.setdown(database);
    }

    // Tester om server model retunere korrekte overlap,
    // når man prøver at lave en ny booking.
    @Test void testOverlap()
    {
        User user = database.getUser("Rune");
        Room room = database.getRoom("A03.01", user);

        BookingInterval interval0 = new BookingInterval(LocalDate.of(2023, 5, 12), LocalTime.of(8, 0), LocalTime.of(18, 0));
        BookingInterval interval1 = new BookingInterval(LocalDate.of(2023, 5, 12), LocalTime.of(10, 0), LocalTime.of(13, 0));
        BookingInterval interval2 = new BookingInterval(LocalDate.of(2023, 5, 12), LocalTime.of(14, 0), LocalTime.of(15, 0));

        CreateBookingParameters parameters0 = new CreateBookingParameters(room, interval0, false, null);
        CreateBookingParameters parameters1 = new CreateBookingParameters(room, interval1, false, null);
        CreateBookingParameters parameters2 = new CreateBookingParameters(room, interval2, false, null);

        List<Overlap> overlaps0 = new ArrayList<>();
        List<Overlap> overlaps1 = new ArrayList<>();
        List<Overlap> overlaps2 = new ArrayList<>();

        ErrorResponseReason reason0 = model.createBooking(user, parameters0, overlaps0);
        ErrorResponseReason reason1 = model.createBooking(user, parameters1, overlaps1);
        ErrorResponseReason reason2 = model.createBooking(user, parameters2, overlaps2);

        // Forsøg på at lavende en overlappende booking giver ikke ErrorResponse,
        // i stedet får man en list af overlap tilbage.
        assertEquals(reason0, ERROR_RESPONSE_REASON_NONE);
        assertEquals(reason1, ERROR_RESPONSE_REASON_NONE);
        assertEquals(reason2, ERROR_RESPONSE_REASON_NONE);

        assertEquals(overlaps0.size(), 2);
        assertEquals(overlaps1.size(), 1);
        assertEquals(overlaps2.size(), 0);

        assertEquals(overlaps0.get(0).getUsers().size(), 0);
        assertEquals(overlaps0.get(1).getUsers().size(), 0);
        assertEquals(overlaps1.get(0).getUsers().size(), 0);
    }

    // Tester om server model returnerer korrekte overlap med korrekte brugere
    // når man prøver at lave en ny booking.
    @Test void testOverlap_withUserGroups()
    {
        User user = database.getUser("Gitte");
        UserGroup userGroup = database.getUserGroups().get(0);

        Room room = database.getRoom("A03.02", user);

        BookingInterval interval0 = new BookingInterval(LocalDate.of(2023, 5, 12), LocalTime.of(8, 0), LocalTime.of(18, 0));
        BookingInterval interval1 = new BookingInterval(LocalDate.of(2023, 5, 12), LocalTime.of(10, 0), LocalTime.of(13, 0));
        BookingInterval interval2 = new BookingInterval(LocalDate.of(2023, 5, 12), LocalTime.of(14, 0), LocalTime.of(15, 0));

        CreateBookingParameters parameters0 = new CreateBookingParameters(room, interval0, false, userGroup);
        CreateBookingParameters parameters1 = new CreateBookingParameters(room, interval1, false, userGroup);
        CreateBookingParameters parameters2 = new CreateBookingParameters(room, interval2, false, userGroup);

        List<Overlap> overlaps0 = new ArrayList<>();
        List<Overlap> overlaps1 = new ArrayList<>();
        List<Overlap> overlaps2 = new ArrayList<>();

        ErrorResponseReason reason0 = model.createBooking(user, parameters0, overlaps0);
        ErrorResponseReason reason1 = model.createBooking(user, parameters1, overlaps1);
        ErrorResponseReason reason2 = model.createBooking(user, parameters2, overlaps2);

        // Forsøg på at lavende en overlappende booking giver ikke ErrorResponse,
        // i stedet får man en list af overlap tilbage.
        assertEquals(reason0, ERROR_RESPONSE_REASON_NONE);
        assertEquals(reason1, ERROR_RESPONSE_REASON_NONE);
        assertEquals(reason2, ERROR_RESPONSE_REASON_NONE);

        assertEquals(overlaps0.size(), 2);
        assertEquals(overlaps1.size(), 1);
        assertEquals(overlaps2.size(), 0);

        assertEquals(overlaps0.get(0).getUsers().size(), 3);
        assertEquals(overlaps0.get(1).getUsers().size(), 2);
        assertEquals(overlaps1.get(0).getUsers().size(), 3);
    }

    @Test void testLogin()
    {
        User userCorrect = model.login("Gitte", "1234");
        User userIncorrect = model.login("Gitte", "abcd");

        assertEquals(userCorrect.getName(), "Gitte");
        assertEquals(userCorrect.getViaId(), 555555);
        assertEquals(userCorrect.getInitials(), "GITT");
        assertEquals(userCorrect.getType().getName(), "Skemalægger");
        assertEquals(userIncorrect, null);
    }

    @Test void testCreateUser()
    {
        List<UserType> userTypes = model.getUserTypes();

        User userBefore = model.getUser("Test testesen");
        assertEquals(userBefore, null);

        model.createUser(
            "Test testesen",
            "abc123",
            "R2D2",
            123456,
            userTypes.get(2)
        );

        User userWithPassword = model.getUser("Test testesen");
        User userWithoutPassword = model.login("Test testesen", "abc123");

        assertEquals(userWithPassword.getName(), "Test testesen");
        assertEquals(userWithPassword.getInitials(), "R2D2");
        assertEquals(userWithPassword.getViaId(), 123456);
        assertEquals(userWithPassword.getType(), userTypes.get(2));

        assertEquals(userWithoutPassword.getName(), "Test testesen");
        assertEquals(userWithoutPassword.getInitials(), "R2D2");
        assertEquals(userWithoutPassword.getViaId(), 123456);
        assertEquals(userWithoutPassword.getType(), userTypes.get(2));
    }
}
