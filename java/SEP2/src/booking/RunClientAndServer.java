package booking;

import booking.client.model.ClientModelImpl;
import booking.client.networking.ClientNetworkSocket;
import booking.database.DatabaseHandler;
import booking.server.model.ServerModelImpl;
import booking.server.networking.ServerNetworkSocket;
import booking.shared.GetAvailableRoomsParameters;
import booking.shared.UpdateRoomParameters;
import booking.shared.objects.Room;

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

                clientModel.login("Gitte", "");

                var timeSlots = clientModel.getTimeSlots();

                Room room = clientModel.getRoom("C02.07");
                UpdateRoomParameters parameters = new UpdateRoomParameters(room);
                parameters.setNewName("C02.99");

                clientModel.updateRoom(room, parameters);

                Room room2 = clientModel.getRoom("C02.99");

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
