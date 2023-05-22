package test;

import booking.client.core.ViewHandler;
import booking.client.core.ViewModelFactory;
import booking.client.model.ClientModel;
import booking.client.model.ClientModelImpl;
import booking.client.networking.ClientNetwork;
import booking.client.networking.ClientNetworkSocket;
import booking.client.networking.ClientResponseException;
import booking.client.view.login.LoginViewModel;
import booking.client.view.userGUI.UserBookRoomViewModel;
import booking.database.DatabaseHandler;
import booking.shared.NowProvider;
import booking.server.model.ServerModel;
import booking.server.model.ServerModelImpl;
import booking.server.networking.ServerNetwork;
import booking.server.networking.ServerNetworkSocket;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static test.TestConstants.*;

import java.time.LocalDate;

public class TestIntegration
{
    private ViewHandler viewHandler;
    private ViewModelFactory viewModelFactory;
    private DatabaseHandler database;
    private Thread serverThread;
    private ServerNetwork server;
    private ClientModel model;

    @BeforeEach void setup()
    {
        database = TestDatabaseUtil.setup();

        // TODO(rune): Vi har ikke nogen måde at lukke serveren og alle dens tråde på en gang.

        NowProvider nowProvider = new FakeNowProvider();
        ServerModel serverModel = new ServerModelImpl(database, nowProvider);
        ServerNetwork serverNetwork = new ServerNetworkSocket(serverModel);

        ClientNetwork clientNetwork = new ClientNetworkSocket();
        ClientModel clientModel = new ClientModelImpl(clientNetwork, nowProvider);

        serverThread = new Thread(serverNetwork);
        serverThread.setDaemon(true);
        serverThread.start();

        try
        {
            Thread.sleep(1000); // HACK(rune): Vent på at server starter.
        }
        catch (InterruptedException e)
        {
            throw new RuntimeException(e);
        }

        server = serverNetwork;
        model = clientModel;
        viewHandler = new FakeViewHandler();
        viewModelFactory = new ViewModelFactory();
    }

    @AfterEach void cleanup()
    {
        TestDatabaseUtil.setdown(database);
        serverThread.interrupt();
        server.close();
    }

    @Test void testLogin()
    {
        LoginViewModel loginViewModel = viewModelFactory.getLoginViewModel(viewHandler, model);
        loginViewModel.usernameProperty().set(Integer.toString(VIAID_RUNE));
        loginViewModel.passwordProperty().set("abc");
        loginViewModel.loginAction();

        assertEquals(model.getUser().getName(), "Rune");
        assertEquals(model.getUser().getViaId(), 444444);
        assertEquals(model.getUser().getInitials(), null);
        assertEquals(model.getUser().getType().getName(), "Studerende");
    }

    @Test void testLogin_invalidCredentials()
    {
        // TODO(rune): Test forkert brugernavn og password
    }

    @Test void testRegister()
    {
        // TODO(rune): Test register
    }

    @Test void testCreateBooking()
    {
        model.login(VIAID_RUNE, "abc");

        // NOTE(rune): Tjek om vi finder de rigtige lokaler
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

        // NOTE(rune): Tjek at booket lokale ikke længere bliver vist på listen
        // TODO(rune): Burde available rooms listen opdatere sig automatisk, når man har klikket book?
        viewModel.bookRoom(viewModel.getRoomList().get(0));
        viewModel.showAvailableRooms();
        assertEquals(viewModel.getRoomList().size(), 1);
        assertEquals(viewModel.getRoomList().get(0).getName(), "A02.03");

        // NOTE(rune): Tjek om booket lokale dukker på på listen igen.
        viewModel.selectedFromTimeProperty().set("13:00");
        viewModel.selectedToTimeProperty().set("14:00");
        viewModel.showAvailableRooms();
        assertEquals(viewModel.getRoomList().size(), 2);
        assertEquals(viewModel.getRoomList().get(0).getName(), "A02.01");
        assertEquals(viewModel.getRoomList().get(1).getName(), "A02.03");
    }
}
