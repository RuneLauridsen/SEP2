package booking.client.view.CoordinatorGUI;

import booking.client.viewModel.coordinatorGUIVM.CoordinatorBookingMenuViewModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import booking.client.model.ArgbIntConverter;
import booking.client.view.shared.ButtonTableCell;
import booking.shared.objects.Booking;
import booking.shared.objects.Room;
import booking.shared.objects.RoomType;
import booking.shared.objects.UserGroup;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Insets;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.time.LocalDate;
import java.time.LocalTime;

public class CoordinatorBookingMenu
{
    @FXML private TableView<Booking> tvBookings;
    @FXML private TableColumn<Booking, Room> colRoom;
    @FXML private TableColumn<Booking, RoomType> colType;
    @FXML private TableColumn<Booking, LocalDate> colDate;
    @FXML private TableColumn<Booking, LocalTime> colFrom;
    @FXML private TableColumn<Booking, LocalTime> colTo;
    @FXML private TableColumn<Booking, UserGroup> colCourse;
    @FXML private TableColumn<Booking, Booking> colDelete;
    @FXML private Button btnBookRoom;

    @FXML private Button btnInsertFile;

    @FXML private Button btnCancel;

    @FXML private Button btnConfirm;

    @FXML private Label lblFileName;

    @FXML private ListView<CoordinatorBookingMenu> lvBookings;

    @FXML private VBox VBoxFile;

    private CoordinatorBookingMenuViewModel viewModel;

    public void init(CoordinatorBookingMenuViewModel viewModel)
    {
        this.viewModel = viewModel;
        tvBookings.setRowFactory(tv -> new TableRow<>());

        tvBookings.setItems(viewModel.getBookings());

        colRoom.setCellValueFactory(p -> new SimpleObjectProperty<>(p.getValue().getRoom()));
        colDate.setCellValueFactory(p -> new SimpleObjectProperty<>(p.getValue().getInterval().getDate()));
        colFrom.setCellValueFactory(p -> new SimpleObjectProperty<>(p.getValue().getInterval().getStart()));
        colTo.setCellValueFactory(p -> new SimpleObjectProperty<>(p.getValue().getInterval().getEnd()));

        colDelete.setCellValueFactory(p -> new SimpleObjectProperty<>(p.getValue()));
        colDelete.setCellFactory(p -> new ButtonTableCell<>("❌", viewModel::cancelBooking));
    }

    @FXML
    private void btnBookRoomClicked(ActionEvent event)
    {
        viewModel.bookRoomAction();
    }

    @FXML
    public void btnInsertFileClicked(ActionEvent actionEvent)
    {
        VBoxFile.setVisible(true);
        viewModel.insertFileAction();
    }

    @FXML
    public void btnCancelClicked(ActionEvent actionEvent)
    {
        viewModel.cancelAction();
    }

    @FXML
    public void btnConfirmClicked(ActionEvent actionEvent)
    {
        viewModel.confirmAction();
    }

    public void tableviewClicked(MouseEvent mouseEvent)
    {
        viewModel.ChangeToSearch(tvBookings.getSelectionModel().getSelectedItem().getRoom().getName());
    }
}
