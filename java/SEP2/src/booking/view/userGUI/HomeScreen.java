package booking.view.userGUI;

import booking.ViewHandler;
import booking.core.Booking;
import booking.core.Room;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableObjectValue;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.util.Callback;

import java.awt.print.Book;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Locale;

public class HomeScreen
{
    @FXML private TableColumn<Booking, Room> colRoom;
    @FXML private TableColumn<Booking, LocalDate> colDate;
    @FXML private TableColumn<Booking, LocalTime> colFrom;
    @FXML private TableColumn<Booking, LocalTime> colTo;
    @FXML private TableView<Booking> tblActiveBookings;
    @FXML private VBox mainVBox;
    @FXML private Label lblNoBookings;
    @FXML private Label lblName;

    private HomeScreenViewModel viewModel;

    public void init(HomeScreenViewModel viewModel)
    {
        this.viewModel = viewModel;

        //Only remove if no bookings
        mainVBox.getChildren().remove(tblActiveBookings);
        mainVBox.getChildren().add(4, tblActiveBookings);

        //Only remove if active bookings
        mainVBox.getChildren().remove(lblNoBookings);
        mainVBox.getChildren().add(4, lblNoBookings);

        lblName.textProperty().bind(Bindings.concat("Hello ").concat(viewModel.usernameProperty()));
        tblActiveBookings.setItems(viewModel.getActiveBookings());

        colRoom.setCellValueFactory(p -> new SimpleObjectProperty<>(p.getValue().getRoom()));
        colDate.setCellValueFactory(p -> new SimpleObjectProperty<>(p.getValue().getInterval().getDate()));
        colFrom.setCellValueFactory(p -> new SimpleObjectProperty<>(p.getValue().getInterval().getStart()));
        colTo.setCellValueFactory(p -> new SimpleObjectProperty<>(p.getValue().getInterval().getEnd()));
    }
}