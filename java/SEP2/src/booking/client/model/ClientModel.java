package booking.client.model;

import booking.server.model.importFile.ImportFileResult;
import booking.shared.CreateBookingParameters;
import booking.shared.objects.Booking;
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
    public void login(int viaid, String password) throws ClientModelException;
    public void logout() throws ClientModelException;
    public void register(String username, String password, String initials, int viaid, UserType userType) throws ClientModelException;

    // NOTE(rune): Returns null if user is not logged in.
    public User getUser();

    public List<Room> getAvailableRooms(GetAvailableRoomsParameters parameters) throws ClientModelException;
    public List<Booking> getActiveBookings() throws ClientModelException;
    public void createBooking(CreateBookingParameters parameters) throws ClientModelOverlapException, ClientModelException;
    public void deleteBooking(Booking booking) throws ClientModelException;
    public void createRoom(String name, RoomType type, int maxComf, int maxSafety, int size, String comment, boolean isDouble, String doubleName) throws ClientModelException;
    public Room getRoom(String room) throws ClientModelException;

    public List<Room> getRooms() throws ClientModelException;
    public List<RoomType> getRoomTypes() throws ClientModelException;
    public List<UserType> getUserTypes() throws ClientModelException;

    public List<Booking> getBookingsForRoom(String roomName, LocalDate start, LocalDate end) throws ClientModelException;
    public List<Booking> getBookingsForUser(User user, LocalDate start, LocalDate end) throws ClientModelException;

    public List<UserGroup> getUserGroups() throws ClientModelException;
    public List<User> getUserGroupUsers(UserGroup userGroup) throws ClientModelException;

    public void updateRoom(Room room) throws ClientModelException;
    public void updateUserRoomData(Room room, String comment, Integer color) throws ClientModelException;

    public List<TimeSlot> getTimeSlots() throws ClientModelException;

    public boolean isAvailable(Room room) throws ClientModelException;

    public ImportFileResult importFile(String fileName) throws ClientModelException;
    public void deleteRoom(Room room) throws ClientModelException;
}
