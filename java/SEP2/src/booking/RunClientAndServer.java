package booking;

import booking.client.logic.ClientModelImpl;
import booking.client.networking.ClientNetworkSocket;
import booking.core.BookingInterval;
import booking.database.DatabaseHandler;
import booking.server.model.ServerModelImpl;
import booking.server.networking.ServerNetworkSocket;
import booking.shared.GetAvailableRoomsParameters;
import com.sun.source.tree.IdentifierTree;

import java.time.LocalDate;
import java.time.LocalDateTime;
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

                var gitte = database.getUser("Gitte");
                var rune = database.getUser("Rune");

                var interval = new BookingInterval(
                    LocalDate.of(2024, 01, 01),
                    LocalTime.of(10, 0),
                    LocalTime.of(16, 0)
                );

                //var gitteRooms = database.getAvailableRooms(gitte, interval, null, null, null, null);
                //var runeRooms = database.getAvailableRooms(rune, interval, null, null, null, null);

                var C0201 = database.getRoom("C02.01", gitte);
                var C0202 = database.getRoom("C02.02", rune);

                serverModel.createBooking(gitte, C0201, new BookingInterval(
                    LocalDate.of(2023, 5, 8),
                    LocalTime.of(10, 0),
                    LocalTime.of(16, 0)
                ));

                serverModel.createBooking(gitte, C0201, new BookingInterval(
                    LocalDate.of(2023, 5, 9),
                    LocalTime.of(10, 0),
                    LocalTime.of(16, 0)
                ));

                serverModel.createBooking(gitte, C0202, new BookingInterval(
                    LocalDate.of(2023, 5, 10),
                    LocalTime.of(10, 0),
                    LocalTime.of(16, 0)
                ));

                serverModel.createBooking(gitte, C0202, new BookingInterval(
                    LocalDate.of(2023, 5, 11),
                    LocalTime.of(10, 0),
                    LocalTime.of(16, 0)
                ));

                serverModel.createBooking(gitte, C0202, new BookingInterval(
                    LocalDate.of(2023, 5, 12),
                    LocalTime.of(10, 0),
                    LocalTime.of(16, 0)
                ));

                var bookings1 = serverModel.getBookingsForUser(
                    "Gitte",
                    LocalDate.MIN,
                    LocalDate.MAX,
                    gitte
                );

                var bookings2 = serverModel.getBookingsForUser(
                    "Gitte",
                    LocalDate.of(2023, 5, 9),
                    LocalDate.of(2023, 5, 11),
                    rune
                );

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
