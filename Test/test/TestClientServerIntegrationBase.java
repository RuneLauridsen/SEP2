package test;

import booking.client.core.ViewModelFactory;
import booking.client.model.ClientModel;
import booking.client.networking.ClientNetworkException;
import booking.client.viewModel.loginVM.LoginViewModel;
import booking.client.viewModel.userGUIVM.UserBookRoomViewModel;
import booking.client.viewModel.userGUIVM.UserHomeScreenViewModel;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import static test.TestConstants.VIAID_GITTE;
import static test.TestConstants.VIAID_RUNE;
import static test.TestConstants.VIAID_SIMON;

public class TestClientServerIntegrationBase
{
    protected FakeViewHandler viewHandler;
    protected ViewModelFactory viewModelFactory;
    protected ClientModel client;
    protected TestUtil.ServerInstance server;

    @BeforeEach void setup() throws ClientNetworkException
    {
        server = TestUtil.setupServer();
        client = TestUtil.setupClient();

        viewModelFactory = new ViewModelFactory();
        viewHandler = new FakeViewHandler(viewModelFactory, client);
    }

    @AfterEach void cleanup()
    {
        TestUtil.setdownServer(server);
    }

    protected void loginAs(int viaid)
    {
        String password = switch (viaid)
            {
                case VIAID_GITTE -> "1234";
                case VIAID_SIMON -> "qwertyuiopåasdfghjklæøzxcvbnm";
                case VIAID_RUNE -> "abc";
                default -> throw new RuntimeException();
            };

        LoginViewModel loginViewModel = viewModelFactory.getLoginViewModel(viewHandler, client);
        loginViewModel.VIAIDProperty().set("" + viaid);
        loginViewModel.passwordProperty().set(password);
        loginViewModel.loginAction();
    }
}
