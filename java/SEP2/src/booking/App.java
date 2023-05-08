package booking;

import booking.view.chooseDate.ChooseDateView;
import booking.view.chooseDate.ChooseDateViewModel;
import booking.view.homeScreen.UserHomeScreen;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class App extends Application
{
    @Override public void start(Stage primaryStage) throws Exception
    {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("view/homeScreen/UserHomeScreen.fxml"));
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
        UserHomeScreen controller = fxmlLoader.getController();
        controller.init();
        //controller.init(viewModel);

        primaryStage.setTitle("Booking");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
