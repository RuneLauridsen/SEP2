package booking.view.CoordinatorGUI;

import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;

public class HomeScreen
{
  public Label lblName;
  public TableView tvtRooms;
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
