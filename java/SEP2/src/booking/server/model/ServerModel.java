package booking.server.model;

import booking.core.Booking;
import booking.core.BookingInterval;
import booking.core.Room;
import booking.core.User;
import booking.shared.GetAvailableRoomsParameters;
import booking.shared.socketMessages.ErrorResponseReason;

import java.time.LocalDate;
import java.util.List;

public interface ServerModel
{
    // TODO(rune): Password checking
    public User getUser(String username);

    public List<Room> getAvailableRooms(User activeUser, GetAvailableRoomsParameters parameters);

    // NOTE(rune): Returnerer en fejlkode, hvis bruger ikke har adgang til at booke lokaler,
    // eller har for mange igangværende bookinger. Checker ikke efter overlap.
    public ErrorResponseReason createBooking(User activeUser, Room room, BookingInterval interval);

    // NOTE(rune): Returnerer en fejlkode, hvis bruger ikke har adgang til at slette bookinger.
    public ErrorResponseReason deleteBooking(User activeUser, Booking booking);

    // TODO(rune): Almindelige brugere må ikke se andre brugeres bookinger?
    public List<Booking> getBookingsForUser(String userName, LocalDate from, LocalDate to, User activeUser);

    public List<Booking> getBookingsForRoom(String roomName, LocalDate from, LocalDate to, User activeUser);
}
