package booking.client.view.userGUI;

import booking.client.viewModel.userGUIVM.UserHomeScreenViewModel;
import booking.shared.objects.Booking;
import booking.shared.objects.Room;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

import java.time.LocalDate;
import java.time.LocalTime;

public class UserHomeScreen
{
    public TextField txtSearch;
    @FXML private TableColumn<Booking, Room> colRoom;
    @FXML private TableColumn<Booking, LocalDate> colDate;
    @FXML private TableColumn<Booking, LocalTime> colFrom;
    @FXML private TableColumn<Booking, LocalTime> colTo;
    @FXML private TableColumn<Booking, Booking> colCancel;
    @FXML private TableView<Booking> tblActiveBookings;
    @FXML private VBox mainVBox;
    @FXML private Label lblNoBookings;
    @FXML private Label lblName;

    private UserHomeScreenViewModel viewModel;

    //TODO gør sådan at listen bliver opdateret
    public void init(UserHomeScreenViewModel viewModel)
    {
        this.viewModel = viewModel;

        if (viewModel.getActiveBookings().size() > 0)
        {
            tblActiveBookings.setManaged(true);
            lblNoBookings.setManaged(false);
        }
        else
        {
            tblActiveBookings.setManaged(false);
            lblNoBookings.setManaged(true);
        }

        lblName.textProperty().bind(Bindings.concat("Hello ").concat(viewModel.usernameProperty()));
        tblActiveBookings.setItems(viewModel.getActiveBookings());

        colRoom.setCellValueFactory(p -> new SimpleObjectProperty<>(p.getValue().getRoom()));
        colDate.setCellValueFactory(p -> new SimpleObjectProperty<>(p.getValue().getInterval().getDate()));
        colFrom.setCellValueFactory(p -> new SimpleObjectProperty<>(p.getValue().getInterval().getStart()));
        colTo.setCellValueFactory(p -> new SimpleObjectProperty<>(p.getValue().getInterval().getEnd()));

        colCancel.setCellValueFactory(p -> new SimpleObjectProperty<>(p.getValue()));
        colCancel.setCellFactory(p -> new ButtonTableCell<>("❌", viewModel::cancelBooking));

        txtSearch.textProperty().bindBidirectional(viewModel.getSearchProperty());
    }

    public void findAvailableRoomClick(ActionEvent actionEvent)
    {
        viewModel.ChangeToBooking();
    }

    public void searchRoomClick(ActionEvent actionEvent)
    {
        viewModel.ChangeToSearch(txtSearch.textProperty().get());
        txtSearch.textProperty().set(null);
    }

    public void tableViewClicked(MouseEvent mouseEvent)
    {
        viewModel.ChangeToSearch(tblActiveBookings.getSelectionModel().getSelectedItem().getRoom().getName());
    }
}
