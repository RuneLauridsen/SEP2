package booking.view.userGUI;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import java.util.List;

public class UserBookRoom
{
  public DatePicker dpDate;
  public ComboBox cbbFromTime;
  public ComboBox cbbToTime;
  public TextField txtMinCap;
  public TextField txtMaxCap;
  public ComboBox cbbBuilding;
  public ComboBox cbbFloor;
  public Label lblError;
  public Button btnSearch;
  public VBox vbAvailableRooms;
  public Label lblNoRooms;
  public ListView lvRooms;

  ObservableList<String> timeIntervals = FXCollections.observableArrayList();
  ObservableList<String> buildings = FXCollections.observableArrayList();
  ObservableList<Integer> floors = FXCollections.observableArrayList();
  public void init(UserBookRoomViewModel viewModel){
    timeIntervals.addAll(List.of("7:00","7:15","7:30","7:45","8:00","8:15","8:30","8:45","9:00","9:15","9:30","9:45","10:00","10:15","10:30","10:45","11:00","11:15","11:30","11:45","12:00","12:15","12:30","12:45","13:00","13:15","13:30","13:45","14:00","14:15","14:30","14:45","15:00","15:15","15:30","15:45","16:00"));
    buildings.addAll(List.of("A","B","C"));
    //TODO only 5 floors for normal
    floors.addAll(List.of(1,2,3,4,5,6));

    cbbFromTime.setItems(timeIntervals);
    cbbToTime.setItems(timeIntervals);
    cbbFloor.setItems(floors);
    cbbBuilding.setItems(buildings);

  }
}


