package booking;

import booking.core.User;
import booking.view.homeScreen.HomeScreen;
import booking.view.login.Login;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Optional;

// TODO(rune): Meget gentaget kode i showXYZ metoder.
public class ViewHandler
{
    private static final String DEFAULT_WINDOW_TITLE = "Bookingsystem";

    private final Stage primaryStage;
    private final ViewModelFactory viewModelFactory;

    public ViewHandler(Stage primaryStage, ViewModelFactory viewModelFactory)
    {
        this.primaryStage = primaryStage;
        this.viewModelFactory = viewModelFactory;
    }

    public void showLogin()
    {
        try
        {
            Scene scene = null;
            FXMLLoader loader = new FXMLLoader();
            Parent root = null;

            loader.setLocation(getClass().getResource("view/login/Login.fxml"));
            root = loader.load();

            Login view = loader.getController();
            view.init(viewModelFactory.getLoginViewModel(this, null));

            scene = new Scene(root, 309, 238);
            primaryStage.setScene(scene);
            primaryStage.show();
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
        }
    }

    public void showHomeScreen(User user)
    {
        try
        {
            Scene scene = null;
            FXMLLoader loader = new FXMLLoader();
            Parent root = null;

            loader.setLocation(getClass().getResource("view/homeScreen/HomeScreen.fxml"));
            root = loader.load();

            HomeScreen view = loader.getController();
            view.init(viewModelFactory.getUserHomeScreenViewModel(this, null));

            scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.show();
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
        }
    }

    public void showInfo(String header, String content)
    {
        showAlert(Alert.AlertType.INFORMATION, DEFAULT_WINDOW_TITLE, header, content);
    }

    public void showWarning(String header, String content)
    {
        showAlert(Alert.AlertType.WARNING, DEFAULT_WINDOW_TITLE, header, content);
    }

    public void showError(String header, String content)
    {
        showAlert(Alert.AlertType.ERROR, DEFAULT_WINDOW_TITLE, header, content);
    }

    public static Optional<ButtonType> showAlert(Alert.AlertType type, String title, String header, String content)
    {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        return alert.showAndWait();
    }
}
