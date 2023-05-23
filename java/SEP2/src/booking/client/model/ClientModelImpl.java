package booking.client.model;

import booking.server.model.importFile.ImportFileError;
import booking.server.model.importFile.ImportFileResult;
import booking.client.networking.ClientNetwork;
import booking.client.networking.ClientNetworkException;
import booking.client.networking.ClientNetworkResponseException;
import booking.shared.CreateBookingParameters;
import booking.shared.NowProvider;
import booking.shared.objects.Booking;
import booking.shared.objects.Room;
import booking.shared.objects.TimeSlot;
import booking.shared.objects.User;
import booking.shared.objects.*;
import booking.shared.GetAvailableRoomsParameters;
import booking.shared.objects.UserGroup;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class ClientModelImpl implements ClientModel
{
    private final ClientNetwork networkLayer;
    private final NowProvider nowProvider;
    private final FileIO fileIO;
    private User user;

    public ClientModelImpl(ClientNetwork networkLayer, NowProvider nowProvider, FileIO fileIO)
    {
        this.networkLayer = networkLayer;
        this.nowProvider = nowProvider;
        this.fileIO = fileIO;
    }

    @Override public void deleteBooking(Booking booking)
    {
        try
        {
            networkLayer.deleteBooking(booking);
        }
        catch (ClientNetworkResponseException e)
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

    @Override public void login(int viaid, String password) throws ClientModelException
    {
        try
        {
            user = networkLayer.login(viaid, password);
        }
        catch (ClientNetworkResponseException e)
        {
            throw new ClientModelException(e.getMessage(), e);
        }
        catch (ClientNetworkException e)
        {
            throw new RuntimeException(e); // TODO(rune): Bedre error handling
        }
    }

    @Override public void logout()
    {

    }

    @Override public void register(String username, String password, String initials, int viaid, UserType userType) throws ClientModelException
    {
        try
        {
            networkLayer.createUser(username, password, initials, viaid, userType);
            login(viaid, password);
        }
        catch (ClientNetworkResponseException e)
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
        catch (ClientNetworkResponseException e)
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
        catch (ClientNetworkResponseException e)
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
        catch (ClientNetworkResponseException e)
        {
            throw new RuntimeException(e); // TODO(rune): Bedre error handling
        }
        catch (ClientNetworkException e)
        {
            throw new RuntimeException(e); // TODO(rune): Bedre error handling
        }
    }

    @Override public List<Booking> getActiveBookings()
    {
        try
        {
            return networkLayer.getBookingsForUser(user, nowProvider.nowDate(), LocalDate.MAX);
        }
        catch (ClientNetworkResponseException e)
        {
            throw new RuntimeException(e); // TODO(rune): Bedre error handling
        }
        catch (ClientNetworkException e)
        {
            throw new RuntimeException(e); // TODO(rune): Bedre error handling
        }
    }

    @Override public void createBooking(CreateBookingParameters parameters)
        throws ClientModelOverlapException
    {
        try
        {
            List<Overlap> overlaps = networkLayer.createBooking(parameters);
            if (overlaps.size() > 0)
            {
                throw new ClientModelOverlapException(overlaps);
            }
        }
        catch (ClientNetworkResponseException e)
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
        catch (ClientNetworkResponseException e)
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
        catch (ClientNetworkResponseException e)
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
        catch (ClientNetworkResponseException e)
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
        catch (ClientNetworkResponseException e)
        {
            throw new RuntimeException(e); // TODO(rune): Bedre error handling
        }
        catch (ClientNetworkException e)
        {
            throw new RuntimeException(e); // TODO(rune): Bedre error handling
        }
    }

    @Override public List<Booking> getBookingsForUser(User user, LocalDate start, LocalDate end)
    {
        try
        {
            return networkLayer.getBookingsForUser(user, start, end);
        }
        catch (ClientNetworkResponseException e)
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
        catch (ClientNetworkResponseException e)
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
        catch (ClientNetworkResponseException e)
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
        catch (ClientNetworkResponseException e)
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
        catch (ClientNetworkResponseException e)
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
        catch (ClientNetworkResponseException e)
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

            LocalTime nowTime = nowProvider.nowTime();
            LocalDate nowDate = nowProvider.nowDate();

            List<Booking> bookingsToday = networkLayer.getBookingsForRoom(room.getName(), nowDate, nowDate);
            for (Booking bookingToday : bookingsToday)
            {
                if (bookingToday.getInterval().isOverlapWith(nowTime))
                {
                    return false;
                }
            }

            return true;
        }
        catch (ClientNetworkResponseException e)
        {
            throw new RuntimeException(e); // TODO(rune): Bedre error handling
        }
        catch (ClientNetworkException e)
        {
            throw new RuntimeException(e); // TODO(rune): Bedre error handling
        }
    }

    @Override public ImportFileResult importFile(String fileName)
    {
        String fileContent = null;

        try
        {
            fileContent = fileIO.readFile(fileName);
        }
        catch (IOException e)
        {
            ImportFileResult result = new ImportFileResult();
            result.addError(new ImportFileError(e.getMessage()));
            return result;
        }

        try
        {
            return networkLayer.importFile(fileContent);
        }
        catch (ClientNetworkResponseException e)
        {
            throw new RuntimeException(e); // TODO(rune): Bedre error handling
        }
        catch (ClientNetworkException e)
        {
            throw new RuntimeException(e); // TODO(rune): Bedre error handling
        }
    }

    @Override public void deleteRoom(Room room)
    {
        try
        {
            networkLayer.deleteRoom(room);
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
