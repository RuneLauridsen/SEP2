package booking.client.view.CoordinatorGUI;

import booking.client.view.shared.ColoredRoomListCell;
import booking.client.viewModel.coordinatorGUIVM.CoordinatorBookRoomViewModel;
import booking.client.viewModel.sharedVM.PredefinedColor;
import booking.shared.objects.Room;
import booking.shared.objects.TimeSlot;
import booking.shared.objects.UserGroup;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class CoordinatorBookRoom
{
    public CheckBox cbPrefix;
    public HBox hbPrefix;
    public VBox vbTime;
    @FXML
    private ComboBox<PredefinedColor> cbbCategory;
    @FXML
    private DatePicker dpStartDate;
    @FXML
    private ComboBox<String> cbbFromTime;
    @FXML
    private ComboBox<String> cbbToTime;
    @FXML
    private ComboBox<TimeSlot> cbbPrefixTime;
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
    private VBox vbAvailableRooms;
    @FXML
    private Label lblNoRooms;
    @FXML
    private ListView<Room> lvRooms;

    private CoordinatorBookRoomViewModel viewModel;

    public void init(CoordinatorBookRoomViewModel viewModel)
    {
        this.viewModel = viewModel;

        cbbFromTime.setItems(viewModel.getTimeIntervals());
        cbbToTime.setItems(viewModel.getTimeIntervals());
        cbbFloor.setItems(viewModel.getFloors());
        cbbBuilding.setItems(viewModel.getBuildings());
        cbbPrefixTime.setItems(viewModel.getPreFixTimes());
        cbbCourse.setItems(viewModel.getCourses());
        cbbCategory.setItems(viewModel.getColors());


        dpStartDate.valueProperty().bindBidirectional(viewModel.selectedStartDateProperty());
        cbbFromTime.valueProperty().bindBidirectional(viewModel.selectedFromTimeProperty());
        cbbToTime.valueProperty().bindBidirectional(viewModel.selectedToTimeProperty());
        cbbPrefixTime.valueProperty().bindBidirectional(viewModel.selectedPreFixTimeProperty());
        cbbBuilding.valueProperty().bindBidirectional(viewModel.selectedBuildingProperty());
        cbbFloor.valueProperty().bindBidirectional(viewModel.selectedFloorProperty());
        cbbCourse.valueProperty().bindBidirectional(viewModel.selectedCourseProperty());
        cbbCategory.valueProperty().bindBidirectional(viewModel.selectedCategoryProperty());
        cbPrefix.selectedProperty().bindBidirectional(viewModel.prefixCheckBoxProperty());
        txtMaxCap.textProperty().bindBidirectional(viewModel.selectedMaxCapProperty());
        txtMinCap.textProperty().bindBidirectional(viewModel.selectedMinCapProperty());

        lvRooms.setCellFactory(listView -> new ColoredRoomListCell("Book", viewModel::bookRoom));
        lvRooms.setItems(viewModel.getRoomList());

        refresh();
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
        cbPrefix.setSelected(false);
        vbTime.setDisable(cbPrefix.isSelected());
        hbPrefix.setVisible(cbPrefix.isSelected());

        dpStartDate.setValue(null);
        cbbFromTime.setValue(null);
        cbbToTime.setValue(null);
        cbbPrefixTime.setValue(null);
        txtMinCap.clear();
        txtMaxCap.clear();
        cbbBuilding.setValue(null);
        cbbFloor.setValue(null);
        cbbCourse.setValue(null);
        cbbCategory.setValue(null);
    }

    public void listViewClicked(MouseEvent Event)
    {
        viewModel.changeToSearch(lvRooms.getSelectionModel().getSelectedItem());
    }

    public void checkboxClicked(ActionEvent actionEvent)
    {
        vbTime.setDisable(cbPrefix.isSelected());
        hbPrefix.setVisible(cbPrefix.isSelected());
    }
}
