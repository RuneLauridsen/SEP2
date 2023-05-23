package booking.server.networking;

import booking.server.model.ServerModelException;
import booking.server.model.importFile.ImportFileResult;
import booking.shared.objects.*;
import booking.server.model.ServerModel;
import booking.shared.socketMessages.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.time.LocalDate;
import java.util.List;

import static booking.shared.socketMessages.ErrorResponseReason.*;

public class ServerNetworkSocketHandler implements Runnable
{
    private Socket socket;
    private ObjectInputStream inFromClient;
    private ObjectOutputStream outToClient;

    private ServerModel model;

    public ServerNetworkSocketHandler(Socket socket, ServerModel model)
    {
        this.socket = socket;
        this.model = model;

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

    public void run()
    {
        try
        {
            // TODO(rune): Lav sådan at man kan registerer ny bruger uden at være logget ind.

            // NOTE(rune): Så længe activeUser er null, er klienten ikke logget ind. Nogle request
            // kan godt behandles uden af være logget ind, f.eks. CreateUserRequest.
            User activeUser = null;

            //
            // Message loop indtil client logger af eller mister forbindelse
            //
            while (true)
            {
                // TODO(rune): Open/Closed - ligesom Jens snakkede om.
                // Enten kan vi lave et fælles Request interface som har en getResponse metode,
                // eller også laver vi bare et Map<RequestType, RequestHandler>, med function pointere til,
                // hvordan man finder response. Hælder mest til nr. 2.
                Object request = readRequest();

                try
                {

                    //
                    // Login
                    //
                    if (request instanceof LoginRequest loginRequest)
                    {
                        activeUser = model.login(loginRequest.getViaid(), loginRequest.getPassword());
                        sendResponse(new LoginResponse(activeUser));
                    }

                    //
                    // Available rooms
                    //
                    else if (request instanceof AvailableRoomsRequest availableRoomsRequest)
                    {
                        List<Room> availableRooms = model.getAvailableRooms(activeUser, availableRoomsRequest.getParameters());
                        sendResponse(new AvailableRoomsResponse(availableRooms));
                    }

                    //
                    // All rooms
                    //
                    else if (request instanceof RoomsRequest roomsRequest)
                    {
                        List<Room> rooms = model.getRooms(activeUser);
                        sendResponse(new RoomsResponse(rooms));
                    }

                    //
                    // Get bookings for room
                    //
                    else if (request instanceof BookingsForRoomRequest bookingsForRoomRequest)
                    {
                        List<Booking> bookingsForRoom = model.getBookingsForRoom(
                            activeUser, bookingsForRoomRequest.getRoomName(),
                            bookingsForRoomRequest.getFrom(),
                            bookingsForRoomRequest.getTo()
                        );

                        sendResponse(new BookingsForRoomResponse(bookingsForRoom));
                    }

                    //
                    // Get bookings for user
                    //
                    else if (request instanceof BookingsForUserRequest bookingsForUserRequest)
                    {
                        List<Booking> bookingsForUser = model.getBookingsForUser(
                            activeUser, bookingsForUserRequest.getUser(),
                            bookingsForUserRequest.getFrom(),
                            bookingsForUserRequest.getTo()
                        );

                        sendResponse(new BookingsForUserResponse(bookingsForUser));
                    }

                    //
                    // Create booking
                    //
                    else if (request instanceof CreateBookingRequest createBookingRequest)
                    {
                        List<Overlap> overlaps = model.createBooking(
                            activeUser,
                            createBookingRequest.getParameters()
                        );

                        var remove = model.getBookingsForRoom(
                            activeUser, createBookingRequest.getParameters().getRoom().getName(),
                            LocalDate.MIN,
                            LocalDate.MAX
                        );

                        sendResponse(new CreateBookingResponse(overlaps));
                    }

                    //
                    // Create Room
                    //
                    else if (request instanceof CreateRoomRequest createRoomRequest)
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

                        sendResponse(new CreateRoomResponse());
                    }

                    //
                    // Delete booking
                    //
                    else if (request instanceof DeleteBookingRequest deleteBookingRequest)
                    {
                        model.deleteBooking(
                            activeUser,
                            deleteBookingRequest.getBooking()
                        );

                        sendResponse(new DeleteBookingResponse());
                    }

                    //
                    // Room
                    //
                    else if (request instanceof RoomRequest roomRequest)
                    {
                        Room room = model.getRoom(roomRequest.getRoomName(), activeUser);

                        sendResponse(new RoomResponse(room));
                    }

                    //
                    // Room types
                    //
                    else if (request instanceof RoomTypesRequest roomTypesRequest)
                    {
                        List<RoomType> roomTypes = model.getRoomTypes();

                        sendResponse(new RoomTypesResponse(roomTypes));
                    }

                    //
                    // User types
                    //
                    else if (request instanceof UserTypesRequest userTypesRequest)
                    {
                        List<UserType> userTypes = model.getUserTypes();
                        sendResponse(new UserTypesResponse(userTypes));
                    }

                    //
                    // User groups
                    //
                    else if (request instanceof UserGroupsRequest userGroupsRequest)
                    {
                        List<UserGroup> userGroups = model.getUserGroups();

                        sendResponse(new UserGroupsResponse(userGroups));
                    }

                    //
                    // User group users
                    //
                    else if (request instanceof UserGroupUsersRequest userGroupUsersRequest)
                    {
                        List<User> users = model.getUserGroupUsers(userGroupUsersRequest.getUserGroup());

                        sendResponse(new UserGroupUsersResponse(users));
                    }

                    // Update room
                    else if (request instanceof UpdateRoomRequest updateRoomRequest)
                    {
                        model.updateRoom(
                            activeUser, updateRoomRequest.getRoom()
                        );

                        sendResponse(new UpdateRoomResponse());
                    }

                    //
                    // Update user room data
                    //
                    else if (request instanceof UpdateUserRoomDataRequest updateUserRoomDataRequest)
                    {
                        model.updateUserRoomData(activeUser,
                                                 updateUserRoomDataRequest.getRoom(),
                                                 updateUserRoomDataRequest.getComment(),
                                                 updateUserRoomDataRequest.getColor());

                        sendResponse(new UpdateUserRoomDataResponse());
                    }

                    //
                    // Time slots
                    //
                    else if (request instanceof TimeSlotsRequest timeSlotsRequest)
                    {
                        List<TimeSlot> timeSlots = model.getTimeSlots();
                        sendResponse(new TimeSlotsResponse(timeSlots));
                    }

                    //
                    // Create user
                    //
                    else if (request instanceof CreateUserRequest createUserRequest)
                    {
                        model.createUser(
                            createUserRequest.getUsername(),
                            createUserRequest.getPassword(),
                            createUserRequest.getInitials(),
                            createUserRequest.getViaid(),
                            createUserRequest.getUserType()
                        );

                        sendResponse(new CreateUserResponse());
                    }

                    // Import file
                    else if (request instanceof ImportFileRequest overlapsRequest)
                    {
                        ImportFileResult result = model.importFile(
                            activeUser, overlapsRequest.getFileContent()
                        );

                        sendResponse(new ImportFileResponse(result));
                    }

                    //
                    // Unknown request
                    //
                    else
                    {
                        sendResponse(new ErrorResponse(ERROR_RESPONSE_REASON_INVALID_REQUEST_TYPE));
                    }
                }
                catch (ServerModelException e)
                {
                    sendResponse(new ErrorResponse(e.getReason()));
                }
            }
        }
        catch (IOException e)
        {
            // NOTE(rune): Når server er ved at lukke smider readObject en IOException
            // fordi readObject stream bliver lukket, så readObject ikke blokere tråden længere.
            // Hvis vi IOException har en anden årsag, lukker vi også bare socket'en.
        }
        finally
        {
            close();
        }
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
            // TODO(rune): Giver det mening at håndtere fejlen anderledes?
            e.printStackTrace();
        }
    }
}
