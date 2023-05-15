package booking.server.model;

import booking.core.Booking;
import booking.core.BookingInterval;
import booking.core.Room;
import booking.core.User;
import booking.database.Persistence;
import booking.shared.GetAvailableRoomsParameters;
import booking.shared.socketMessages.ErrorResponseReason;

import static booking.shared.socketMessages.ErrorResponseReason.*;

import java.time.LocalDate;
import java.util.List;

public class ServerModelImpl implements ServerModel
{
    private final Persistence persistence;

    public ServerModelImpl(Persistence persistence)
    {
        this.persistence = persistence;
    }

    @Override public User getUser(String username)
    {
        return persistence.getUser(username);
    }

    @Override public List<Room> getAvailableRooms(User activeUser, GetAvailableRoomsParameters parameters)
    {
        return persistence.getAvailableRooms(
            activeUser,
            parameters.getInterval(),
            parameters.getMinCapacity(),
            parameters.getMaxCapacity(),
            parameters.getBuilding(),
            parameters.getFloor()
        );
    }

    @Override public ErrorResponseReason createBooking(User user, Room room, BookingInterval interval)
    {
        // NOTE(rune): Vi stoler på at klienten sender Room med korrekt RoomType værdi. Dette er ikke en sikker måde
        // a styre adgang til lokaler på, da klienten nemt kunne sende en Room instans med modificeret RoomType,
        // for at få adgang til at booke lokaler, som klienten normalt ikke ville have adgang til.
        // SocketHandler'en holder selv styr på User objektet, så der kan klienten ikke snyde.

        boolean isAllowedToBookRoom = user.getType().getAllowedRoomTypes().contains(room.getType());
        if (isAllowedToBookRoom)
        {
            List<Booking> activeBookings = persistence.getBookingsForUser(
                user,
                LocalDate.now(), // TODO(rune): Lav om så det kan unit testes
                LocalDate.MAX,
                user
            );

            if (activeBookings.size() < user.getType().getMaxBookingCount())
            {
                persistence.createBooking(user, room, interval);
                return ERROR_RESPONSE_REASON_NONE;
            }
            else
            {
                return ERROR_RESPONSE_REASON_TOO_MANY_ACTIVE_BOOKINGS;
            }
        }
        else
        {
            return ERROR_RESPONSE_REASON_TOO_MANY_ACTIVE_BOOKINGS;
        }
    }

    @Override public ErrorResponseReason deleteBooking(User activeUser, Booking booking)
    {
        if (activeUser.getType().canEditBookings() || activeUser.equals(booking.getUser()))
        {
            persistence.deleteBooking(booking);
            return ERROR_RESPONSE_REASON_NONE;
        }
        else
        {
            return ERROR_RESPONSE_REASON_ACCESS_DENIED;
        }
    }

    @Override public List<Booking> getBookingsForUser(String userName, LocalDate from, LocalDate to, User activeUser)
    {
        // TODO(rune): Almindelige brugere må ikke se andre brugeres bookinger?

        User user = persistence.getUser(userName);
        if (user != null)
        {
            return persistence.getBookingsForUser(user, from, to, activeUser);
        }
        else
        {
            return List.of();
        }
    }

    @Override public List<Booking> getBookingsForRoom(String roomName, LocalDate from, LocalDate to, User activeUser)
    {
        Room room = persistence.getRoom(roomName, activeUser);
        if (room != null)
        {
            return persistence.getBookingsForRoom(room, from, to, activeUser);
        }
        else
        {
            return List.of();
        }
    }
}
