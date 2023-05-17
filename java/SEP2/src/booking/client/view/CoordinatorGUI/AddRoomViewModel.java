package booking.client.view.CoordinatorGUI;

import booking.client.core.ViewHandler;
import booking.client.model.ClientModel;
import booking.shared.objects.RoomType;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class AddRoomViewModel
{
  ViewHandler viewHandler;
  ClientModel persistence;
  public AddRoomViewModel(ViewHandler viewHandler, ClientModel persistence)
  {
    this.viewHandler = viewHandler;
    this.persistence = persistence;
  }

  public void createRoom(String name, RoomType type, int maxComf, int maxSafety, int size, String comment, boolean isDouble, String doubleName){
    persistence.createRoom(name,type,maxComf,maxSafety,size,comment,isDouble,doubleName);
  }

  public ObservableList<RoomType> getRoomTypes()
  {
    ObservableList<RoomType> types = FXCollections.observableArrayList();
    //TODO get list of room types
    types.add(new RoomType(1,"Grupperum"));
    return types;
  }
}
