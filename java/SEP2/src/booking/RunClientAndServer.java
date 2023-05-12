package booking;

import booking.client.logic.ClientModelImpl;
import booking.client.networking.ClientNetworkSocket;
import booking.database.DatabaseHandler;
import booking.server.model.ServerModelImpl;
import booking.server.networking.ServerNetworkSocket;
import booking.shared.GetAvailableRoomsParameters;

import java.time.LocalDate;
import java.time.LocalTime;

public class RunClientAndServer
{
    public static void main(String[] args) throws InterruptedException
    {
        var serverThread = new Thread(() -> {
            try
            {
                var database = new DatabaseHandler();
                database.open();

                var serverModel = new ServerModelImpl(database);
                var server = new ServerNetworkSocket(serverModel);

                server.run();
            }
            catch (Exception e)
            {
                throw new RuntimeException(e);
            }
        });

        var clientThread = new Thread(() -> {
            try
            {
                var clientNetwork = new ClientNetworkSocket();
                var clientModel = new ClientModelImpl(clientNetwork);

                clientModel.login("Rune123", "asdasd");

                var p = new GetAvailableRoomsParameters(
                    LocalDate.of(2023, 5, 8),
                    LocalTime.of(10, 0),
                    LocalTime.of(16, 0)
                );

                var rooms = clientModel.getAvailableRooms(p);

                System.out.println(clientModel.getUser());
            }
            catch (Exception e)
            {
                throw new RuntimeException(e);
            }
        });

        serverThread.start();
        Thread.sleep(1000); // Wait for server to start
        clientThread.start();

        serverThread.join();
        clientThread.join();
    }
}
