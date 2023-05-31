package booking.client.model;

import booking.client.networking.ClientNetwork;
import booking.client.networking.ClientNetworkException;
import booking.client.networking.ClientNetworkResponseException;
import booking.server.model.importFile.ImportFileError;
import booking.server.model.importFile.ImportFileResult;
import booking.shared.CreateBookingParameters;
import booking.shared.GetAvailableRoomsParameters;
import booking.shared.NowProvider;
import booking.shared.objects.*;

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

    @Override public void deleteBooking(Booking booking) throws ClientModelException
    {
        try
        {
            networkLayer.deleteBooking(booking);
        }
        catch (ClientNetworkResponseException e)
        {
            throw wrapNetworkResponse(e);
        }
        catch (ClientNetworkException e)
        {
            throw wrapNetwork(e);
        }
    }

    public User getUser()
    {
        return user;
    }

    @Override public User login(int viaid, String password) throws ClientModelException
    {
        try
        {
            user = networkLayer.login(viaid, password);
            return user;
        }
        catch (ClientNetworkResponseException e)
        {
            throw wrapNetworkResponse(e);
        }
        catch (ClientNetworkException e)
        {
            throw wrapNetwork(e);
        }
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
            throw wrapNetworkResponse(e);
        }
        catch (ClientNetworkException e)
        {
            throw wrapNetwork(e);
        }
    }

    @Override public List<RoomType> getRoomTypes() throws ClientModelException
    {
        try
        {
            return networkLayer.getRoomTypes();
        }
        catch (ClientNetworkResponseException e)
        {
            throw wrapNetworkResponse(e);
        }
        catch (ClientNetworkException e)
        {
            throw wrapNetwork(e);
        }
    }

    @Override public List<UserType> getUserTypes() throws ClientModelException
    {
        try
        {
            return networkLayer.getUserTypes();
        }
        catch (ClientNetworkResponseException e)
        {
            throw wrapNetworkResponse(e);
        }
        catch (ClientNetworkException e)
        {
            throw wrapNetwork(e);
        }
    }

    @Override public List<Room> getAvailableRooms(GetAvailableRoomsParameters parameters) throws ClientModelException
    {
        try
        {
            return networkLayer.getAvailableRooms(parameters);
        }
        catch (ClientNetworkResponseException e)
        {
            throw wrapNetworkResponse(e);
        }
        catch (ClientNetworkException e)
        {
            throw wrapNetwork(e);
        }
    }

    @Override public List<Booking> getActiveBookings() throws ClientModelException
    {
        try
        {
            return networkLayer.getBookingsForUser(user, nowProvider.nowDate(), LocalDate.MAX);
        }
        catch (ClientNetworkResponseException e)
        {
            throw wrapNetworkResponse(e);
        }
        catch (ClientNetworkException e)
        {
            throw wrapNetwork(e);
        }
    }

    @Override public void createBooking(CreateBookingParameters parameters)
        throws ClientModelOverlapException, ClientModelException
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
            throw wrapNetworkResponse(e);
        }
        catch (ClientNetworkException e)
        {
            throw wrapNetwork(e);
        }
    }

    @Override public void createRoom(String name, RoomType type, int maxComf, int maxSafety, int size, String comment, boolean isDouble, String doubleName) throws ClientModelException
    {
        try
        {
            networkLayer.createRoom(name, type, maxComf, maxSafety, size, comment, isDouble, doubleName);
        }
        catch (ClientNetworkResponseException e)
        {
            throw wrapNetworkResponse(e);
        }
        catch (ClientNetworkException e)
        {
            throw wrapNetwork(e);
        }
    }

    @Override public Room getRoom(String room) throws ClientModelException
    {
        try
        {
            return networkLayer.getRoom(room);
        }
        catch (ClientNetworkResponseException e)
        {
            throw wrapNetworkResponse(e);
        }
        catch (ClientNetworkException e)
        {
            throw wrapNetwork(e);
        }
    }

    @Override public List<Room> getRooms() throws ClientModelException
    {
        try
        {
            return networkLayer.getRooms();
        }
        catch (ClientNetworkResponseException e)
        {
            throw wrapNetworkResponse(e);
        }
        catch (ClientNetworkException e)
        {
            throw wrapNetwork(e);
        }
    }

    @Override public List<Booking> getBookingsForRoom(String roomName, LocalDate start, LocalDate end) throws ClientModelException
    {
        try
        {
            return networkLayer.getBookingsForRoom(roomName, start, end);
        }
        catch (ClientNetworkResponseException e)
        {
            throw wrapNetworkResponse(e);
        }
        catch (ClientNetworkException e)
        {
            throw wrapNetwork(e);
        }
    }

    @Override public List<UserGroup> getUserGroups() throws ClientModelException
    {
        try
        {
            return networkLayer.getUserGroups();
        }
        catch (ClientNetworkResponseException e)
        {
            throw wrapNetworkResponse(e);
        }
        catch (ClientNetworkException e)
        {
            throw wrapNetwork(e);
        }
    }

    @Override public void updateRoom(Room room) throws ClientModelException
    {
        try
        {
            networkLayer.updateRoom(room);
            networkLayer.updateUserRoomData(room, room.getUserComment(), room.getUserColor());
        }
        catch (ClientNetworkResponseException e)
        {
            throw wrapNetworkResponse(e);
        }
        catch (ClientNetworkException e)
        {
            throw wrapNetwork(e);
        }
    }

    @Override public List<TimeSlot> getTimeSlots()
    {
        return List.of(
            new TimeSlot(LocalTime.of(8, 20), LocalTime.of(11, 50)),
            new TimeSlot(LocalTime.of(12, 45), LocalTime.of(16, 5))
        );
    }

    @Override public boolean isAvailable(Room room) throws ClientModelException
    {
        try
        {
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
            throw wrapNetworkResponse(e);
        }
        catch (ClientNetworkException e)
        {
            throw wrapNetwork(e);
        }
    }

    @Override public ImportFileResult importFile(String fileName) throws ClientModelException
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
            throw wrapNetworkResponse(e);
        }
        catch (ClientNetworkException e)
        {
            throw wrapNetwork(e);
        }
    }

    @Override public void deleteRoom(Room room) throws ClientModelException
    {
        try
        {
            networkLayer.deleteRoom(room);
        }
        catch (ClientNetworkResponseException e)
        {
            throw wrapNetworkResponse(e);
        }
        catch (ClientNetworkException e)
        {
            throw wrapNetwork(e);
        }
    }

    // NOTE: Separate exceptiontyper er ikke strengt tager nødvendigt, men
    // holder lagene adskilt. Det er også praktisk at kunne skelne mellem egentlige
    // netværksfejl, og et response med eksempelvis TOO_MANY_ACTIVE_BOOKINGS, hvis man
    // kun ville logge egentlige netværksfejl.

    private static ClientModelException wrapNetworkResponse(ClientNetworkResponseException e)
    {
        return new ClientModelException(e.getMessage(), e);
    }

    private static ClientModelException wrapNetwork(ClientNetworkException e)
    {
        return new ClientModelException("Netværksfejl " + e.getMessage(), e);
    }
}
