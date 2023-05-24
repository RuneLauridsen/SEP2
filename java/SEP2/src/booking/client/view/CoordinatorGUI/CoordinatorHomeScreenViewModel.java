package booking.client.view.CoordinatorGUI;

import booking.client.core.ViewHandler;
import booking.client.model.ClientModel;
import booking.client.model.ClientModelException;
import booking.shared.objects.Room;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class CoordinatorHomeScreenViewModel
{
  private final StringProperty username;
  ObservableList<Room> rooms = FXCollections.observableArrayList();
  ViewHandler viewHandler;
  ClientModel model;

  public CoordinatorHomeScreenViewModel(ViewHandler viewHandler, ClientModel model)
  {
    username = new SimpleStringProperty();
    username.set(model.getUser().getName());
    this.viewHandler = viewHandler;
    this.model = model;
  }

  public StringProperty usernameProperty()
  {
    return username;
  }

  public ObservableList<Room> getAllRooms()
  {
    try
    {
      rooms.addAll(model.getRooms());
    }
    catch (ClientModelException e)
    {
      viewHandler.showErrorDialog(e.getMessage());
    }

    return rooms;
  }

  public void changeToAddRoom()
  {
    viewHandler.showAddRoom();
  }

  public String isAvailable(Room room)
  {
    try
    {
      if (model.isAvailable(room))
      {
        return "Available";
      }
      else
      {
        return "Cccupied";
      }
    }
    catch (ClientModelException e)
    {
      viewHandler.showErrorDialog(e.getMessage());
      return "";
    }
  }

  public void changeToSearch(String name)
  {
    try
    {
      viewHandler.showRoomInfo(model.getRoom(name));
    }
    catch (ClientModelException e)
    {
      viewHandler.showErrorDialog(e.getMessage());
    }
  }

  public void refreshRooms()
  {
    // TODO(rune): Hvorfor både refreshRooms og getAllRooms?

    rooms.clear();

    try
    {
      rooms.addAll(model.getRooms());
    }
    catch (ClientModelException e)
    {
      viewHandler.showErrorDialog(e.getMessage());
    }
  }

  public void changeToEditRoom(Room room)
  {
    viewHandler.showEditRoom(room);
  }

  public void changeToBooking()
  {
    viewHandler.showCoordinatorBookingMenu();
  }
}
