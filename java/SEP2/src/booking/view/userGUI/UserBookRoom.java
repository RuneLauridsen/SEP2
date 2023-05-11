package booking.view.userGUI;

import booking.core.Room;
import booking.view.roomList.RoomListCell;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

import java.util.List;

public class UserBookRoom
{
    @FXML private DatePicker dpDate;
    @FXML private ComboBox<String> cbbFromTime;
    @FXML private ComboBox<String> cbbToTime;
    @FXML private TextField txtMinCap;
    @FXML private TextField txtMaxCap;
    @FXML private ComboBox<Character> cbbBuilding;
    @FXML private ComboBox<Integer> cbbFloor;
    @FXML private Label lblError;
    @FXML private Button btnSearch;
    @FXML private VBox vbAvailableRooms;
    @FXML private Label lblNoRooms;
    @FXML private ListView<Room> lvRooms;

    private UserBookRoomViewModel viewModel;

    public void init(UserBookRoomViewModel viewModel)
    {
        this.viewModel = viewModel;

        cbbFromTime.setItems(viewModel.getTimeIntervals());
        cbbToTime.setItems(viewModel.getTimeIntervals());
        cbbFloor.setItems(viewModel.getFloors());
        cbbBuilding.setItems(viewModel.getBuildings());

        dpDate.valueProperty().bindBidirectional(viewModel.selectedDateProperty());
        cbbFromTime.valueProperty().bindBidirectional(viewModel.selectedFromTimeProperty());
        cbbToTime.valueProperty().bindBidirectional(viewModel.selectedToTimeProperty());
        cbbBuilding.valueProperty().bindBidirectional(viewModel.selectedBuildingProperty());
        cbbFloor.valueProperty().bindBidirectional(viewModel.selectedFloorProperty());

        lvRooms.setCellFactory(listView -> new RoomListCell("Book", viewModel::bookRoom));
        lvRooms.setItems(viewModel.getRoomList());
    }

    public void btnSearchClicked(MouseEvent e)
    {
        lvRooms.getItems().clear();
        viewModel.showAvailablerooms();

        if (viewModel.getRoomList().size() > 0)
        {
            vbAvailableRooms.setManaged(true);
            lblNoRooms.setManaged(false);
        }
        else
        {
            vbAvailableRooms.setManaged(false);
            lblNoRooms.setManaged(true);
        }
        vbAvailableRooms.setVisible(true);
    }
}


