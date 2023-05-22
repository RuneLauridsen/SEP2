package booking.server.model;

import booking.shared.CreateBookingParameters;
import booking.shared.objects.*;
import booking.shared.GetAvailableRoomsParameters;
import booking.shared.socketMessages.ErrorResponseReason;

import java.time.LocalDate;
import java.util.List;

public interface ServerModel
{
    public User getUser(int viaid);
    public Room getRoom(String roomName, User activeUser);

    public User login(int viaid, String password);

    public List<Room> getRooms(User activeUser);

    public List<RoomType> getRoomTypes();
    public List<UserType> getUserTypes();

    public List<Room> getAvailableRooms(User activeUser, GetAvailableRoomsParameters parameters);

    // NOTE(rune): Returnerer en fejlkode, hvis bruger ikke har adgang til at booke lokaler,
    // eller har for mange igangværende bookinger. Checker ikke efter overlap.
    public ErrorResponseReason createBooking(User activeUser, CreateBookingParameters parameters, List<Overlap> overlaps);

    // NOTE(rune): Returnerer en fejlkode, hvis bruger ikke har adgang til at slette bookinger.
    public ErrorResponseReason deleteBooking(User activeUser, Booking booking);

    // TODO(rune): Almindelige brugere må ikke se andre brugeres bookinger?
    // NOTE(rune): Tager både userName og activeUser som argument, da returnerede objekter
    // indeholder bruger-specifik data (Room.userComment og Room.userColor).
    // userName fortæller hvilken bruger der skal hentes bookings for, og activeUser,
    // fortæller hvilken bruger der skal hentes bruger-specifik data for.
    public List<Booking> getBookingsForUser(User user, LocalDate from, LocalDate to, User activeUser);

    public List<Booking> getBookingsForRoom(String roomName, LocalDate from, LocalDate to, User activeUser);
    public List<UserGroup> getUserGroups();

    public List<User> getUserGroupUsers(UserGroup userGroup);

    public ErrorResponseReason createRoom(String name, RoomType type, int maxComf, int maxSafety, int size, String comment, boolean isDouble, String doubleName);
    public ErrorResponseReason updateRoom(Room room, User activeUser);
    public void updateUserRoomData(User user, Room room, String comment, int color);

    public ErrorResponseReason createUser(String username, String password, String initials, int viaid, UserType userType);

    public List<TimeSlot> getTimeSlots();

}
