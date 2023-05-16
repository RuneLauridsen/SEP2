package booking;

import booking.client.model.ClientModelImpl;
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

                clientModel.login("Gitte", "");

                var gitte = clientModel.getUser();
                var ug = clientModel.getUserGroups();
                var ugu = clientModel.getUserGroupUsers(ug.get(1));

                var C0201 = clientModel.getRoom("C02.01");
                var C0202 = clientModel.getRoom("C02.02");
                var C0203 = clientModel.getRoom("C02.03");
                var C0204 = clientModel.getRoom("C02.04");

                var params = new GetAvailableRoomsParameters(
                    LocalDate.of(2023, 01, 01),
                    LocalTime.of(10, 0),
                    LocalTime.of(16, 0)
                );

                var rooms0 = clientModel.getAvailableRooms(params);

                clientModel.updateUserRoomData(C0202, "ny komt", 69420);

                var rooms1 = clientModel.getAvailableRooms(params);

                clientModel.updateUserRoomData(C0204, "ny komt 4", 444);

                var rooms2 = clientModel.getAvailableRooms(params);

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
