package booking.client.view.CoordinatorGUI;

import booking.client.view.userGUI.ButtonTableCell;
import booking.client.viewModel.coordinatorGUIVM.CoordinatorHomeScreenViewModel;
import booking.shared.objects.Room;
import booking.shared.objects.RoomType;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
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
    public Label lblName;
    public TableView<Room> tvtRooms;
    public TableColumn<Room, String> tcolName;
    public TableColumn<Room, RoomType> tcolType;
    public TableColumn<Room, String> tcolStatus;
    public TableColumn<Room, Room> tcolAlter;

    CoordinatorHomeScreenViewModel viewModel;

    public void init(CoordinatorHomeScreenViewModel viewModel)
    {
        this.viewModel = viewModel;

        lblName.textProperty().bind(Bindings.concat("Hello ").concat(viewModel.usernameProperty()));

        tvtRooms.setRowFactory(tv -> {
            return new TableRow<Room>()
            {
                @Override
                protected void updateItem(Room room, boolean empty)
                {
                    super.updateItem(room, empty);

                    if (room != null && room.getUserColor() != 0)
                    {
                        Color color = ArgbIntConverter.intToColor(room.getUserColor());
                        setBackground(new Background(new BackgroundFill(color,
                                                                        CornerRadii.EMPTY, Insets.EMPTY)));
                        //setStyle("-fx-background-color: rgb("+color.getRed()+","+color.getGreen()+","+color.getBlue()+");");
                    }
                }
            };
        });

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
