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
        viewHandler.showHomeScreen(user);

        /*
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("view/homeScreen/HomeScreen.fxml"));
        Scene scene = null;
        try
        {
            scene = new Scene(fxmlLoader.load(), 309, 238);
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }

        //ChooseDateViewModel viewModel = new ChooseDateViewModel();
        HomeScreen controller = fxmlLoader.getController();
        controller.init();
        //controller.init(viewModel);

        primaryStage.setTitle("Booking");
        primaryStage.setScene(scene);
        primaryStage.show();

         */
    }
}
