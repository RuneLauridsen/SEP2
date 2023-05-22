package booking.client.model;

import booking.shared.CreateBookingParameters;
import booking.shared.objects.Booking;
import booking.shared.objects.BookingInterval;
import booking.shared.objects.Room;
import booking.shared.objects.TimeSlot;
import booking.shared.objects.User;
import booking.shared.objects.*;
import booking.shared.GetAvailableRoomsParameters;
import booking.shared.objects.UserGroup;

import java.time.LocalDate;
import java.util.List;

public interface ClientModel
{
    public void login(int viaid, String password);
    public void logout();
    public void register(String username, String password, String initials, int viaid, UserType userType);

    public User getUser(); // NOTE(rune): Returns null if user is not logged in.

    public List<Room> getAvailableRooms(GetAvailableRoomsParameters parameters);
    public List<Booking> getActiveBookings();
    public void createBooking(CreateBookingParameters parameters);
    public void deleteBooking(Booking booking);
    public void createRoom(String name, RoomType type, int maxComf, int maxSafety, int size, String comment, boolean isDouble, String doubleName);
    public Room getRoom(String room);

    public List<Room> getRooms();
    public List<RoomType> getRoomTypes();
    public List<UserType> getUserTypes();

    public List<Booking> getBookingsForRoom(String roomName, LocalDate start, LocalDate end);
    public List<Booking> getBookingsForUser(User user, LocalDate start, LocalDate end);

    public List<UserGroup> getUserGroups();
    public List<User> getUserGroupUsers(UserGroup userGroup);

    public void updateRoom(Room room);
    public void updateUserRoomData(Room room, String comment, Integer color);

    public List<TimeSlot> getTimeSlots();

    public boolean isAvailable(Room room);
  public void deleteRoom(Room room);
}
