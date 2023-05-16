package booking.client.model;

import booking.shared.objects.*;
import booking.shared.GetAvailableRoomsParameters;

import java.time.LocalDate;
import java.util.List;

public interface ClientModel
{
    public void login(String username, String password);
    public void logout();
    public User getUser(); // NOTE(rune): Returns null if user is not logged in.

    public List<Room> getAvailableRooms(GetAvailableRoomsParameters parameters);
    public List<Booking> getActiveBookings(LocalDate start, LocalDate end);
    public void createBooking(Room room, BookingInterval interval);
    public void createRoom(String name, RoomType type, int maxComf, int maxSafety, int size, String comment, boolean isDouble, String doubleName);
    public Room getRoom(String room, User activeUser);

    public List<Booking> getBookingsForRoom(String roomName, LocalDate start, LocalDate end);
    // TODO(rune): getBookingsForUser
    public boolean isAvailable(Room room);
}
