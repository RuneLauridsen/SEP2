package booking.database;

import booking.core.User;

import java.sql.SQLException;
import java.time.LocalDate;

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

        var bookings = persistence.getBookingsForUser(
            rune,
            LocalDate.MIN,
            LocalDate.MAX,
            null
        );

        persistence.deleteBooking(bookings.get(3));

        var bookings2 = persistence.getBookingsForUser(
            rune,
            LocalDate.MIN,
            LocalDate.MAX,
            null
        );

        database.close();
    }
}
