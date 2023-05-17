package booking.server.model;

import booking.shared.UpdateRoomParameters;
import booking.shared.objects.Booking;
import booking.shared.objects.BookingInterval;
import booking.shared.objects.Room;
import booking.shared.objects.RoomType;
import booking.shared.objects.TimeSlot;
import booking.shared.objects.User;
import booking.database.Persistence;
import booking.shared.GetAvailableRoomsParameters;
import booking.shared.objects.UserGroup;
import booking.shared.socketMessages.ErrorResponseReason;

import static booking.shared.socketMessages.ErrorResponseReason.*;

import java.time.LocalDate;
import java.util.ArrayList;
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

    @Override public Room getRoom(String roomName, User activeUser)
    {
        return persistence.getRoom(roomName, activeUser);
    }

    @Override public List<RoomType> getRoomTypes()
    {
        return new ArrayList<>(persistence.getRoomTypes().values());
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

    @Override public ErrorResponseReason createRoom(String name, RoomType type, int maxComf, int maxSafety, int size, String comment, boolean isDouble, String doubleName)
    {
        //TODO(julie) errorhandle stuff

        persistence.createRoom(name, type, maxComf, maxSafety, size, comment, isDouble, doubleName);
        return ERROR_RESPONSE_REASON_NONE;
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

    @Override public List<UserGroup> getUserGroups()
    {
        List<UserGroup> userGroups = persistence.getUserGroups();
        return userGroups;
    }

    @Override public List<User> getUserGroupUsers(UserGroup userGroup)
    {
        List<User> users = persistence.getUserGroupUsers(userGroup);
        return users;
    }

    @Override public ErrorResponseReason updateRoom(Room room, UpdateRoomParameters parameters, User activeUser)
    {
        if (activeUser.getType().canEditRooms())
        {
            persistence.updateRoom(room, parameters);
            return ERROR_RESPONSE_REASON_NONE;
        }
        else
        {
            return ERROR_RESPONSE_REASON_ACCESS_DENIED;
        }
    }

    @Override public void updateUserRoomData(User user, Room room, String comment, int color)
    {
        persistence.updateUserRoomData(user, room, comment, color);
    }

    @Override public List<TimeSlot> getTimeSlots()
    {
        return persistence.getTimeSlots();
    }
}
