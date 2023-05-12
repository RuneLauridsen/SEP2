package booking.client.logic;

import booking.core.Booking;
import booking.core.BookingInterval;
import booking.core.Room;
import booking.core.User;
import booking.shared.GetAvailableRoomsParameters;

import java.util.List;

public interface ClientModel
{
    public void login(String username, String password);
    public void logout();
    public User getUser(); // NOTE(rune): Returns null if user is not logged in.

    public List<Room> getAvailableRooms(GetAvailableRoomsParameters parameters);
    public List<Booking> getActiveBookings();
    public void createBooking(Room room, BookingInterval interval);
}
