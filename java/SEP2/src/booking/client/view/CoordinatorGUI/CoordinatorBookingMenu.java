package booking.client.view.CoordinatorGUI;

import booking.client.view.shared.ButtonTableCell;
import booking.client.viewModel.coordinatorGUIVM.CoordinatorBookingMenuViewModel;
import booking.shared.objects.Booking;
import booking.shared.objects.Room;
import booking.shared.objects.RoomType;
import booking.shared.objects.UserGroup;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

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
        colCourse.setCellValueFactory(p -> new SimpleObjectProperty<>(p.getValue().getUserGroup()));
        colType.setCellValueFactory(p -> new SimpleObjectProperty<>(p.getValue().getRoom().getType()));

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
        viewModel.insertFileAction();
    }

    public void tableviewClicked(MouseEvent mouseEvent)
    {
        viewModel.ChangeToSearch(tvBookings.getSelectionModel().getSelectedItem().getRoom());
    }
}
