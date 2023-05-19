package booking.client.view.CoordinatorGUI;

import booking.client.core.ViewHandler;
import booking.client.model.ClientModel;
import booking.shared.objects.Room;
import booking.shared.objects.RoomType;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class EditRoomViewModel
{
  private final ViewHandler viewHandler;
  private final ClientModel model;
  private final Room room;
  public EditRoomViewModel(ViewHandler viewHandler, ClientModel model, Room room )
  {
    this.viewHandler = viewHandler;
    this.model = model;
    this.room = room;
  }

  public Room getRoom()
  {
    return room;
  }
  public ObservableList<RoomType> getRoomTypes()
  {
    ObservableList<RoomType> types = FXCollections.observableArrayList(model.getRoomTypes());
    return types;
  }

  public void UpdateRoom(String name, RoomType type, int maxComf, int maxSafety, int size, String comment, boolean isDouble, String doubleName, String personalComment, String color){
    room.setName(name);
    room.setType(type);
    room.setSize(size);
    room.setComfortCapacity(maxComf);
    room.setFireCapacity(maxSafety);
    room.setComment(comment);
    room.setUserComment(personalComment);
    //if ()
    //room.setUserColor();
    model.updateRoom(room);
  }
}
