package booking.client.view.CoordinatorGUI;

import booking.shared.objects.Room;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;

public class CoordinatorHomeScreen
{
  public Label lblName;
  public TableView<Room> tvtRooms;
  public TableColumn tcolName;
  public TableColumn tcolType;
  public TableColumn tcolStatus;
  public TableColumn tcolBook;
  public TableColumn tcolAlter;

  CoordinatorHomeScreenViewModel viewModel;
  public void init(CoordinatorHomeScreenViewModel viewModel){
    this.viewModel = viewModel;
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
  }
}
