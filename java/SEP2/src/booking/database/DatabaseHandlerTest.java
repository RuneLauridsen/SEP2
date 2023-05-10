package booking.database;

import booking.core.BookingInterval;
import booking.core.User;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;

public class DatabaseHandlerTest
{
    public static void main(String[] args) throws SQLException
    {
        var database = new DatabaseHandler();
        database.open();
        var persistence = (Persistence) database;

        var userTypes = persistence.getUserTypes();

        User rune = persistence.getUser("Rune");
        User gitte = persistence.getUser("Gitte");
        User simon = persistence.getUser("Simon");

        var rooms = persistence.getAvailableRooms(
            rune,
            new BookingInterval(
                LocalDate.of(2023, 5, 30),
                LocalTime.of(10, 0),
                LocalTime.of(16, 0)
            ),
            13,       // min cap
            15,       // max cap
            null,       // bygning
            null        // etage
        );

        database.close();
    }
}
