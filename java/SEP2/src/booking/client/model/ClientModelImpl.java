package booking.client.model;

import booking.client.networking.ClientNetwork;
import booking.client.networking.ClientNetworkException;
import booking.client.networking.ClientResponseException;
import booking.shared.CreateBookingParameters;
import booking.shared.objects.Booking;
import booking.shared.objects.BookingInterval;
import booking.shared.objects.Room;
import booking.shared.objects.TimeSlot;
import booking.shared.objects.User;
import booking.shared.objects.*;
import booking.shared.GetAvailableRoomsParameters;
import booking.shared.objects.UserGroup;
import booking.shared.socketMessages.CreateUserRequest;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class ClientModelImpl implements ClientModel
{
    private final ClientNetwork networkLayer;
    private User user;

    public ClientModelImpl(ClientNetwork networkLayer)
    {
        this.networkLayer = networkLayer;
    }

    @Override public void deleteBooking(Booking booking)
    {
        try
        {
            networkLayer.deleteBooking(booking);
        }
        catch (ClientResponseException e)
        {
            throw new RuntimeException(e); // TODO(rune): Bedre error handling
        }
        catch (ClientNetworkException e)
        {
            throw new RuntimeException(e); // TODO(rune): Bedre error handling
        }
    }

    public User getUser()
    {
        return user;
    }

    @Override public void login(String username, String password)
    {
        try
        {
            user = networkLayer.connect(username, password);
        }
        catch (ClientResponseException e)
        {
            throw new RuntimeException(e); // TODO(rune): Bedre error handling
        }
        catch (ClientNetworkException e)
        {
            throw new RuntimeException(e); // TODO(rune): Bedre error handling
        }
    }

    @Override public void logout()
    {

    }

    @Override public void register(String username, String password, String initials, int viaid, UserType userType)
    {
        try
        {
            networkLayer.createUser(username, password, initials, viaid, userType);
            login(username, password);
        }
        catch (ClientResponseException e)
        {
            throw new RuntimeException(e); // TODO(rune): Bedre error handling
        }
        catch (ClientNetworkException e)
        {
            throw new RuntimeException(e); // TODO(rune): Bedre error handling
        }
    }

    @Override public List<RoomType> getRoomTypes()
    {
        try
        {
            return networkLayer.getRoomTypes();
        }
        catch (ClientResponseException e)
        {
            throw new RuntimeException(e); // TODO(rune): Bedre error handling
        }
        catch (ClientNetworkException e)
        {
            throw new RuntimeException(e); // TODO(rune): Bedre error handling
        }
    }

    @Override public List<UserType> getUserTypes()
    {
        try
        {
            return networkLayer.getUserTypes();
        }
        catch (ClientResponseException e)
        {
            throw new RuntimeException(e); // TODO(rune): Bedre error handling
        }
        catch (ClientNetworkException e)
        {
            throw new RuntimeException(e); // TODO(rune): Bedre error handling
        }
    }

    @Override public List<Room> getAvailableRooms(GetAvailableRoomsParameters parameters)
    {
        try
        {
            return networkLayer.getAvailableRooms(parameters);
        }
        catch (ClientResponseException e)
        {
            throw new RuntimeException(e); // TODO(rune): Bedre error handling
        }
        catch (ClientNetworkException e)
        {
            throw new RuntimeException(e); // TODO(rune): Bedre error handling
        }
    }

    @Override public List<Booking> getActiveBookings(LocalDate start, LocalDate end)
    {
        try
        {
            return networkLayer.getBookingsForUser(user.getName(), start, end);
        }
        catch (ClientResponseException e)
        {
            throw new RuntimeException(e); // TODO(rune): Bedre error handling
        }
        catch (ClientNetworkException e)
        {
            throw new RuntimeException(e); // TODO(rune): Bedre error handling
        }
    }

    @Override public void createBooking(CreateBookingParameters parameters)
    {
        try
        {
            networkLayer.createBooking(parameters);
        }
        catch (ClientResponseException e)
        {
            throw new RuntimeException(e); // TODO(rune): Bedre error handling
        }
        catch (ClientNetworkException e)
        {
            throw new RuntimeException(e); // TODO(rune): Bedre error handling
        }
    }

    @Override public void createRoom(String name, RoomType type, int maxComf, int maxSafety, int size, String comment, boolean isDouble, String doubleName)
    {
        try
        {
            networkLayer.createRoom(name, type, maxComf, maxSafety, size, comment, isDouble, doubleName);
        }
        catch (ClientResponseException e)
        {
            throw new RuntimeException(e); // TODO(rune): Bedre error handling
        }
        catch (ClientNetworkException e)
        {
            throw new RuntimeException(e); // TODO(rune): Bedre error handling
        }
    }

    @Override public Room getRoom(String room)
    {
        try
        {
            return networkLayer.getRoom(room);
        }
        catch (ClientResponseException e)
        {
            throw new RuntimeException(e); // TODO(rune): Bedre error handling
        }
        catch (ClientNetworkException e)
        {
            throw new RuntimeException(e); // TODO(rune): Bedre error handling
        }
    }

    @Override public List<Room> getRooms()
    {
        try
        {
            return networkLayer.getRooms();
        }
        catch (ClientResponseException e)
        {
            throw new RuntimeException(e); // TODO(rune): Bedre error handling
        }
        catch (ClientNetworkException e)
        {
            throw new RuntimeException(e); // TODO(rune): Bedre error handling
        }
    }

    @Override public List<Booking> getBookingsForRoom(String roomName, LocalDate start, LocalDate end)
    {
        try
        {
            return networkLayer.getBookingsForRoom(roomName, start, end);
        }
        catch (ClientResponseException e)
        {
            throw new RuntimeException(e); // TODO(rune): Bedre error handling
        }
        catch (ClientNetworkException e)
        {
            throw new RuntimeException(e); // TODO(rune): Bedre error handling
        }
    }

    @Override public List<Booking> getBookingsForUser(String userName, LocalDate start, LocalDate end)
    {
        try
        {
            return networkLayer.getBookingsForUser(userName, start, end);
        }
        catch (ClientResponseException e)
        {
            throw new RuntimeException(e); // TODO(rune): Bedre error handling
        }
        catch (ClientNetworkException e)
        {
            throw new RuntimeException(e); // TODO(rune): Bedre error handling
        }
    }

    @Override public List<UserGroup> getUserGroups()
    {
        try
        {
            return networkLayer.getUserGroups();
        }
        catch (ClientResponseException e)
        {
            throw new RuntimeException(e); // TODO(rune): Bedre error handling
        }
        catch (ClientNetworkException e)
        {
            throw new RuntimeException(e); // TODO(rune): Bedre error handling
        }
    }

    @Override public List<User> getUserGroupUsers(UserGroup userGroup)
    {
        try
        {
            return networkLayer.getUserGroupUsers(userGroup);
        }
        catch (ClientResponseException e)
        {
            throw new RuntimeException(e); // TODO(rune): Bedre error handling
        }
        catch (ClientNetworkException e)
        {
            throw new RuntimeException(e); // TODO(rune): Bedre error handling
        }
    }

    @Override public void updateRoom(Room room)
    {
        try
        {
            // TODO(rune): Slå sammen til en request?
            networkLayer.updateRoom(room);
            networkLayer.updateUserRoomData(room, room.getName(), room.getUserColor());
        }
        catch (ClientResponseException e)
        {
            throw new RuntimeException(e);
        }
        catch (ClientNetworkException e)
        {
            throw new RuntimeException(e);
        }
    }

    @Override public void updateUserRoomData(Room room, String comment, Integer color)
    {
        try
        {
            networkLayer.updateUserRoomData(room, comment, color);
        }
        catch (ClientResponseException e)
        {
            throw new RuntimeException(e); // TODO(rune): Bedre error handling
        }
        catch (ClientNetworkException e)
        {
            throw new RuntimeException(e); // TODO(rune): Bedre error handling
        }
    }

    @Override public List<TimeSlot> getTimeSlots()
    {
        try
        {
            return networkLayer.getTimeSlots();
        }
        catch (ClientResponseException e)
        {
            throw new RuntimeException(e); // TODO(rune): Bedre error handling
        }
        catch (ClientNetworkException e)
        {
            throw new RuntimeException(e); // TODO(rune): Bedre error handling
        }
    }

    @Override public boolean isAvailable(Room room)
    {
        try
        {
            // TODO(rune): Kan vi gøre det smartere end at hente alle bookinger ned hver gang?
            // Er det nødvendigt at lave smartere? Hvis vi laver en kalender er den en god idé
            // at hente alle dagens bookinger ned fra serveren.

            LocalTime now = LocalTime.now();
            List<Booking> bookingsToday = networkLayer.getBookingsForRoom(room.getName(), LocalDate.now(), LocalDate.now());
            for (Booking bookingToday : bookingsToday)
            {
                if (bookingToday.getInterval().isOverlapWith(now))
                {
                    return false;
                }
            }

            return true;
        }
        catch (ClientResponseException e)
        {
            throw new RuntimeException(e); // TODO(rune): Bedre error handling
        }
        catch (ClientNetworkException e)
        {
            throw new RuntimeException(e); // TODO(rune): Bedre error handling
        }
    }
}
