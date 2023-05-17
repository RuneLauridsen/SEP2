package booking;

import booking.client.model.ClientModelImpl;
import booking.client.networking.ClientNetworkSocket;
import booking.database.DatabaseHandler;
import booking.server.model.ServerModelImpl;
import booking.server.networking.ServerNetworkSocket;

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

                clientModel.login("Gitte", "");

                var timeSlots = clientModel.getTimeSlots();

                System.out.println(timeSlots);

            }
            catch (Exception e)
            {
                throw new RuntimeException(e);
            }
        });

        serverThread.start();
        Thread.sleep(2000); // Wait for server to start
        clientThread.start();

        serverThread.join();
        clientThread.join();
    }
}
