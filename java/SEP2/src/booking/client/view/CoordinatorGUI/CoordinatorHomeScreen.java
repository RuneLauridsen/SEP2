package booking.client.view.CoordinatorGUI;

import booking.client.view.userGUI.ButtonTableCell;
import booking.shared.objects.Room;
import booking.shared.objects.RoomType;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;

public class CoordinatorHomeScreen
{
  public Label lblName;
  public TableView<Room> tvtRooms;
  public TableColumn<Room, String> tcolName;
  public TableColumn<Room, RoomType> tcolType;
  public TableColumn<Room, String> tcolStatus;
  public TableColumn<Room, Room> tcolAlter;
  public TableColumn <Room, Room> tcolDelete ;

  CoordinatorHomeScreenViewModel viewModel;
  public void init(CoordinatorHomeScreenViewModel viewModel){
    this.viewModel = viewModel;


    



    tvtRooms.setItems(viewModel.getAllRooms());
    tcolName.setCellValueFactory(p -> new SimpleObjectProperty<>(p.getValue().getName()));
    tcolType.setCellValueFactory(p -> new SimpleObjectProperty<>(p.getValue().getType()));
    tcolStatus.setCellValueFactory(p -> new SimpleStringProperty(viewModel.isAvailable(p.getValue())));

    tcolAlter.setCellValueFactory(p -> new SimpleObjectProperty<>(p.getValue()));
    tcolDelete.setCellValueFactory(p -> new SimpleObjectProperty<>(p.getValue()));
    tcolAlter.setCellFactory(p -> new ButtonTableCell<>("Edit", viewModel::changeToEditRoom));
    tcolDelete.setCellFactory(p -> new ButtonTableCell<>("❌", viewModel::deleteRoom));
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
  }

  public void tableViewClicked(MouseEvent mouseEvent)
  {
    viewModel.ChangeToSearch(tvtRooms.getSelectionModel().getSelectedItem().getName());
  }
}
