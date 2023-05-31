package booking.server.networking;

import booking.server.model.ServerModel;
import booking.server.model.ServerModelException;
import booking.server.model.importFile.ImportFileResult;
import booking.shared.objects.*;
import booking.shared.socketMessages.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;

import static booking.shared.socketMessages.ErrorResponseReason.ERROR_RESPONSE_REASON_INVALID_REQUEST_TYPE;

public class ServerNetworkSocketHandler implements Runnable
{
    private Socket socket;
    private ObjectInputStream inFromClient;
    private ObjectOutputStream outToClient;

    private ServerModel model;

    // NOTE: Så længe activeUser er null, er klienten ikke logget ind. Nogle request
    // kan godt behandles uden af være logget ind, f.eks. CreateUserRequest.
    private User activeUser;

    public ServerNetworkSocketHandler(Socket socket, ServerModel model)
    {
        this.socket = socket;
        this.model = model;
        this.activeUser = null;

        try
        {
            outToClient = new ObjectOutputStream(socket.getOutputStream());
            inFromClient = new ObjectInputStream(socket.getInputStream());
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }


    public void run() {
        try {
            // Message loop indtil client mister forbindelse med IOException
            while (true) {
                Object request = readRequest();

                try
                {
                    sendResponse(getResponseForRequest(request));
                }
                catch (ServerModelException e)
                {
                    sendResponse(new ErrorResponse(e.getReason()));
                }
            }
        }
        catch (IOException e)
        {
            // NOTE: Når server er ved at lukke smider readObject en IOException
            // fordi readObject stream bliver lukket, så readObject ikke blokerer tråden længere.
            // Hvis vi IOException har en anden årsag, lukker vi også bare socket'en.
        }
        finally
        {
            close();
        }
    }

    private Response getResponseForRequest(Object request) throws ServerModelException
    {
        //
        // Login
        //
        if (request instanceof LoginRequest loginRequest)
        {
            activeUser = model.login(loginRequest.getViaid(), loginRequest.getPassword());
            return new LoginResponse(activeUser);
        }

        //
        // Available rooms
        //
        if (request instanceof AvailableRoomsRequest availableRoomsRequest)
        {
            List<Room> availableRooms = model.getAvailableRooms(activeUser, availableRoomsRequest.getParameters());
            return new AvailableRoomsResponse(availableRooms);
        }

        //
        // All rooms
        //
        if (request instanceof RoomsRequest roomsRequest)
        {
            List<Room> rooms = model.getRooms(activeUser);
            return new RoomsResponse(rooms);
        }

        //
        // Get bookings for room
        //
        if (request instanceof BookingsForRoomRequest bookingsForRoomRequest)
        {
            List<Booking> bookingsForRoom = model.getBookingsForRoom(
                activeUser, bookingsForRoomRequest.getRoomName(),
                bookingsForRoomRequest.getFrom(),
                bookingsForRoomRequest.getTo()
            );

            return new BookingsForRoomResponse(bookingsForRoom);
        }

        //
        // Get bookings for user
        //
        if (request instanceof BookingsForUserRequest bookingsForUserRequest)
        {
            List<Booking> bookingsForUser = model.getBookingsForUser(
                activeUser, bookingsForUserRequest.getUser(),
                bookingsForUserRequest.getFrom(),
                bookingsForUserRequest.getTo()
            );

            return new BookingsForUserResponse(bookingsForUser);
        }

        //
        // Create booking
        //

        if (request instanceof CreateBookingRequest createBookingRequest)
        {
            List<Overlap> overlaps = model.createBooking(
                activeUser,
                createBookingRequest.getParameters()
            );
            return new CreateBookingResponse(overlaps);
        }

        //
        // Create Room
        //
        if (request instanceof CreateRoomRequest createRoomRequest)
        {
            model.createRoom(
                activeUser,
                createRoomRequest.getName(),
                createRoomRequest.getType(),
                createRoomRequest.getMaxComf(),
                createRoomRequest.getMaxSafety(),
                createRoomRequest.getSize(),
                createRoomRequest.getComment(),
                createRoomRequest.isDouble(),
                createRoomRequest.getDoubleName()
            );

            return new CreateRoomResponse();
        }

        //
        // Delete booking
        //
        if (request instanceof DeleteBookingRequest deleteBookingRequest)
        {
            model.deleteBooking(
                activeUser,
                deleteBookingRequest.getBooking()
            );

            return new DeleteBookingResponse();
        }

        //
        // Room
        //
        if (request instanceof RoomRequest roomRequest)
        {
            Room room = model.getRoom(roomRequest.getRoomName(), activeUser);

            return new RoomResponse(room);
        }
        //
        //Delete room
        //
        if (request instanceof DeleteRoomRequest deleteRoomRequest)
        {
            model.deleteRoom(
                activeUser, deleteRoomRequest.getRoom()
            );

            return new DeleteRoomResponse();
        }

        //
        // Room types
        //
        if (request instanceof RoomTypesRequest roomTypesRequest)
        {
            List<RoomType> roomTypes = model.getRoomTypes();

            return new RoomTypesResponse(roomTypes);
        }

        //
        // User types
        //
        if (request instanceof UserTypesRequest userTypesRequest)
        {
            List<UserType> userTypes = model.getUserTypes();
            return new UserTypesResponse(userTypes);
        }

        //
        // User groups
        //
        if (request instanceof UserGroupsRequest userGroupsRequest)
        {
            List<UserGroup> userGroups = model.getUserGroups();

            return new UserGroupsResponse(userGroups);
        }

        //
        // User group users
        //
        if (request instanceof UserGroupUsersRequest userGroupUsersRequest)
        {
            List<User> users = model.getUserGroupUsers(userGroupUsersRequest.getUserGroup());

            return new UserGroupUsersResponse(users);
        }

        // Update room
        if (request instanceof UpdateRoomRequest updateRoomRequest)
        {
            model.updateRoom(
                activeUser, updateRoomRequest.getRoom()
            );

            return new UpdateRoomResponse();
        }


        //
        // Update user room data
        //
        if (request instanceof UpdateUserRoomDataRequest updateUserRoomDataRequest)
        {
            model.updateUserRoomData(
                activeUser,
                updateUserRoomDataRequest.getRoom(),
                updateUserRoomDataRequest.getComment(),
                updateUserRoomDataRequest.getColor()
            );

            return new UpdateUserRoomDataResponse();
        }

        //
        // Create user
        //
        if (request instanceof CreateUserRequest createUserRequest)
        {
            model.createUser(
                createUserRequest.getUsername(),
                createUserRequest.getPassword(),
                createUserRequest.getInitials(),
                createUserRequest.getViaid(),
                createUserRequest.getUserType()
            );

            return new CreateUserResponse();
        }

        // Import file
        if (request instanceof ImportFileRequest overlapsRequest)
        {
            ImportFileResult result = model.importFile(
                activeUser, overlapsRequest.getFileContent()
            );

            return new ImportFileResponse(result);
        }

        //
        // Unknown request
        //
        return new ErrorResponse(ERROR_RESPONSE_REASON_INVALID_REQUEST_TYPE);
    }

    private void sendResponse(Response response) throws IOException
    {
        outToClient.writeObject(response);
    }

    private Request readRequest() throws IOException
    {
        try
        {
            return (Request) inFromClient.readObject();
        }
        catch (ClassNotFoundException e)
        {
            return null; // Caller handles null and returns ErrorResponse
        }
    }

    public void close()
    {
        try
        {
            inFromClient.close();
            outToClient.close();
            socket.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
