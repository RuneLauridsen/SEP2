package booking.client.networking;

import booking.server.model.importFile.ImportFileResult;
import booking.shared.CreateBookingParameters;
import booking.shared.GetAvailableRoomsParameters;
import booking.shared.objects.*;
import booking.shared.socketMessages.*;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;

public class ClientNetworkSocket implements ClientNetwork
{
    private ObjectOutputStream outToServer;
    private ObjectInputStream inFromServer;

    @Override public void connect()
    {
        try
        {
            URI uri = getClass().getResource("ClientNetworkSocketHost.txt").toURI();
            String host = Files.readString(Path.of(uri));

            Socket socket = new Socket(host, 2910);
            outToServer = new ObjectOutputStream(socket.getOutputStream());
            inFromServer = new ObjectInputStream(socket.getInputStream());
        }
        catch (IOException | URISyntaxException e)
        {
            throw new RuntimeException(e);
        }
    }

    @Override public User login(int viaid, String password)
        throws ClientNetworkException, ClientNetworkResponseException
    {
        sendRequest(new LoginRequest(viaid, password));
        LoginResponse response = (LoginResponse) readResponse();
        return response.getUser();
    }

    @Override public void logout()
        throws ClientNetworkException, ClientNetworkResponseException
    {
        // TODO: Logout request. Dog lukker ClientNetworkSocketHandler automatisk når client disconnecter.
    }

    public void createUser(String username, String password, String initials, int viaid, UserType userType)
        throws ClientNetworkException, ClientNetworkResponseException
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
        throws ClientNetworkException, ClientNetworkResponseException
    {
        sendRequest(new DeleteRoomRequest(room));
        DeleteRoomResponse response = (DeleteRoomResponse) readResponse();
    }

    @Override public List<Room> getAvailableRooms(GetAvailableRoomsParameters parameters)
        throws ClientNetworkException, ClientNetworkResponseException
    {
        sendRequest(new AvailableRoomsRequest(parameters));
        AvailableRoomsResponse response = (AvailableRoomsResponse) readResponse();
        return response.getRooms();
    }

    @Override public List<Overlap> createBooking(CreateBookingParameters parameters)
        throws ClientNetworkException, ClientNetworkResponseException
    {
        sendRequest(new CreateBookingRequest(parameters));
        CreateBookingResponse response = (CreateBookingResponse) readResponse();
        return response.getOverlaps();
    }

    @Override public void deleteBooking(Booking booking)
        throws ClientNetworkException, ClientNetworkResponseException
    {
        sendRequest(new DeleteBookingRequest(booking));
        DeleteBookingResponse response = (DeleteBookingResponse) readResponse();
    }

    @Override public Room getRoom(String roomName)
        throws ClientNetworkException, ClientNetworkResponseException
    {
        sendRequest(new RoomRequest(roomName));
        RoomResponse response = (RoomResponse) readResponse();
        return response.getRoom();
    }

    @Override public List<RoomType> getRoomTypes()
        throws ClientNetworkException, ClientNetworkResponseException
    {
        sendRequest(new RoomTypesRequest());
        RoomTypesResponse response = (RoomTypesResponse) readResponse();
        return response.getRoomTypes();
    }

    @Override public List<UserType> getUserTypes()
        throws ClientNetworkException, ClientNetworkResponseException
    {
        sendRequest(new UserTypesRequest());
        UserTypesResponse response = (UserTypesResponse) readResponse();
        return response.getUserTypes();
    }

    public List<Booking> getBookingsForRoom(String roomName, LocalDate start, LocalDate end)
        throws ClientNetworkException, ClientNetworkResponseException
    {
        sendRequest(new BookingsForRoomRequest(roomName, start, end));
        BookingsForRoomResponse response = (BookingsForRoomResponse) readResponse();
        return response.getBookings();
    }

    public List<Booking> getBookingsForUser(User user, LocalDate start, LocalDate end)
        throws ClientNetworkException, ClientNetworkResponseException
    {
        sendRequest(new BookingsForUserRequest(user, start, end));
        BookingsForUserResponse response = (BookingsForUserResponse) readResponse();
        return response.getBookings();
    }

    @Override public List<Room> getRooms()
        throws ClientNetworkException, ClientNetworkResponseException
    {
        sendRequest(new RoomsRequest());
        RoomsResponse response = (RoomsResponse) readResponse();
        return response.getRooms();
    }

    public List<UserGroup> getUserGroups()
        throws ClientNetworkException, ClientNetworkResponseException
    {
        sendRequest(new UserGroupsRequest());
        UserGroupsResponse response = (UserGroupsResponse) readResponse();
        return response.getUserGroups();
    }

    public List<User> getUserGroupUsers(UserGroup userGroup)
        throws ClientNetworkException, ClientNetworkResponseException
    {
        sendRequest(new UserGroupUsersRequest(userGroup));
        UserGroupUsersResponse response = (UserGroupUsersResponse) readResponse();
        return response.getUsers();
    }

    @Override public void updateRoom(Room room) throws ClientNetworkException, ClientNetworkResponseException
    {
        sendRequest(new UpdateRoomRequest(room));
        UpdateRoomResponse response = (UpdateRoomResponse) readResponse();
    }

    @Override public void updateUserRoomData(Room room, String comment, Integer color) throws ClientNetworkException, ClientNetworkResponseException
    {
        sendRequest(new UpdateUserRoomDataRequest(room, comment, color));
        UpdateUserRoomDataResponse response = (UpdateUserRoomDataResponse) readResponse();
    }

    @Override public void createRoom(String name, RoomType type, int maxComf, int maxSafety, int size, String comment, boolean isDouble, String doubleName)
        throws ClientNetworkException, ClientNetworkResponseException
    {
        sendRequest(new CreateRoomRequest(name, type, maxComf, maxSafety, size, comment, isDouble, doubleName));
        CreateRoomResponse response = (CreateRoomResponse) readResponse();
    }

    @Override public ImportFileResult importFile(String fileContent)
        throws ClientNetworkException, ClientNetworkResponseException
    {
        sendRequest(new ImportFileRequest(fileContent));
        ImportFileResponse response = (ImportFileResponse) readResponse();
        return response.getResult();
    }

    private void sendRequest(Request request)
        throws ClientNetworkException
    {
        try
        {
            outToServer.writeObject(request);
        }
        catch (IOException e)
        {
            throw new ClientNetworkException("IO error when sending request: " + e.getMessage(), e);
        }
    }

    private Response readResponse()
        throws ClientNetworkException, ClientNetworkResponseException
    {
        try
        {
            Response response = (Response) inFromServer.readObject();

            if (response instanceof ErrorResponse errorResponse)
            {
                throw new ClientNetworkResponseException(errorResponse.getReason());
            }
            else
            {
                return response;
            }
        }
        catch (IOException e)
        {
            throw new ClientNetworkException("IO error when receiving response: " + e.getMessage(), e);
        }
        catch (ClassNotFoundException e)
        {
            throw new ClientNetworkException("Server returned an unknown class.", e);
        }
    }
}
