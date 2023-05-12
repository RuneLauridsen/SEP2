package booking.client.networking;

import booking.core.User;
import booking.shared.GetAvailableRoomsParameters;
import booking.shared.socketMessages.AvailableRoomsRequest;
import booking.shared.socketMessages.AvailableRoomsResponse;
import booking.shared.socketMessages.ConnectionRequest;
import booking.shared.socketMessages.ConnectionResponse;
import booking.core.Booking;
import booking.core.BookingInterval;
import booking.core.Room;
import booking.shared.socketMessages.Request;
import booking.shared.socketMessages.Response;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;

public class ClientNetworkSocket implements ClientNetwork
{
    private Socket socket;
    private ObjectOutputStream outToServer;
    private ObjectInputStream inFromServer;

    @Override public User connect(String username, String password) throws ClientNetworkException
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
        catch (Exception e)
        {
            throw new RuntimeException(e); // TODO(rune): Bedre error handling
        }
    }

    @Override public void disconnect()
    {
        // TODO(rune): Disconnect
    }

    @Override public List<Room> getAvailableRooms(GetAvailableRoomsParameters parameters) throws ClientNetworkException
    {
        sendRequest(new AvailableRoomsRequest(parameters));
        AvailableRoomsResponse response = (AvailableRoomsResponse) readResponse();
        return response.getRooms();
    }

    @Override public List<Booking> getActiveBookings()
    {
        return null;
    }

    @Override public void createBooking(Room room, BookingInterval interval)
    {

    }

    private void sendRequest(Request request) throws ClientNetworkException
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

    private Response readResponse() throws ClientNetworkException
    {
        try
        {
            return (Response) inFromServer.readObject();
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
