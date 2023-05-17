package booking.client.view.CoordinatorGUI;

import booking.client.core.ViewHandler;
import booking.client.model.ClientModel;
import booking.shared.objects.Room;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;

public class CoordinatorHomeScreenViewModel
{
  ViewHandler viewHandler;
  ClientModel persistence;
  public CoordinatorHomeScreenViewModel(ViewHandler viewHandler, ClientModel persistence)
  {
    this.viewHandler = viewHandler;
    this.persistence =persistence;
  }

  public ObservableList<Room> getAllRooms(){
    ObservableList<Room> rooms = FXCollections.observableArrayList();
    rooms.addAll(persistence.getRooms());
   return rooms;
  }


  public void changeToAddRoom(){
    viewHandler.showAddRoom();
  }

  public String isAvailable(Room room)
  {
    if (persistence.isAvailable(room))
      return "Available";
    else
      return "occupied";

  }

  public void ChangeToSearch(String name)
  {
    viewHandler.showRoomInfo(persistence.getRoom(name));
  }

  public void changeToEditRoom(Room room)
  {
    viewHandler.showEditRoom(room);
  }
}
