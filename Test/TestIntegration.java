import booking.client.core.ViewHandler;
import booking.client.core.ViewModelFactory;
import booking.client.model.ClientModel;
import booking.client.model.ClientModelImpl;
import booking.client.networking.ClientNetwork;
import booking.client.networking.ClientNetworkSocket;
import booking.client.view.userGUI.UserBookRoomViewModel;
import booking.database.DatabaseHandler;
import booking.server.model.ServerModel;
import booking.server.model.ServerModelImpl;
import booking.server.networking.ServerNetwork;
import booking.server.networking.ServerNetworkSocket;
import booking.shared.objects.BookingInterval;
import booking.shared.objects.Overlap;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class TestIntegration
{
    private ViewHandler viewHandler;
    private ViewModelFactory viewModelFactory;
    private DatabaseHandler database;
    private Thread serverThread;
    private ClientModel model;

    @BeforeEach void setup()
    {
        database = TestDatabaseUtil.setup();

        // TODO(rune): Vi har ikke nogen måde at lukke serveren og alle dens tråde på en gang.

        ServerModel serverModel = new ServerModelImpl(database);
        ServerNetwork serverNetwork = new ServerNetworkSocket(serverModel);

        ClientNetwork clientNetwork = new ClientNetworkSocket();
        ClientModel clientModel = new ClientModelImpl(clientNetwork);

        serverThread = new Thread(serverNetwork);
        serverThread.setDaemon(true);
        serverThread.start();

        try
        {
            Thread.sleep(1000); // HACK(rune): Wait for server to start.
        }
        catch (InterruptedException e)
        {
            throw new RuntimeException(e);
        }

        model = clientModel;
    }

    @AfterEach void cleanup()
    {
        TestDatabaseUtil.setdown(database);
        serverThread.interrupt();
    }

    @Test void testCreateBooking()
    {
        model.login("Rune", "");

        UserBookRoomViewModel viewModel = new UserBookRoomViewModel(null, model);
        viewModel.selectedDateProperty().set(LocalDate.of(2023, 5, 9));
        viewModel.selectedFromTimeProperty().set("11:00");
        viewModel.selectedToTimeProperty().set("13:00");

        viewModel.showAvailableRooms();

        BookingInterval interval0 = new BookingInterval(LocalDate.of(2023, 5, 12), LocalTime.of(14, 0), LocalTime.of(15, 0));
        BookingInterval interval1 = new BookingInterval(LocalDate.of(2023, 5, 12), LocalTime.of(8, 0), LocalTime.of(18, 0));
        BookingInterval interval2 = new BookingInterval(LocalDate.of(2023, 5, 12), LocalTime.of(10, 0), LocalTime.of(12, 0));

        //CreateBookingParameters parameters1 = new CreateBookingParameters(room, interval1, false, null);
        //CreateBookingParameters parameters0 = new CreateBookingParameters(room, interval0, false, null);
        //CreateBookingParameters parameters2 = new CreateBookingParameters(room, interval2, false, null);

        List<Overlap> overlaps0 = new ArrayList<>();
        List<Overlap> overlaps1 = new ArrayList<>();
        List<Overlap> overlaps2 = new ArrayList<>();
    }
}
