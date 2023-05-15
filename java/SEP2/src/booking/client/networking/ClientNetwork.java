package booking.client.networking;

import booking.shared.objects.User;
import booking.shared.GetAvailableRoomsParameters;
import booking.shared.objects.Booking;
import booking.shared.objects.BookingInterval;
import booking.shared.objects.Room;

import java.time.LocalDate;
import java.util.List;

public interface ClientNetwork
{
    public User connect(String username, String password)
        throws ClientNetworkException, ClientResponseException;

    public void disconnect()
        throws ClientNetworkException, ClientResponseException;

    public List<Room> getAvailableRooms(GetAvailableRoomsParameters parameters)
        throws ClientNetworkException, ClientResponseException;

    public List<Booking> getActiveBookings(LocalDate start, LocalDate end)
        throws ClientNetworkException, ClientResponseException;

    public void createBooking(Room room, BookingInterval interval)
        throws ClientNetworkException, ClientResponseException;
    public void getRoom(String room, User activeUser)
        throws ClientNetworkException, ClientResponseException;

    public List<Booking> getBookingsForRoom(String roomName, LocalDate start, LocalDate end)
        throws ClientNetworkException, ClientResponseException;
}
