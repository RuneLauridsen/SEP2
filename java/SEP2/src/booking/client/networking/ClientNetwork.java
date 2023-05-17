package booking.client.networking;

import booking.shared.UpdateRoomParameters;
import booking.shared.objects.TimeSlot;
import booking.shared.objects.User;
import booking.shared.GetAvailableRoomsParameters;
import booking.shared.objects.Booking;
import booking.shared.objects.BookingInterval;
import booking.shared.objects.Room;
import booking.shared.objects.UserGroup;
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

    public void createBooking(Room room, BookingInterval interval, boolean isOverlapAllowed, UserGroup userGroup)
        throws ClientNetworkException, ClientResponseException;

    public void deleteBooking(Booking booking)
        throws ClientNetworkException, ClientResponseException;

    public List<Booking> getBookingsForRoom(String roomName, LocalDate start, LocalDate end)
        throws ClientNetworkException, ClientResponseException;

    public List<Booking> getBookingsForUser(String userName, LocalDate start, LocalDate end)
        throws ClientNetworkException, ClientResponseException;

    public List<Room> getRooms()
        throws ClientNetworkException, ClientResponseException;

    public Room getRoom(String roomName)
        throws ClientNetworkException, ClientResponseException;

    public List<RoomType> getRoomTypes()
        throws ClientNetworkException, ClientResponseException;

    public List<UserGroup> getUserGroups()
        throws ClientNetworkException, ClientResponseException;

    public List<User> getUserGroupUsers(UserGroup userGroup)
        throws ClientNetworkException, ClientResponseException;

    public void updateRoom(Room room, UpdateRoomParameters parameters)
        throws ClientNetworkException, ClientResponseException;

    public void updateUserRoomData(Room room, String comment, Integer color)
        throws ClientNetworkException, ClientResponseException;

    public List<TimeSlot> getTimeSlots()
        throws ClientNetworkException, ClientResponseException;

    void createRoom(String name, RoomType type, int maxComf, int maxSafety, int size, String comment, boolean isDouble, String doubleName)
        throws ClientNetworkException, ClientResponseException;
}
