package booking.server.persistene;

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

    // Henter all lokaletyper. Key = RoomType.id.
    public Map<Integer, RoomType> getRoomTypes();

    // Henter bruger ud fra viaid.
    // Returnerer null hvis der ikke findes nogen bruger med viaid'en.
    // Hvis passwordHash er null, ignoreres password.
    public User getUser(int viaid, String passwordHash);

    // Henter bruger ud fra viaid.
    // Ignorerer passwordHash.
    public User getUser(int viaid);

    // Henter et lokale, dets lokaltype og dets bruger-specifikke data.
    public Room getRoom(String room, User activeUser);

    // Henter all lokaler, deres lokaltype og deres bruger-specifikke data.
    public List<Room> getRooms(User activeUser);

    // Henter all bookinger for en bestemt bruger, i et bestemt dato interval.
    // activeUser fortæller hvilken bruger der skal hentes bruger-specifik lokale data for.
    public List<Booking> getBookingsForUser(User user, LocalDate startDate, LocalDate endDate, User activeUser);

    // Henter alle booking for et bestemt lokale, i et bestemt dato interval.
    // activeUser fortæller hvilken bruger der skal hentes bruger-specifik lokale data for.
    public List<Booking> getBookingsForRoom(Room room, LocalDate startDate, LocalDate endDate, User activeUser);

    // Henter alle booking for klasser/hold, som en bestemt bruger er medlem af.
    // activeUser fortæller hvilken bruger der skal hentes bruger-specifik lokale data for.
    public List<Booking> getBookingsForUserGroupUser(User user, LocalDate startDate, LocalDate endDate, User activeUser);

    // Tilføjer en ny booking. Checker ikke efter overlap, lokalets ledighed osv.
    public void createBooking(User activeUser, Room room, BookingInterval bookingInterval, UserGroup userGroup);

    // Sletter en booking. Checker ikke efter tilladelse.
    public void deleteBooking(Booking booking);

    // Henter alle ledige lokaler.
    // minCapacity, maxCapacity, building og floor kan sættes til null, hvis parameteret skal ignoreres.
    // getAvailableRooms tager også højde for brugertyper, dvs. hvis brugertype er studerende
    // vil getAvailableRooms aldrig returnere medarbejderrum, selvom er eller flere medarbejderrum er ledige.
    public List<Room> getAvailableRooms(User activeUser, BookingInterval interval, Integer minCapacity, Integer maxCapacity, Character building, Integer floor);

    // Tilføjer en ny bruger. Returnere false hvis viaid'en er optaget.
    public boolean createUser(String name, String initials, int viaid, String passwordHash, UserType type);

    // Henter alle klasse/hold
    public List<UserGroup> getUserGroups();

    // Henter all brugere i en klasse/hold
    public List<User> getUserGroupUsers(UserGroup userGroup);

    // Opdatere ikke-bruger-specifik data på et lokale.
    public void updateRoom(Room room);

    // Opdatere bruger-specifik data på et lokale.
    public void updateUserRoomData(User user, Room room, String comment, int color);

    // Henter all faste booking intervaller
    public List<TimeSlot> getTimeSlots();

    // Indsætter nyt lokale uden bruger-specifik data.
    public void createRoom(String name, RoomType type, int maxComf, int maxSafety, int size, String comment, boolean isDouble, String doubleName);
    public void deleteRoom(Room room);
}
