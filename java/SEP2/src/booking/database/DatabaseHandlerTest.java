package booking.database;

import booking.core.BookingInterval;

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

        var allRooms = persistence.getAllRooms();
        var allUsers = persistence.getAllUsers();

        var roomC0206 = allRooms.get(5);
        var userRune = allUsers.get(0);

        var bookings1 = persistence.getActiveBookings(
            userRune,
            LocalDate.MIN,
            LocalDate.MAX
        );

        persistence.insertBooking(
            userRune,
            roomC0206,
            new BookingInterval(
                LocalDate.of(2023, 5, 8),
                LocalTime.of(11, 0),
                LocalTime.of(13, 0)
            )
        );

        var bookings2 = persistence.getActiveBookings(
            allUsers.get(0),
            LocalDate.MIN,
            LocalDate.MAX
        );

        database.close();
    }
}
