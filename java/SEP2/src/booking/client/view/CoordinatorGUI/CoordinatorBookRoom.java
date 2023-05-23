package booking.client.view.CoordinatorGUI;

import booking.client.view.userGUI.RoomListCell;
import booking.shared.objects.Room;
import booking.shared.objects.TimeSlot;
import booking.shared.objects.UserGroup;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

public class CoordinatorBookRoom
{
    public ComboBox<String> cbbCategory;
    @FXML
    private DatePicker dpStartDate;
    @FXML
    private DatePicker dpEndDate;
    @FXML
    private ComboBox<String> cbbFromTime;
    @FXML
    private ComboBox<String> cbbToTime;
    @FXML
    private ComboBox<TimeSlot> cbbPrefixTime;
    @FXML
    private ComboBox<String> cbbDayTime;
    @FXML
    private TextField txtMinCap;
    @FXML
    private TextField txtMaxCap;
    @FXML
    private ComboBox<Character> cbbBuilding;
    @FXML
    private ComboBox<Integer> cbbFloor;
    @FXML
    private ComboBox<UserGroup> cbbCourse;
    @FXML
    private Label lblError;
    @FXML
    private Button btnSearch;
    @FXML
    private VBox vbAvailableRooms;
    @FXML
    private Label lblNoRooms;
    @FXML
    private ListView<Room> lvRooms;

    private CoordinatorBookRoomViewModel viewModel;

    //TODO Farve filtrering
    public void init(CoordinatorBookRoomViewModel viewModel)
    {
        refresh();
        this.viewModel = viewModel;

        cbbFromTime.setItems(viewModel.getTimeIntervals());
        cbbToTime.setItems(viewModel.getTimeIntervals());
        cbbFloor.setItems(viewModel.getFloors());
        cbbBuilding.setItems(viewModel.getBuildings());
        cbbPrefixTime.setItems(viewModel.getPreFixTimes());
        cbbDayTime.setItems(viewModel.getDays());
        cbbCourse.setItems(viewModel.getCourses());
        cbbCategory.setItems(viewModel.getColors());


        dpStartDate.valueProperty().bindBidirectional(viewModel.selectedStartDateProperty());
        dpEndDate.valueProperty().bindBidirectional(viewModel.selectedEndDateProperty());
        cbbFromTime.valueProperty().bindBidirectional(viewModel.selectedFromTimeProperty());
        cbbToTime.valueProperty().bindBidirectional(viewModel.selectedToTimeProperty());
        cbbPrefixTime.valueProperty().bindBidirectional(viewModel.selectedPreFixTimeProperty());
        cbbDayTime.valueProperty().bindBidirectional(viewModel.selectedDayProperty());
        cbbBuilding.valueProperty().bindBidirectional(viewModel.selectedBuildingProperty());
        cbbFloor.valueProperty().bindBidirectional(viewModel.selectedFloorProperty());
        cbbCourse.valueProperty().bindBidirectional(viewModel.selectedCourseProperty());
        cbbCategory.valueProperty().bindBidirectional(viewModel.selectedCategoryProperty());

        lvRooms.setCellFactory(listView -> new RoomListCell("Book", viewModel::bookRoom));
        lvRooms.setItems(viewModel.getRoomList());
    }

    public void btnSearchClicked(MouseEvent event)
    {
        lvRooms.getItems().clear();
        viewModel.showAvailableRooms();

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
    public void refresh()
    {
        dpStartDate.setValue(null);
        dpEndDate.setValue(null);
        cbbFromTime.setValue(null);
        cbbToTime.setValue(null);
        cbbPrefixTime.setValue(null);
        cbbDayTime.setValue(null);
        txtMinCap.clear();
        txtMaxCap.clear();
        cbbBuilding.setValue(null);
        cbbFloor.setValue(null);
        cbbCourse.setValue(null);
        cbbCategory.setValue(null);
    }

    public void listViewClicked(MouseEvent Event)
    {
        viewModel.ChangeToSearch(lvRooms.getSelectionModel().getSelectedItem().getName());
    }

}
