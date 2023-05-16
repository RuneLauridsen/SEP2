package booking.client.networking;

import booking.shared.objects.User;
import booking.shared.GetAvailableRoomsParameters;
import booking.shared.objects.Booking;
import booking.shared.objects.BookingInterval;
import booking.shared.objects.Room;
import booking.shared.objects.UserGroup;

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

    public void createBooking(Room room, BookingInterval interval, boolean isOverlapAllowed, UserGroup userGroup)
        throws ClientNetworkException, ClientResponseException;

    public List<Booking> getBookingsForRoom(String roomName, LocalDate start, LocalDate end)
        throws ClientNetworkException, ClientResponseException;

    public List<Booking> getBookingsForUser(String userName, LocalDate start, LocalDate end)
        throws ClientNetworkException, ClientResponseException;

    public Room getRoom(String roomName)
        throws ClientNetworkException, ClientResponseException;

    public List<UserGroup> getUserGroups()
        throws ClientNetworkException, ClientResponseException;

    public List<User> getUserGroupUsers(UserGroup userGroup)
        throws ClientNetworkException, ClientResponseException;

    public void updateUserRoomData(Room room, String comment, Integer color)
        throws ClientNetworkException, ClientResponseException;
}
