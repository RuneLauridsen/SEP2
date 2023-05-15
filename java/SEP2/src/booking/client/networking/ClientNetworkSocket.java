package booking.client.networking;

import booking.shared.objects.User;
import booking.shared.GetAvailableRoomsParameters;
import booking.shared.socketMessages.*;
import booking.shared.objects.Booking;
import booking.shared.objects.BookingInterval;
import booking.shared.objects.Room;

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

    @Override public User connect(String username, String password)
        throws ClientNetworkException, ClientResponseException
    {
        try
        {
            Socket socket = new Socket("localhost", 2910);
            outToServer = new ObjectOutputStream(socket.getOutputStream());
            inFromServer = new ObjectInputStream(socket.getInputStream());

            sendRequest(new ConnectionRequest(username, password));
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

    @Override public List<Room> getAvailableRooms(GetAvailableRoomsParameters parameters)
        throws ClientNetworkException, ClientResponseException
    {
        sendRequest(new AvailableRoomsRequest(parameters));
        AvailableRoomsResponse response = (AvailableRoomsResponse) readResponse();
        return response.getRooms();
    }

    @Override public List<Booking> getActiveBookings(LocalDate start, LocalDate end)
        throws ClientNetworkException, ClientResponseException
    {
        sendRequest(new ActiveBookingsRequest(start, end));
        ActiveBookingsResponse response = (ActiveBookingsResponse) readResponse();
        return response.getBookings();
    }

    @Override public void createBooking(Room room, BookingInterval interval)
        throws ClientNetworkException, ClientResponseException
    {
        sendRequest(new CreateBookingRequest(room, interval));
        CreateBookingResponse response = (CreateBookingResponse) readResponse();
    }

    @Override public Room getRoom(String room, User activeUser)
        throws ClientNetworkException, ClientResponseException
    {
        sendRequest(new RoomRequest(room));
        RoomResponse response = (RoomResponse)readResponse();
        return response.getRoom();
    }

    public List<Booking> getBookingsForRoom(String roomName, LocalDate start, LocalDate end)
        throws ClientNetworkException, ClientResponseException
    {
        sendRequest(new BookingsForRoomRequest(roomName, start, end));
        BookingsForRoomResponse response = (BookingsForRoomResponse) readResponse();
        return response.getBookings();
    }

    public Room getRoom(String roomName)
        throws ClientNetworkException, ClientResponseException
    {
        sendRequest(new RoomRequest(roomName));
        RoomResponse response = (RoomResponse) readResponse();
        return response.getRoom();
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
