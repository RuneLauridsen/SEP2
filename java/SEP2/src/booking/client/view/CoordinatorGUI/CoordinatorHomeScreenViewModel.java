package booking.client.view.CoordinatorGUI;

import booking.client.core.ViewHandler;
import booking.client.model.ClientModel;
import booking.shared.objects.Booking;
import booking.shared.objects.Room;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.time.LocalDate;
import java.util.List;

public class CoordinatorHomeScreenViewModel
{
  ObservableList<Room> rooms = FXCollections.observableArrayList();
  ViewHandler viewHandler;
  ClientModel model;
  public CoordinatorHomeScreenViewModel(ViewHandler viewHandler, ClientModel model)
  {
    this.viewHandler = viewHandler;
    this.model =model;
  }

  public ObservableList<Room> getAllRooms(){
    rooms.addAll(model.getRooms());
   return rooms;
  }


  public void changeToAddRoom(){
    viewHandler.showAddRoom();
  }

  public String isAvailable(Room room)
  {
    if (model.isAvailable(room))
      return "Available";
    else
      return "occupied";

  }

  public void ChangeToSearch(String name)
  {
    viewHandler.showRoomInfo(model.getRoom(name));
  }

  public void refreshActiveBookings()
  {
    rooms.clear();
    rooms.addAll(model.getRooms());
  }

  public void changeToEditRoom(Room room)
  {
    viewHandler.showEditRoom(room);
  }
}
