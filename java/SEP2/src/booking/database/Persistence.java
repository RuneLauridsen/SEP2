package booking.database;

import booking.shared.CreateBookingParameters;
import booking.shared.UpdateRoomParameters;
import booking.shared.objects.Booking;
import booking.shared.objects.BookingInterval;
import booking.shared.objects.Room;
import booking.shared.objects.RoomType;
import booking.shared.objects.TimeSlot;
import booking.shared.objects.User;
import booking.shared.objects.UserGroup;
import booking.shared.objects.UserType;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

// TODO(rune): Det bliver et stort interface, men hvilken fordel ville det give at dele op?
// Måske kunne man dele det op i UserPersistence, RoomPersistence, BookingPersistence osv.
public interface Persistence
{
    // Henter all brugertyper. Key = UserType.id.
    public Map<Integer, UserType> getUserTypes();

    public List<BookingInterval> getBookingsFromRoomName(String roomName);

    public boolean isAvailable(String roomName);

    // Henter bruger ud fra brugernavn. Flere brugere kan ikke have samme brugernavn.
    // Returnerer null hvis der ikke findes nogen bruger med brugernavnet.
    public User getUser(String username);

    // Henter et lokale, dets lokaltype og dets bruger-specifikke data.
    public Room getRoom(String room, User activeUser);

    // Henter all lokaletyper. Key = RoomType.id.
    public Map<Integer, RoomType> getRoomTypes();

    // Henter all lokaler, deres lokaltype og deres bruger-specifikke data.
    public List<Room> getRooms(User activeUser);

    // Henter all bookinger for en bestemt bruger, i et bestemt dato interval.
    public List<Booking> getBookingsForUser(User user, LocalDate startDate, LocalDate endDate, User activeUser);

    // Henter alle booking for et bestemt lokale, i et bestemt dato interval.
    public List<Booking> getBookingsForRoom(Room room, LocalDate startDate, LocalDate endDate, User activeUser);

    // Tilføjer en ny booking. Checker ikke efter overlap, lokalets ledighed osv.
    public void createBooking(User activeUser, CreateBookingParameters parameters);

    public void deleteBooking(Booking booking);

    // Henter alle ledige lokaler.
    // minCapacity, maxCapacity, building og floor kan sættes til null, hvis parameteret skal ignoreres.
    // getAvailableRooms tager også højde for brugertyper, dvs. hvis brugertype er studerende
    // vil getAvailableRooms aldrig returnere medarbejderrum, selvom er eller flere medarbejderrum er ledige.
    public List<Room> getAvailableRooms(User activeUser, BookingInterval interval, Integer minCapacity, Integer maxCapacity, Character building, Integer floor);

    // Tilføjer en ny bruger. Returnere false hvis brugernavnet er optaget.
    public boolean createUser(String name, String initials, Integer viaid, String passwordHash, UserType type);

    public List<UserGroup> getUserGroups();
    public List<User> getUserGroupUsers(UserGroup userGroup);
    public void updateRoom(Room room, UpdateRoomParameters parameters);
    public void updateUserRoomData(User user, Room room, String comment, int color);
    public List<TimeSlot> getTimeSlots();
    public void createRoom(String name, RoomType type, int maxComf, int maxSafety, int size, String comment, boolean isDouble, String doubleName);
}
