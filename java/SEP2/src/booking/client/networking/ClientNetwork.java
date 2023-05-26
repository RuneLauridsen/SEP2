package booking.client.networking;

import booking.server.model.importFile.ImportFileResult;
import booking.shared.CreateBookingParameters;
import booking.shared.GetAvailableRoomsParameters;
import booking.shared.objects.*;

import java.time.LocalDate;
import java.util.List;

public interface ClientNetwork
{
    public void connect() throws ClientNetworkException;

    public User login(int viaid, String password)
        throws ClientNetworkException, ClientNetworkResponseException;

    public void logout()
        throws ClientNetworkException, ClientNetworkResponseException;

    public List<Room> getAvailableRooms(GetAvailableRoomsParameters parameters)
        throws ClientNetworkException, ClientNetworkResponseException;

    public List<Overlap> createBooking(CreateBookingParameters parameters)
        throws ClientNetworkException, ClientNetworkResponseException;

    public void deleteBooking(Booking booking)
        throws ClientNetworkException, ClientNetworkResponseException;

    public List<Booking> getBookingsForRoom(String roomName, LocalDate start, LocalDate end)
        throws ClientNetworkException, ClientNetworkResponseException;

    public List<Booking> getBookingsForUser(User user, LocalDate start, LocalDate end)
        throws ClientNetworkException, ClientNetworkResponseException;

    public List<Room> getRooms()
        throws ClientNetworkException, ClientNetworkResponseException;

    public Room getRoom(String roomName)
        throws ClientNetworkException, ClientNetworkResponseException;

    public List<RoomType> getRoomTypes()
        throws ClientNetworkException, ClientNetworkResponseException;

    public List<UserType> getUserTypes()
        throws ClientNetworkException, ClientNetworkResponseException;

    public List<UserGroup> getUserGroups()
        throws ClientNetworkException, ClientNetworkResponseException;

    public List<User> getUserGroupUsers(UserGroup userGroup)
        throws ClientNetworkException, ClientNetworkResponseException;

    public void updateRoom(Room room)
        throws ClientNetworkException, ClientNetworkResponseException;

    public void updateUserRoomData(Room room, String comment, Integer color)
        throws ClientNetworkException, ClientNetworkResponseException;

    public List<TimeSlot> getTimeSlots()
        throws ClientNetworkException, ClientNetworkResponseException;

    public void createRoom(String name, RoomType type, int maxComf, int maxSafety, int size, String comment, boolean isDouble, String doubleName)
        throws ClientNetworkException, ClientNetworkResponseException;

    public void createUser(String username, String password, String initials, int viaid, UserType userType)
        throws ClientNetworkException, ClientNetworkResponseException;

    public ImportFileResult importFile(String fileContent)
        throws ClientNetworkException, ClientNetworkResponseException;

    void deleteRoom(Room room)
        throws ClientNetworkException, ClientNetworkResponseException;
}
