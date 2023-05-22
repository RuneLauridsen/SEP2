package booking.client.networking;

import booking.shared.CreateBookingParameters;
import booking.shared.objects.*;
import booking.shared.GetAvailableRoomsParameters;
import booking.shared.socketMessages.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.time.LocalDate;
import java.util.List;

public class ClientNetworkSocket implements ClientNetwork
{
    private Socket socket;
    private ObjectOutputStream outToServer;
    private ObjectInputStream inFromServer;

    @Override public User connect(int viaid, String password)
        throws ClientNetworkException, ClientResponseException
    {
        try
        {
            Socket socket = new Socket("localhost", 2910);
            outToServer = new ObjectOutputStream(socket.getOutputStream());
            inFromServer = new ObjectInputStream(socket.getInputStream());

            sendRequest(new ConnectionRequest(viaid, password));
            ConnectionResponse connectionResponse = (ConnectionResponse) readResponse();
            return connectionResponse.getUser();
        }
        catch (IOException e)
        {
            throw new RuntimeException(e); // TODO(rune): Bedre error handling
        }
    }

    @Override public void disconnect()
        throws ClientNetworkException, ClientResponseException
    {
        // TODO(rune): Disconnect
    }

    public void createUser(String username, String password, String initials, int viaid, UserType userType)
        throws ClientNetworkException, ClientResponseException
    {
        CreateUserRequest createUserRequest = new CreateUserRequest(
            username,
            password,
            initials,
            viaid,
            userType
        );

        sendRequest(createUserRequest);

        CreateUserResponse response = (CreateUserResponse) readResponse();
    }

    @Override public void deleteRoom(Room room)
        throws ClientNetworkException, ClientResponseException
    {
        sendRequest(new DeleteRoomRequest(room));
        DeleteRoomResponse response = (DeleteRoomResponse) readResponse();
    }

    @Override public List<Room> getAvailableRooms(GetAvailableRoomsParameters parameters)
        throws ClientNetworkException, ClientResponseException
    {
        sendRequest(new AvailableRoomsRequest(parameters));
        AvailableRoomsResponse response = (AvailableRoomsResponse) readResponse();
        return response.getRooms();
    }

    @Override public void createBooking(CreateBookingParameters parameters)
        throws ClientNetworkException, ClientResponseException
    {
        sendRequest(new CreateBookingRequest(parameters));
        CreateBookingResponse response = (CreateBookingResponse) readResponse();
    }

    @Override public void deleteBooking(Booking booking)
        throws ClientNetworkException, ClientResponseException
    {
        sendRequest(new DeleteBookingRequest(booking));
        DeleteBookingResponse response = (DeleteBookingResponse) readResponse();
    }

    @Override public Room getRoom(String roomName)
        throws ClientNetworkException, ClientResponseException
    {
        sendRequest(new RoomRequest(roomName));
        RoomResponse response = (RoomResponse) readResponse();
        return response.getRoom();
    }

    @Override public List<RoomType> getRoomTypes()
        throws ClientNetworkException, ClientResponseException
    {
        sendRequest(new RoomTypesRequest());
        RoomTypesResponse response = (RoomTypesResponse) readResponse();
        return response.getRoomTypes();
    }

    @Override public List<UserType> getUserTypes()
        throws ClientNetworkException, ClientResponseException
    {
        sendRequest(new UserTypesRequest());
        UserTypesResponse response = (UserTypesResponse) readResponse();
        return response.getUserTypes();
    }

    public List<Booking> getBookingsForRoom(String roomName, LocalDate start, LocalDate end)
        throws ClientNetworkException, ClientResponseException
    {
        sendRequest(new BookingsForRoomRequest(roomName, start, end));
        BookingsForRoomResponse response = (BookingsForRoomResponse) readResponse();
        return response.getBookings();
    }

    public List<Booking> getBookingsForUser(User user, LocalDate start, LocalDate end)
        throws ClientNetworkException, ClientResponseException
    {
        sendRequest(new BookingsForUserRequest(user, start, end));
        BookingsForUserResponse response = (BookingsForUserResponse) readResponse();
        return response.getBookings();
    }

    @Override public List<Room> getRooms()
        throws ClientNetworkException, ClientResponseException
    {
        sendRequest(new RoomsRequest());
        RoomsResponse response = (RoomsResponse) readResponse();
        return response.getRooms();
    }

    public List<UserGroup> getUserGroups()
        throws ClientNetworkException, ClientResponseException
    {
        sendRequest(new UserGroupsRequest());
        UserGroupsResponse response = (UserGroupsResponse) readResponse();
        return response.getUserGroups();
    }

    public List<User> getUserGroupUsers(UserGroup userGroup)
        throws ClientNetworkException, ClientResponseException
    {
        sendRequest(new UserGroupUsersRequest(userGroup));
        UserGroupUsersResponse response = (UserGroupUsersResponse) readResponse();
        return response.getUsers();
    }

    @Override public void updateRoom(Room room) throws ClientNetworkException, ClientResponseException
    {
        sendRequest(new UpdateRoomRequest(room));
        UpdateRoomResponse response = (UpdateRoomResponse) readResponse();
    }

    @Override public void updateUserRoomData(Room room, String comment, Integer color) throws ClientNetworkException, ClientResponseException
    {
        sendRequest(new UpdateUserRoomDataRequest(room, comment, color));
        UpdateUserRoomDataResponse response = (UpdateUserRoomDataResponse) readResponse();
    }

    @Override public List<TimeSlot> getTimeSlots() throws ClientNetworkException, ClientResponseException
    {
        sendRequest(new TimeSlotsRequest());
        TimeSlotsResponse response = (TimeSlotsResponse) readResponse();
        return response.getTimeSlots();
    }

    @Override public void createRoom(String name, RoomType type, int maxComf, int maxSafety, int size, String comment, boolean isDouble, String doubleName)
        throws ClientNetworkException, ClientResponseException
    {
        sendRequest(new CreateRoomRequest(name, type, maxComf, maxSafety, size, comment, isDouble, doubleName));
        CreateRoomResponse response = (CreateRoomResponse) readResponse();
    }

    private void sendRequest(Request request)
        throws ClientNetworkException, ClientResponseException
    {
        try
        {
            outToServer.writeObject(request);
        }
        catch (IOException e)
        {
            throw new ClientNetworkException("IO error when sending request.", e);
        }
    }

    private Response readResponse()
        throws ClientNetworkException, ClientResponseException
    {
        try
        {
            Response response = (Response) inFromServer.readObject();

            if (response instanceof ErrorResponse errorResponse)
            {
                throw new ClientResponseException(errorResponse.getReason());
            }
            else
            {
                return response;
            }
        }
        catch (IOException e)
        {
            throw new ClientNetworkException("IO error when receiving response.", e);
        }
        catch (ClassNotFoundException e)
        {
            throw new ClientNetworkException("Server returned an unknown class.", e);
        }
    }
}
