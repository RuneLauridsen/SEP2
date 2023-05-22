package booking.client.networking;

import booking.shared.CreateBookingParameters;
import booking.shared.objects.TimeSlot;
import booking.shared.objects.User;
import booking.shared.GetAvailableRoomsParameters;
import booking.shared.objects.Booking;
import booking.shared.objects.BookingInterval;
import booking.shared.objects.Room;
import booking.shared.objects.UserGroup;
import booking.shared.objects.*;

import java.time.LocalDate;
import java.util.List;

public interface ClientNetwork
{
    public User connect(int viaid, String password)
        throws ClientNetworkException, ClientResponseException;

    public void disconnect()
        throws ClientNetworkException, ClientResponseException;

    public List<Room> getAvailableRooms(GetAvailableRoomsParameters parameters)
        throws ClientNetworkException, ClientResponseException;

    public void createBooking(CreateBookingParameters parameters)
        throws ClientNetworkException, ClientResponseException;

    public void deleteBooking(Booking booking)
        throws ClientNetworkException, ClientResponseException;

    public List<Booking> getBookingsForRoom(String roomName, LocalDate start, LocalDate end)
        throws ClientNetworkException, ClientResponseException;

    public List<Booking> getBookingsForUser(User user, LocalDate start, LocalDate end)
        throws ClientNetworkException, ClientResponseException;

    public List<Room> getRooms()
        throws ClientNetworkException, ClientResponseException;

    public Room getRoom(String roomName)
        throws ClientNetworkException, ClientResponseException;

    public List<RoomType> getRoomTypes()
        throws ClientNetworkException, ClientResponseException;

    public List<UserType> getUserTypes()
        throws ClientNetworkException, ClientResponseException;

    public List<UserGroup> getUserGroups()
        throws ClientNetworkException, ClientResponseException;

    public List<User> getUserGroupUsers(UserGroup userGroup)
        throws ClientNetworkException, ClientResponseException;

    public void updateRoom(Room room)
        throws ClientNetworkException, ClientResponseException;

    public void updateUserRoomData(Room room, String comment, Integer color)
        throws ClientNetworkException, ClientResponseException;

    public List<TimeSlot> getTimeSlots()
        throws ClientNetworkException, ClientResponseException;

    public void createRoom(String name, RoomType type, int maxComf, int maxSafety, int size, String comment, boolean isDouble, String doubleName)
        throws ClientNetworkException, ClientResponseException;

    public void createUser(String username, String password, String initials, int viaid, UserType userType)
        throws ClientNetworkException, ClientResponseException;
}
