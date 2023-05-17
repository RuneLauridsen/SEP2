package booking.client.core;

import booking.client.model.ClientModel;
import booking.client.view.CoordinatorGUI.CoordinatorHomeScreen;
import booking.client.view.CoordinatorGUI.CoordinatorHomeScreenViewModel;
import booking.client.view.userGUI.UserHomeScreen;
import booking.client.view.userGUI.UserBookRoomViewModel;
import booking.client.view.userGUI.UserHomeScreenViewModel;
import booking.shared.objects.Room;
import booking.shared.objects.User;
import booking.client.view.CoordinatorGUI.AddRoom;
import booking.client.view.CoordinatorGUI.AddRoomViewModel;
import booking.client.view.roomInfo.RoomInfo;
import booking.client.view.roomInfo.RoomInfoViewModel;
import booking.client.view.userGUI.UserBookRoom;
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
    private final ClientModel model;

    public ViewHandler(Stage primaryStage, ViewModelFactory viewModelFactory, ClientModel model)
    {
        this.primaryStage = primaryStage;
        this.viewModelFactory = viewModelFactory;
        this.model = model;
    }

    public void showLogin()
    {
        try
        {
            Scene scene = null;
            FXMLLoader loader = new FXMLLoader();
            Parent root = null;

            loader.setLocation(getClass().getResource("../view/userGUI/Login.fxml"));
            root = loader.load();

            UserBookRoom view = loader.getController();
            view.init(viewModelFactory.getUserBookRoomViewModel(this, model));

            scene = new Scene(root, 600, 400);
            primaryStage.setScene(scene);
            primaryStage.show();
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
        }
    }

    public void showUserBookRoom()
    {
        try
        {
            Scene scene = null;
            FXMLLoader loader = new FXMLLoader();
            Parent root = null;

            loader.setLocation(getClass().getResource("../view/userGUI/UserBookRoom.fxml"));
            root = loader.load();

            UserBookRoomViewModel viewModel = viewModelFactory.getUserBookRoomViewModel(this,  model);

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

    public void showCoordinatorHomeScreen(User user){
        try
        {
            Scene scene = null;
            FXMLLoader loader = new FXMLLoader();
            Parent root = null;

            loader.setLocation(getClass().getResource("../view/coordinatorGUI/CoordinatorHomeScreen.fxml"));
            root = loader.load();

            CoordinatorHomeScreenViewModel viewModel = viewModelFactory.getCoordinatorHomescreenViewModel(this,  model);

            CoordinatorHomeScreen view = loader.getController();
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

    public void showUserHomeScreen(User user)
    {
        try
        {
            Scene scene = null;
            FXMLLoader loader = new FXMLLoader();
            Parent root = null;

            loader.setLocation(getClass().getResource("../view/userGUI/UserHomeScreen.fxml"));
            root = loader.load();

            UserHomeScreenViewModel viewModel = viewModelFactory.getUserHomeScreenViewModel(this,  model);
            viewModel.refreshActiveBookings();

            UserHomeScreen view = loader.getController();
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

    public void showAddRoom(){
        try
        {
            Scene scene = null;
            FXMLLoader loader = new FXMLLoader();
            Parent root = null;

            loader.setLocation(getClass().getResource("../view/CoordinatorGUI/AddRoom.fxml"));
            root = loader.load();


            //TODO Bruger ikke viewModelFactory da der skal åbnes en ny side... Måske er der en anden løsning
            AddRoomViewModel viewModel = new AddRoomViewModel(this, model);

            AddRoom view = loader.getController();
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

    public void showRoomInfo(Room room)
    {
        try
        {
            Scene scene = null;
            FXMLLoader loader = new FXMLLoader();
            Parent root = null;

            loader.setLocation(getClass().getResource("../view/roomInfo/RoomInfo.fxml"));
            root = loader.load();


            //TODO Bruger ikke viewModelFactory da der skal åbnes en ny side... Måske er der en anden løsning
            //RoomInfoViewModel viewModel = viewModelFactory.getRoomInfoViewModel(this, persistence, room);
            RoomInfoViewModel  viewModel = new RoomInfoViewModel(this, model,room);

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
