package booking;

import booking.client.core.ViewHandler;
import booking.client.core.ViewModelFactory;
import booking.client.model.ClientModel;
import booking.client.model.ClientModelImpl;
import booking.client.networking.ClientNetwork;
import booking.client.networking.ClientNetworkSocket;
import booking.shared.objects.User;
import booking.database.DatabaseHandler;
import javafx.application.Application;
import javafx.stage.Stage;

public class App extends Application
{
    @Override public void start(Stage primaryStage) throws Exception
    {
        DatabaseHandler database = new DatabaseHandler();
        database.open();

        // TODO(rune): Fjern
        User user = database.getUser("Rune");

        ClientNetwork network = new ClientNetworkSocket();
        ClientModel model = new ClientModelImpl(network);
        model.login("Rune", "");

        ViewModelFactory viewModelFactory = new ViewModelFactory();
        ViewHandler viewHandler = new ViewHandler(primaryStage, viewModelFactory, model);
        viewHandler.showCoordinatorHomeScreen(user);

    }
}
