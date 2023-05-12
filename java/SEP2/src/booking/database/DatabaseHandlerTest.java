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

        var bookings = persistence.getActiveBookings(
            rune,
            LocalDate.MIN,
            LocalDate.MAX
        );

        persistence.deleteBooking(bookings.get(3));

        var bookings2 = persistence.getActiveBookings(
            rune,
            LocalDate.MIN,
            LocalDate.MAX
        );

        database.close();
    }
}
