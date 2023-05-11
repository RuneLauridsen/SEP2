package booking;

import booking.core.Room;
import booking.core.User;
import booking.view.roomInfo.RoomInfo;
import booking.view.roomInfo.RoomInfoViewModel;
import booking.view.userGUI.*;
import booking.database.Persistence;
import booking.view.userGUI.UserBookRoom;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Optional;

// TODO(rune): Meget gentaget kode i showXYZ metoder.
//TODO(Julie): Muligvis ændre User persistance til singleton klasser.
public class ViewHandler
{
    private static final String DEFAULT_WINDOW_TITLE = "Bookingsystem";

    private final Stage primaryStage;
    private final ViewModelFactory viewModelFactory;
    private final Persistence persistence;

    public ViewHandler(Stage primaryStage, ViewModelFactory viewModelFactory, Persistence persistence)
    {
        this.primaryStage = primaryStage;
        this.viewModelFactory = viewModelFactory;
        this.persistence = persistence;
    }

    public void showLogin()
    {
        try
        {
            Scene scene = null;
            FXMLLoader loader = new FXMLLoader();
            Parent root = null;

            loader.setLocation(getClass().getResource("view/userGUI/Login.fxml"));
            root = loader.load();

            UserBookRoom view = loader.getController();
            view.init(viewModelFactory.getUserBookRoomViewModel(this, persistence));

            scene = new Scene(root, 600, 400);
            primaryStage.setScene(scene);
            primaryStage.show();
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
        }
    }

    public void showUserBookRoom(User user)
    {
        try
        {
            Scene scene = null;
            FXMLLoader loader = new FXMLLoader();
            Parent root = null;

            loader.setLocation(getClass().getResource("view/userGUI/UserBookRoom.fxml"));
            root = loader.load();

            UserBookRoomViewModel viewModel = viewModelFactory.getUserBookRoomViewModel(this, persistence);
            viewModel.setUser(user);

            UserBookRoom view = loader.getController();
            view.init(viewModel);

            scene = new Scene(root);
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

            loader.setLocation(getClass().getResource("view/userGUI/HomeScreen.fxml"));
            root = loader.load();

            UserHomeScreenViewModel viewModel = viewModelFactory.getUserHomeScreenViewModel(this, persistence);
            viewModel.setUser(user);

            HomeScreen view = loader.getController();
            view.init(viewModel);

            scene = new Scene(root);
            Stage bookingStage = new Stage();
            bookingStage.setScene(scene);
            bookingStage.show();
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
        }
    }
    public void showRoomInfo(Room room)
    {
        try
        {
            Scene scene = null;
            FXMLLoader loader = new FXMLLoader();
            Parent root = null;

            loader.setLocation(getClass().getResource("view/roomInfo/RoomInfo.fxml"));
            root = loader.load();

            RoomInfoViewModel viewModel = viewModelFactory.getRoomInfoViewModel(this, persistence, room);

            RoomInfo view = loader.getController();
            view.init(viewModel);

            scene = new Scene(root);
            Stage infoStage = new Stage();
            infoStage.setScene(scene);
            infoStage.show();
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
        }
    }

    public void showInfo(String text)
    {
        showInfo(text, "");
    }

    public void showInfo(String header, String content)
    {
        showAlert(Alert.AlertType.INFORMATION, DEFAULT_WINDOW_TITLE, header, content);
    }

    public void showWarning(String text)
    {
        showWarning(text, "");
    }

    public void showWarning(String header, String content)
    {
        showAlert(Alert.AlertType.WARNING, DEFAULT_WINDOW_TITLE, header, content);
    }

    public void showError(String text)
    {
        showError(text, "");
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
