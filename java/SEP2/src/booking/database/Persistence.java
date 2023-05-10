package booking.database;

import booking.core.Booking;
import booking.core.BookingInterval;
import booking.core.Room;
import booking.core.RoomType;
import booking.core.User;
import booking.core.UserType;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

// TODO(rune): Det bliver et stort interface, men hvilken fordel ville det give at dele op?
// Måske kunne man dele det op i UserPersistence, RoomPersistence, BookingPersistence osv.
public interface Persistence
{
    // Henter all brugertyper. Key = UserType.id.
    public Map<Integer, UserType> getAllUserTypes();

    // Henter alle brugere og deres bruger type.
    public List<User> getAllUsers();

    // Henter all lokaletyper. Key = RoomType.id.
    public Map<Integer, RoomType> getAllRoomTypes();

    // Henter all lokaler og deres lokaletype.
    public List<Room> getAllRooms();

    // Henter all bookinger for en bestemt bruger, i et bestemt dato interval.
    public List<Booking> getActiveBookings(User user, LocalDate startDate, LocalDate endDate);

    // Laver en ny booking. Checker ikke efter overlap, lokalets ledighed osv.
    public void insertBooking(User user, Room room, BookingInterval interval);

    // Henter all ledige lokaler. Tager højde for brugertyper, dvs. hvis brugertype er studerende
    // vil getAvailableRooms aldrig returnere medarbejderrum, selvom er eller flere medarbejderrum er ledige.
    public List<Room> getAvailableRooms(User user, BookingInterval interval);
}
