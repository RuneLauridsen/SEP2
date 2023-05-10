package booking.database;

import booking.core.User;

import javax.imageio.plugins.jpeg.JPEGImageReadParam;
import java.sql.SQLException;

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


        /*

        var allRooms = persistence.getAllRooms();
        var userRune = persistence.getUser("Rune");

        var roomC0206 = allRooms.get(5);

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
         */

        database.close();
    }
}
