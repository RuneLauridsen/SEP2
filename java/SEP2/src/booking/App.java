package booking;

import booking.client.core.ViewHandler;
import booking.client.core.ViewHandlerImpl;
import booking.client.core.ViewModelFactory;
import booking.client.model.ClientModel;
import booking.client.model.ClientModelImpl;
import booking.client.model.FileIO;
import booking.client.model.FileIOImpl;
import booking.client.networking.ClientNetwork;
import booking.client.networking.ClientNetworkSocket;
import booking.shared.NowProvider;
import booking.shared.ReadTimeNowProvider;
import booking.server.persistene.DatabaseHandler;
import javafx.application.Application;
import javafx.stage.Stage;

public class App extends Application
{
    @Override public void start(Stage primaryStage) throws Exception
    {
        DatabaseHandler database = new DatabaseHandler();
        database.open();

        FileIO fileIO = new FileIOImpl();
        NowProvider nowProvider = new ReadTimeNowProvider();
        ClientNetwork network = new ClientNetworkSocket();
        ClientModel model = new ClientModelImpl(network, nowProvider, fileIO);

        ViewModelFactory viewModelFactory = new ViewModelFactory();
        ViewHandler viewHandler = new ViewHandlerImpl(primaryStage, viewModelFactory, model);

        network.connect();
        viewHandler.showLogin();
    }
}
