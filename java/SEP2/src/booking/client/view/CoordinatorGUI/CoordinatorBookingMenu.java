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
    public TableView<Booking> tvBookings;
    public TableColumn<Booking, Room> colRoom;
    public TableColumn<Booking, RoomType> colType;
    public TableColumn<Booking, LocalDate> colDate;
    public TableColumn<Booking, LocalTime> colFrom;
    public TableColumn<Booking, LocalTime> colTo;
    public TableColumn<Booking, UserGroup> colCourse;
    public TableColumn<Booking, Booking> colDelete;
    @FXML
    private Button btnBookRoom;

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
        tvBookings.setRowFactory(tv -> {
            return new TableRow<Booking>()
            {
                @Override
                protected void updateItem(Booking booking, boolean empty)
                {
                    super.updateItem(booking, empty);
                    if (booking != null)
                    {
                        if (booking.getRoom().getUserColor() != 0)
                        {
                            Color color = ArgbIntConverter.intToColor(booking.getRoom().getUserColor());
                            setBackground(new Background(new BackgroundFill(color,
                                                                            CornerRadii.EMPTY, Insets.EMPTY)));
                            //setStyle("-fx-background-color: rgb("+color.getRed()+","+color.getGreen()+","+color.getBlue()+");");
                        }
                    }

                }
            };
        });

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
