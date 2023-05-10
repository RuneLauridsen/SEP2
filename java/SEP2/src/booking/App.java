package booking;

import javafx.application.Application;
import javafx.stage.Stage;

public class App extends Application
{
    @Override public void start(Stage primaryStage) throws Exception
    {
        ViewModelFactory viewModelFactory = new ViewModelFactory();
        ViewHandler viewHandler = new ViewHandler(primaryStage, viewModelFactory);
        viewHandler.showHomeScreen(null);
        viewHandler.showLogin();

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
