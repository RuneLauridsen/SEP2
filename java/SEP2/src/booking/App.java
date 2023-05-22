package booking;

import booking.client.core.ViewHandler;
import booking.client.core.ViewHandlerImpl;
import booking.client.core.ViewModelFactory;
import booking.client.model.ClientModel;
import booking.client.model.ClientModelImpl;
import booking.client.networking.ClientNetwork;
import booking.client.networking.ClientNetworkSocket;
import booking.shared.NowProvider;
import booking.shared.ReadTimeNowProvider;
import booking.shared.objects.User;
import booking.database.DatabaseHandler;
import booking.shared.objects.UserType;
import javafx.application.Application;
import javafx.stage.Stage;

public class App extends Application
{
    @Override public void start(Stage primaryStage) throws Exception
    {
        DatabaseHandler database = new DatabaseHandler();
        database.open();

        NowProvider nowProvider = new ReadTimeNowProvider();
        ClientNetwork network = new ClientNetworkSocket();
        ClientModel model = new ClientModelImpl(network, nowProvider);

        ViewModelFactory viewModelFactory = new ViewModelFactory();
        ViewHandler viewHandler = new ViewHandlerImpl(primaryStage, viewModelFactory, model);

        model.login(555555, "1234");
        viewHandler.showCoordinatorBookingMenu();
    }
}
