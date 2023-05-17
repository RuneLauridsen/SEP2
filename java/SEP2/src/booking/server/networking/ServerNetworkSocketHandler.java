package booking.server.networking;

import booking.shared.objects.Booking;
import booking.shared.objects.Room;
import booking.shared.objects.TimeSlot;
import booking.shared.objects.User;
import booking.server.model.ServerModel;
import booking.shared.objects.UserGroup;
import booking.shared.socketMessages.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
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
            //
            // Login
            //
            ConnectionRequest connectionRequest = (ConnectionRequest) readRequest();
            User user = model.getUser(connectionRequest.getUsername());
            if (user != null)
            {
                sendResponse(new ConnectionResponse(user));
            }
            else
            {
                sendResponse(new ErrorResponse(ERROR_RESPONSE_REASON_INVALID_CREDENTIALS));

                // NOTE(rune): Ved at afvise connection of return før message-loopet, slipper vi
                // for at skulle håndtere error case'en bruger-er-ikke-logget-på, når vi behandler
                // andre requests.
                return;
            }

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

                //
                // Available rooms
                //
                if (request instanceof AvailableRoomsRequest availableRoomsRequest)
                {
                    List<Room> availableRooms = model.getAvailableRooms(user, availableRoomsRequest.getParameters());
                    sendResponse(new AvailableRoomsResponse(availableRooms));
                }

                //
                // Get bookings for room
                //
                else if (request instanceof BookingsForRoomRequest bookingsForRoomRequest)
                {
                    List<Booking> bookingsForRoom = model.getBookingsForRoom(
                        bookingsForRoomRequest.getRoomName(),
                        bookingsForRoomRequest.getFrom(),
                        bookingsForRoomRequest.getTo(),
                        user);

                    sendResponse(new BookingsForRoomResponse(bookingsForRoom));
                }

                //
                // Get bookings for user
                //
                else if (request instanceof BookingsForUserRequest bookingsForUserRequest)
                {
                    List<Booking> bookingsForUser = model.getBookingsForUser(
                        bookingsForUserRequest.getUsername(),
                        bookingsForUserRequest.getFrom(),
                        bookingsForUserRequest.getTo(),
                        user
                    );

                    sendResponse(new BookingsForUserResponse(bookingsForUser));
                }

                //
                // Create booking
                //
                else if (request instanceof CreateBookingRequest createBookingRequest)
                {
                    ErrorResponseReason createBookingResult = model.createBooking(
                        user,
                        createBookingRequest.getRoom(),
                        createBookingRequest.getInterval()
                    );

                    if (createBookingResult == ERROR_RESPONSE_REASON_NONE)
                    {
                        sendResponse(new CreateBookingResponse());
                    }
                    else
                    {
                        sendResponse(new ErrorResponse(createBookingResult));
                    }
                }

                //
                // Create Room
                //
                else if (request instanceof CreateRoomRequest createRoomRequest)
                {
                    ErrorResponseReason createRoomResult = model.createRoom(
                        createRoomRequest.getName(),
                        createRoomRequest.getType(),
                        createRoomRequest.getMaxComf(),
                        createRoomRequest.getMaxSafety(),
                        createRoomRequest.getSize(),
                        createRoomRequest.getComment(),
                        createRoomRequest.isDouble(),
                        createRoomRequest.getDoubleName()
                    );

                    if (createRoomResult == ERROR_RESPONSE_REASON_NONE)
                    {
                        sendResponse(new CreateRoomResponse());
                    }
                    else
                    {
                        sendResponse(new ErrorResponse(createRoomResult));
                    }
                }

                //
                // Delete booking
                //
                else if (request instanceof DeleteBookingRequest deleteBookingRequest)
                {
                    ErrorResponseReason deleteBookingResult = model.deleteBooking(
                        user,
                        deleteBookingRequest.getBooking()
                    );

                    if (deleteBookingResult == ERROR_RESPONSE_REASON_NONE)
                    {
                        sendResponse(new DeleteBookingResponse());
                    }
                    else
                    {
                        sendResponse(new ErrorResponse(deleteBookingResult));
                    }
                }

                //
                // Room
                //
                else if (request instanceof RoomRequest roomRequest)
                {
                    Room room = model.getRoom(roomRequest.getRoomName(), user);

                    sendResponse(new RoomResponse(room));
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
                    ErrorResponseReason error = model.updateRoom(
                        updateRoomRequest.getRoom(),
                        updateRoomRequest.getParameters(),
                        user
                    );

                    if (error == ERROR_RESPONSE_REASON_NONE)
                    {
                        sendResponse(new UpdateRoomResponse());
                    }
                    else
                    {
                        sendResponse(new ErrorResponse(error));
                    }
                }

                //
                // Update user room data
                //
                else if (request instanceof UpdateUserRoomDataRequest updateUserRoomDataRequest)
                {
                    model.updateUserRoomData(user,
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
                // Unknown request
                //
                else
                {
                    sendResponse(new ErrorResponse(ERROR_RESPONSE_REASON_INVALID_REQUEST_TYPE));
                }
            }
        }
        catch (ServerNetworkException e)
        {
            throw new RuntimeException(e); // TODO(rune): Bedre error handling
        }
        finally
        {
            close();
        }
    }

    private void sendResponse(Response response) throws ServerNetworkException
    {
        try
        {
            outToClient.writeObject(response);
        }
        catch (IOException e)
        {
            throw new ServerNetworkException("IO error when sending response.", e);
        }
    }

    private Request readRequest() throws ServerNetworkException
    {
        try
        {
            return (Request) inFromClient.readObject();
        }
        catch (ClassNotFoundException e)
        {
            return null; // Caller handles null and returns ErrorResponse
        }
        catch (IOException e)
        {
            throw new ServerNetworkException("IO error when receiving request.", e);
        }
    }

    private void close()
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
