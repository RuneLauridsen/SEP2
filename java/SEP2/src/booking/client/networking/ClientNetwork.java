package booking.client.networking;

import booking.shared.objects.*;
import booking.shared.GetAvailableRoomsParameters;

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

    public Room getRoom(String room, User activeUser)
        throws ClientNetworkException, ClientResponseException;

    public List<Booking> getBookingsForRoom(String roomName, LocalDate start, LocalDate end)
        throws ClientNetworkException, ClientResponseException;

    public Room getRoom(String roomName)
        throws ClientNetworkException, ClientResponseException;
    void createRoom(String name, RoomType type, int maxComf, int maxSafety, int size, String comment, boolean isDouble, String doubleName)
        throws ClientNetworkException, ClientResponseException;
}
