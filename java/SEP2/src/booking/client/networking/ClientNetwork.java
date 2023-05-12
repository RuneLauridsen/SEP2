package booking.client.networking;

import booking.core.User;
import booking.shared.GetAvailableRoomsParameters;
import booking.core.Booking;
import booking.core.BookingInterval;
import booking.core.Room;

import java.util.List;

public interface ClientNetwork
{
    public User connect(String username, String password)
        throws ClientNetworkException;

    public void disconnect()
        throws ClientNetworkException;

    public List<Room> getAvailableRooms(GetAvailableRoomsParameters parameters)
        throws ClientNetworkException;

    public List<Booking> getActiveBookings()
        throws ClientNetworkException;

    public void createBooking(Room room, BookingInterval interval)
        throws ClientNetworkException;
}
