package booking.client.view.CoordinatorGUI;

import booking.client.view.shared.ButtonTableCell;
import booking.client.view.shared.ColoredRoomTableRow;
import booking.client.viewModel.coordinatorGUIVM.CoordinatorHomeScreenViewModel;
import booking.shared.objects.Room;
import booking.shared.objects.RoomType;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import booking.client.model.ArgbIntConverter;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;

public class CoordinatorHomeScreen
{
    @FXML private Label lblName;
    @FXML private TableView<Room> tvtRooms;
    @FXML private TableColumn<Room, String> tcolName;
    @FXML private TableColumn<Room, RoomType> tcolType;
    @FXML private TableColumn<Room, String> tcolStatus;
    @FXML private TableColumn<Room, Room> tcolAlter;

    CoordinatorHomeScreenViewModel viewModel;

    public void init(CoordinatorHomeScreenViewModel viewModel)
    {
        this.viewModel = viewModel;

        lblName.textProperty().bind(Bindings.concat("Hello ").concat(viewModel.usernameProperty()));

        tvtRooms.setRowFactory(tv -> new ColoredRoomTableRow());

        tvtRooms.setItems(viewModel.getAllRooms());
        tcolName.setCellValueFactory(p -> new SimpleObjectProperty<>(p.getValue().getName()));
        tcolType.setCellValueFactory(p -> new SimpleObjectProperty<>(p.getValue().getType()));
        tcolStatus.setCellValueFactory(p -> new SimpleStringProperty(viewModel.isAvailable(p.getValue())));

        tcolAlter.setCellValueFactory(p -> new SimpleObjectProperty<>(p.getValue()));
        tcolAlter.setCellFactory(p -> new ButtonTableCell<>("Edit", viewModel::changeToEditRoom));
    }

    public void AddRoomClicked(MouseEvent mouseEvent)
    {
        viewModel.changeToAddRoom();
    }

    public void CoursesClicked(MouseEvent mouseEvent)
    {
    }

    public void BookingsClicked(MouseEvent mouseEvent)
    {
        viewModel.changeToBooking();
    }

    public void tableViewClicked(MouseEvent mouseEvent)
    {
        viewModel.changeToSearch(tvtRooms.getSelectionModel().getSelectedItem().getName());
    }
}
