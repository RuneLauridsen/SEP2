package booking.server.persistene;

import booking.shared.NowProvider;
import booking.shared.objects.Booking;
import booking.shared.objects.BookingInterval;
import booking.shared.objects.Room;
import booking.shared.objects.RoomType;
import booking.shared.objects.TimeSlot;
import booking.shared.objects.User;
import booking.shared.objects.UserGroup;
import booking.shared.objects.UserType;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// NOTE(rune): Cacher entiteter som ikke ændre sig ofte: UserType, RoomType, UserGroup og Users i UserGroup
// Brugeren har ikke mulighed for at tilføje/redigere/slette disse entiteter, så cache invalidation kan
// laves meget simpelt, bare ved at sætte en tidsbegrænsning på hvor lang tid de må ligge i cachen.
public class PersistenceCacheProxy implements Persistence
{
    private final Persistence subject;
    private final long cacheExpirationMillis;
    private final NowProvider nowProvider;

    private Map<Integer, UserType> cachedUserTypes;
    private Map<Integer, RoomType> cachedRoomTypes;
    private List<UserGroup> cachedUserGroups;
    private final Map<UserGroup, List<User>> cachedUserGroupUsers;

    private LocalDateTime latestCacheInvalidation;

    public PersistenceCacheProxy(Persistence subject, long cacheExpirationMillis, NowProvider nowProvider)
    {
        this.subject = subject;
        this.cacheExpirationMillis = cacheExpirationMillis;
        this.nowProvider = nowProvider;
        this.cachedUserGroupUsers = new HashMap<>();
        this.latestCacheInvalidation = nowProvider.nowDateTime();
    }

    private void invalidateCachesIfExpired()
    {
        LocalDateTime now = nowProvider.nowDateTime();

        long millisSinceLastInvalidation = ChronoUnit.MILLIS.between(
            latestCacheInvalidation,
            now
        );

        if (millisSinceLastInvalidation >= cacheExpirationMillis)
        {
            cachedUserTypes = null;
            cachedRoomTypes = null;
            cachedUserGroups = null;
            cachedUserGroupUsers.clear();

            latestCacheInvalidation = now;
        }
    }

    @Override public Map<Integer, UserType> getUserTypes() throws PersistenceException
    {
        invalidateCachesIfExpired();

        if (cachedUserTypes == null)
        {
            cachedUserTypes = subject.getUserTypes();
        }

        return cachedUserTypes;
    }

    @Override public Map<Integer, RoomType> getRoomTypes() throws PersistenceException
    {
        invalidateCachesIfExpired();

        if (cachedRoomTypes == null)
        {
            cachedRoomTypes = subject.getRoomTypes();
        }

        return cachedRoomTypes;
    }

    @Override public List<UserGroup> getUserGroups() throws PersistenceException
    {
        invalidateCachesIfExpired();

        if (cachedUserGroups == null)
        {
            cachedUserGroups = subject.getUserGroups();
        }

        return cachedUserGroups;
    }

    @Override public List<User> getUserGroupUsers(UserGroup userGroup) throws PersistenceException
    {
        invalidateCachesIfExpired();

        if (!cachedUserGroupUsers.containsKey(userGroup))
        {
            cachedUserGroupUsers.put(userGroup, subject.getUserGroupUsers(userGroup));
        }

        return cachedUserGroupUsers.get(userGroup);
    }

    @Override public User getUser(int viaid, String passwordHash) throws PersistenceException
    {
        return subject.getUser(viaid, passwordHash);
    }

    @Override public User getUser(int viaid) throws PersistenceException
    {
        return subject.getUser(viaid);
    }

    @Override public Room getRoom(String room, User activeUser) throws PersistenceException
    {
        return subject.getRoom(room, activeUser);
    }

    @Override public List<Room> getRooms(User activeUser) throws PersistenceException
    {
        return subject.getRooms(activeUser);
    }

    @Override public List<Booking> getBookingsForUser(User user, LocalDate startDate, LocalDate endDate, User activeUser) throws PersistenceException
    {
        return subject.getBookingsForUser(user, startDate, endDate, activeUser);
    }

    @Override public List<Booking> getBookingsForRoom(Room room, LocalDate startDate, LocalDate endDate, User activeUser) throws PersistenceException
    {
        return subject.getBookingsForRoom(room, startDate, endDate, activeUser);
    }

    @Override public List<Booking> getBookingsForUserGroupUser(User user, LocalDate startDate, LocalDate endDate, User activeUser) throws PersistenceException
    {
        return subject.getBookingsForUserGroupUser(user, startDate, endDate, activeUser);
    }

    @Override public void createBooking(User activeUser, Room room, BookingInterval bookingInterval, UserGroup userGroup) throws PersistenceException
    {
        subject.createBooking(activeUser, room, bookingInterval, userGroup);
    }

    @Override public void deleteBooking(Booking booking) throws PersistenceException
    {
        subject.deleteBooking(booking);
    }

    @Override public List<Room> getAvailableRooms(User activeUser, BookingInterval interval, Integer minCapacity, Integer maxCapacity, Character building, Integer floor) throws PersistenceException
    {
        return subject.getAvailableRooms(activeUser, interval, minCapacity, maxCapacity, building, floor);
    }

    @Override public boolean createUser(String name, String initials, int viaid, String passwordHash, UserType type) throws PersistenceException
    {
        return subject.createUser(name, initials, viaid, passwordHash, type);
    }

    @Override public void updateRoom(Room room) throws PersistenceException
    {
        subject.updateRoom(room);
    }

    @Override public void updateUserRoomData(User user, Room room, String comment, int color) throws PersistenceException
    {
        subject.updateUserRoomData(user, room, comment, color);
    }

    @Override public List<TimeSlot> getTimeSlots() throws PersistenceException
    {
        return subject.getTimeSlots();
    }

    @Override public void createRoom(String name, RoomType type, int maxComf, int maxSafety, int size, String comment, boolean isDouble, String doubleName) throws PersistenceException
    {
        subject.createRoom(name, type, maxComf, maxSafety, size, comment, isDouble, doubleName);
    }

    @Override public void deleteRoom(Room room) throws PersistenceException
    {
        subject.deleteRoom(room);
    }
}
