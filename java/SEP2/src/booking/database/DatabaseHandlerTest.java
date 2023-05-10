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

        //        persistence.insertBooking(
        //            userRune,
        //            roomC0206,
        //            new BookingInterval(
        //                LocalDate.of(2023, 5, 8),
        //                LocalTime.of(19, 0),
        //                LocalTime.of(21, 0)
        //            )
        //        );

        var rooms = persistence.getAvailableRooms(
            userRune,
            new BookingInterval(
                LocalDate.of(2023, 5, 8),
                LocalTime.of(11, 10),
                LocalTime.of(11, 20)
            )
        );

        database.close();
    }
}
