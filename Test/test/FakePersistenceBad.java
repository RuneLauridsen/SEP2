package test;

import booking.server.persistene.Persistence;
import booking.server.persistene.PersistenceException;
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

public class FakePersistenceBad implements Persistence
{
    // NOTE(rune): Så det er nemmere at kende forskel på "rigtige" database fejl og dem fra FakePersistenceBad i stack trace
    class FakePersistenceException extends PersistenceException
    {
        public FakePersistenceException()
        {
            super(null);
        }
    }

    @Override public Map<Integer, UserType> getUserTypes() throws PersistenceException
    {
        throw new FakePersistenceException();
    }

    @Override public Map<Integer, RoomType> getRoomTypes() throws PersistenceException
    {
        throw new FakePersistenceException();
    }

    @Override public User getUser(int viaid, String passwordHash) throws PersistenceException
    {
        throw new FakePersistenceException();
    }

    @Override public User getUser(int viaid) throws PersistenceException
    {
        throw new FakePersistenceException();
    }

    @Override public Room getRoom(String room, User activeUser) throws PersistenceException
    {
        throw new FakePersistenceException();
    }

    @Override public List<Room> getRooms(User activeUser) throws PersistenceException
    {
        throw new FakePersistenceException();
    }

    @Override public List<Booking> getBookingsForUser(User user, LocalDate startDate, LocalDate endDate, User activeUser) throws PersistenceException
    {
        throw new FakePersistenceException();
    }

    @Override public List<Booking> getBookingsForRoom(Room room, LocalDate startDate, LocalDate endDate, User activeUser) throws PersistenceException
    {
        throw new FakePersistenceException();
    }

    @Override public List<Booking> getBookingsForUserGroupUser(User user, LocalDate startDate, LocalDate endDate, User activeUser) throws PersistenceException
    {
        throw new FakePersistenceException();
    }

    @Override public void createBooking(User activeUser, Room room, BookingInterval bookingInterval, UserGroup userGroup) throws PersistenceException
    {
        throw new FakePersistenceException();
    }

    @Override public void deleteBooking(Booking booking) throws PersistenceException
    {
        throw new FakePersistenceException();
    }

    @Override public List<Room> getAvailableRooms(User activeUser, BookingInterval interval, Integer minCapacity, Integer maxCapacity, Character building, Integer floor) throws PersistenceException
    {
        throw new FakePersistenceException();
    }

    @Override public boolean createUser(String name, String initials, int viaid, String passwordHash, UserType type) throws PersistenceException
    {
        throw new FakePersistenceException();
    }

    @Override public List<UserGroup> getUserGroups() throws PersistenceException
    {
        throw new FakePersistenceException();
    }

    @Override public List<User> getUserGroupUsers(UserGroup userGroup) throws PersistenceException
    {
        throw new FakePersistenceException();
    }

    @Override public void updateRoom(Room room) throws PersistenceException
    {
        throw new FakePersistenceException();
    }

    @Override public void updateUserRoomData(User user, Room room, String comment, int color) throws PersistenceException
    {
        throw new FakePersistenceException();
    }

    @Override public List<TimeSlot> getTimeSlots() throws PersistenceException
    {
        throw new FakePersistenceException();
    }

    @Override public void createRoom(String name, RoomType type, int maxComf, int maxSafety, int size, String comment, boolean isDouble, String doubleName) throws PersistenceException
    {
        throw new FakePersistenceException();
    }

    @Override public void deleteRoom(Room room) throws PersistenceException
    {
        throw new FakePersistenceException();
    }
}
