package booking;

import booking.core.User;
import booking.database.DatabaseHandler;
import javafx.application.Application;
import javafx.stage.Stage;

public class App extends Application
{
    @Override public void start(Stage primaryStage) throws Exception
    {
        DatabaseHandler database = new DatabaseHandler();
        database.open();

        User user = database.getUser("Rune");

        ViewModelFactory viewModelFactory = new ViewModelFactory();
        ViewHandler viewHandler = new ViewHandler(primaryStage, viewModelFactory, database);
        viewHandler.showUserHomeScreen(user);

    }
}
