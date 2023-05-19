import booking.client.core.ViewHandler;
import booking.client.core.ViewHandlerImpl;
import booking.client.core.ViewModelFactory;
import booking.client.model.ClientModel;
import booking.client.model.ClientModelImpl;
import booking.client.networking.ClientNetwork;
import booking.client.networking.ClientNetworkSocket;
import booking.client.view.login.LoginViewModel;
import booking.client.view.userGUI.UserBookRoomViewModel;
import booking.database.DatabaseHandler;
import booking.server.model.ServerModel;
import booking.server.model.ServerModelImpl;
import booking.server.networking.ServerNetwork;
import booking.server.networking.ServerNetworkSocket;
import booking.shared.objects.BookingInterval;
import booking.shared.objects.Overlap;
import booking.shared.CreateBookingParameters;
import booking.shared.GetAvailableRoomsParameters;
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
        viewHandler = new FakeViewHandler();
        viewModelFactory = new ViewModelFactory();
    }

    @AfterEach void cleanup()
    {
        TestDatabaseUtil.setdown(database);
        serverThread.interrupt();
    }

    @Test void testLogin()
    {
        LoginViewModel loginViewModel = viewModelFactory.getLoginViewModel(viewHandler, model);
        loginViewModel.usernameProperty().set("Rune");
        loginViewModel.passwordProperty().set("asdasd");
        loginViewModel.loginAction();

        // TODO(rune): Test forkert brugernavn og password
        assertEquals(model.getUser().getName(), "Rune");
        assertEquals(model.getUser().getViaId(), 444444);
        assertEquals(model.getUser().getInitials(), null);
        assertEquals(model.getUser().getType().getName(), "Studerende");
    }

    @Test void testRegister()
    {

    }

    @Test void testCreateBooking()
    {
        model.login("Rune", "asdasd");

        UserBookRoomViewModel viewModel = viewModelFactory.getUserBookRoomViewModel(viewHandler, model);
        viewModel.selectedDateProperty().set(LocalDate.of(2023, 5, 9));
        viewModel.selectedFromTimeProperty().set("11:00");
        viewModel.selectedToTimeProperty().set("13:00");
        viewModel.selectedBuildingProperty().set('A');
        viewModel.selectedFloorProperty().set(2);

        viewModel.showAvailableRooms();

        assertEquals(viewModel.getRoomList().size(), 2);
        assertEquals(viewModel.getRoomList().get(0).getName(), "A02.01");
        assertEquals(viewModel.getRoomList().get(1).getName(), "A02.03");

        viewModel.bookRoom(viewModel.getRoomList().get(0));
        viewModel.showAvailableRooms();

        assertEquals(viewModel.getRoomList().size(), 1);
        assertEquals(viewModel.getRoomList().get(0).getName(), "A02.03");
    }
}
