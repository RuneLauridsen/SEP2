package booking.server.model;

import booking.server.model.importFile.ImportFileResult;
import booking.shared.CreateBookingParameters;
import booking.shared.objects.*;
import booking.shared.GetAvailableRoomsParameters;

import java.time.LocalDate;
import java.util.List;

public interface ServerModel
{
    public User getUser(int viaid) throws ServerModelException;
    public Room getRoom(String roomName, User activeUser) throws ServerModelException;
    public void createUser(String username, String password, String initials, int viaid, UserType userType) throws ServerModelException;

    public User login(int viaid, String password) throws ServerModelException;

    public List<Room> getRooms(User activeUser) throws ServerModelException;

    public List<RoomType> getRoomTypes() throws ServerModelException;
    public List<UserType> getUserTypes() throws ServerModelException;

    public List<Room> getAvailableRooms(User activeUser, GetAvailableRoomsParameters parameters) throws ServerModelException;

    public List<Overlap> createBooking(User activeUser, CreateBookingParameters parameters) throws ServerModelException;
    public void deleteBooking(User activeUser, Booking booking) throws ServerModelException;

    // NOTE(rune): Tager både userName og activeUser som argument, da returnerede objekter
    // indeholder bruger-specifik data (Room.userComment og Room.userColor).
    // userName fortæller hvilken bruger der skal hentes bookings for, og activeUser,
    // fortæller hvilken bruger der skal hentes bruger-specifik data for.
    public List<Booking> getBookingsForUser(User activeUser, User user, LocalDate from, LocalDate to) throws ServerModelException;
    public List<Booking> getBookingsForRoom(User activeUser, String roomName, LocalDate from, LocalDate to) throws ServerModelException;

    public List<UserGroup> getUserGroups() throws ServerModelException;
    public List<User> getUserGroupUsers(UserGroup userGroup) throws ServerModelException;

    public void createRoom(User activeUser, String name, RoomType type, int maxComf, int maxSafety, int size, String comment, boolean isDouble, String doubleName) throws ServerModelException;
    public void updateRoom(User activeUser, Room room) throws ServerModelException;
    public void deleteRoom(User activeUser, Room room) throws ServerModelException;

    public void updateUserRoomData(User activeUser, Room room, String comment, int color) throws ServerModelException;

    public List<TimeSlot> getTimeSlots() throws ServerModelException;

    public ImportFileResult importFile(User activeUser, String fileContent) throws ServerModelException;
}
