package booking.database;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;

public class DatabaseHandlerTest
{
    public static void main(String[] args) throws SQLException
    {
        var database = new DatabaseHandler();
        database.open();

        var allRooms = database.getAllRooms();
        var allUsers = database.getAllUsers();

        var roomC0206 = allRooms.get(5);
        var userRune = allUsers.get(0);

        var bookings1 = database.getActiveBookings(
            userRune,
            LocalDate.MIN,
            LocalDate.MAX
        );

        database.insertBooking(
            userRune,
            roomC0206,
            LocalDate.of(2023, 5, 8),
            LocalTime.of(11, 0),
            LocalTime.of(13, 0)
        );

        var bookings2 = database.getActiveBookings(
            allUsers.get(0),
            LocalDate.MIN,
            LocalDate.MAX
        );
    }
}
