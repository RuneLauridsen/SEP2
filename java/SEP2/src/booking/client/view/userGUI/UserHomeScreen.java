package booking.client.view.userGUI;

import booking.client.view.shared.ButtonTableCell;
import booking.client.viewModel.userGUIVM.UserHomeScreenViewModel;
import booking.shared.objects.Booking;
import booking.shared.objects.Room;
import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

import java.time.LocalDate;
import java.time.LocalTime;

public class UserHomeScreen
{
    @FXML private Button btnSearch;
    @FXML private TextField txtSearch;
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

        viewModel.getActiveBookings().addListener(this::onActiveBookingsChanged);
        onActiveBookingsChanged(null);

        lblName.textProperty().bind(Bindings.concat("Hello ").concat(viewModel.usernameProperty()));
        tblActiveBookings.setItems(viewModel.getActiveBookings());

        colRoom.setCellValueFactory(p -> new SimpleObjectProperty<>(p.getValue().getRoom()));
        colDate.setCellValueFactory(p -> new SimpleObjectProperty<>(p.getValue().getInterval().getDate()));
        colFrom.setCellValueFactory(p -> new SimpleObjectProperty<>(p.getValue().getInterval().getStart()));
        colTo.setCellValueFactory(p -> new SimpleObjectProperty<>(p.getValue().getInterval().getEnd()));

        colCancel.setCellValueFactory(p -> new SimpleObjectProperty<>(p.getValue()));
        colCancel.setCellFactory(p -> new ButtonTableCell<>("❌", viewModel::cancelBooking));

        txtSearch.textProperty().bindBidirectional(viewModel.getSearchProperty());
        btnSearch.disableProperty().bindBidirectional(viewModel.searchDisable());
    }

    private void onActiveBookingsChanged(Observable observable)
    {
        if (viewModel.getActiveBookings().size() > 0)
        {
            tblActiveBookings.setVisible(true);
            tblActiveBookings.setManaged(true);
            lblNoBookings.setVisible(false);
            lblNoBookings.setManaged(false);
        }
        else
        {
            tblActiveBookings.setVisible(false);
            tblActiveBookings.setManaged(false);
            lblNoBookings.setVisible(true);
            lblNoBookings.setManaged(true);
        }
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
