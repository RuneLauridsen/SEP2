package booking.server.model;

import booking.shared.objects.Booking;
import booking.shared.objects.BookingInterval;
import booking.shared.objects.Room;
import booking.shared.objects.User;
import booking.shared.GetAvailableRoomsParameters;
import booking.shared.objects.UserGroup;
import booking.shared.socketMessages.ErrorResponseReason;

import java.time.LocalDate;
import java.util.List;

public interface ServerModel
{
    // TODO(rune): Password checking
    public User getUser(String username);

    public Room getRoom(String roomName, User activeUser);

    public List<Room> getAvailableRooms(User activeUser, GetAvailableRoomsParameters parameters);

    // NOTE(rune): Returnerer en fejlkode, hvis bruger ikke har adgang til at booke lokaler,
    // eller har for mange igangværende bookinger. Checker ikke efter overlap.
    public ErrorResponseReason createBooking(User activeUser, Room room, BookingInterval interval);

    // NOTE(rune): Returnerer en fejlkode, hvis bruger ikke har adgang til at slette bookinger.
    public ErrorResponseReason deleteBooking(User activeUser, Booking booking);

    // TODO(rune): Almindelige brugere må ikke se andre brugeres bookinger?
    // NOTE(rune): Tager både userName og activeUser som argument, da returnerede objekter
    // indeholder bruger-specifik data (Room.userComment og user_room_data.userColor).
    // userName fortæller hvilken bruger der skal hentes bookings for, og activeUser,
    // fortæller hvilken bruger der skal hentes bruger-specifik data for.
    public List<Booking> getBookingsForUser(String userName, LocalDate from, LocalDate to, User activeUser);

    public List<Booking> getBookingsForRoom(String roomName, LocalDate from, LocalDate to, User activeUser);

    public List<UserGroup> getUserGroups();

    public List<User> getUserGroupUsers(UserGroup userGroup);

    public void updateUserRoomData(User user, Room room, String comment, int color);
}
