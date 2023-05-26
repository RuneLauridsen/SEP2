package booking.client.model;

import booking.server.model.importFile.ImportFileResult;
import booking.shared.CreateBookingParameters;
import booking.shared.GetAvailableRoomsParameters;
import booking.shared.objects.*;

import java.time.LocalDate;
import java.util.List;

public interface ClientModel extends
    ClientModelActiveBookings,
    ClientModelActiveUser,
    ClientModelUserBooking,
    ClientModelCoordinatorBooking,
    ClientModelImport,
    ClientModelLogin,
    ClientModelRegister,
    ClientModelRoomInfo,
    ClientModelRoomMangement
{
    // NOTE(rune): Det er ikke nogen grund til at skrive alle metoderne her, da vi extender de segregerede interfaces
    // men det giver et godt overblik.

    public User login(int viaid, String password) throws ClientModelException;
    public void register(String username, String password, String initials, int viaid, UserType userType) throws ClientModelException;
    public User getUser();

    public List<Room> getAvailableRooms(GetAvailableRoomsParameters parameters) throws ClientModelException;
    public List<Booking> getActiveBookings() throws ClientModelException;

    public void createBooking(CreateBookingParameters parameters) throws ClientModelOverlapException, ClientModelException;
    public void deleteBooking(Booking booking) throws ClientModelException;

    public Room getRoom(String room) throws ClientModelException;
    public List<Room> getRooms() throws ClientModelException;
    public List<RoomType> getRoomTypes() throws ClientModelException;
    public List<UserType> getUserTypes() throws ClientModelException;

    public List<Booking> getBookingsForRoom(String roomName, LocalDate start, LocalDate end) throws ClientModelException;
    public List<UserGroup> getUserGroups() throws ClientModelException;

    public void createRoom(String name, RoomType type, int maxComf, int maxSafety, int size, String comment, boolean isDouble, String doubleName) throws ClientModelException;
    public void updateRoom(Room room) throws ClientModelException;
    public void deleteRoom(Room room) throws ClientModelException;

    public List<TimeSlot> getTimeSlots() throws ClientModelException;

    public boolean isAvailable(Room room) throws ClientModelException;

    public ImportFileResult importFile(String fileName) throws ClientModelException;
}
