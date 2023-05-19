package booking.client.view.CoordinatorGUI;

import booking.client.core.ViewHandler;
import booking.client.model.ArgbIntConverter;
import booking.client.model.ClientModel;
import booking.shared.objects.Room;
import booking.shared.objects.RoomType;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.paint.Color;

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
    return FXCollections.observableArrayList(model.getRoomTypes());
  }

  public ObservableList<String> getColors(){
    ObservableList<String> colors = FXCollections.observableArrayList();
    colors.addAll("Red","Blue","Yellow","Orange", "Green","Purple","Pink","Mint","Green","Gray");
    return colors;
  }

  public void UpdateRoom(String name, RoomType type, int maxComf, int maxSafety, int size, String comment, boolean isDouble, String doubleName, String personalComment, String color){
    room.setName(name);
    room.setType(type);
    room.setSize(size);
    room.setComfortCapacity(maxComf);
    room.setFireCapacity(maxSafety);
    room.setComment(comment);
    room.setUserComment(personalComment);

    if (color != null && !color.isEmpty()){
      int colorInt;
      switch (color){
        case "Red":
          colorInt = ArgbIntConverter.argbToInt(243, 131, 131);
          break;
        case "Blue":
          colorInt = ArgbIntConverter.argbToInt(130,137,243);
          break;
        case "Yellow":
          colorInt = ArgbIntConverter.argbToInt(250,250,100);
          break;
        case "Orange":
          colorInt = ArgbIntConverter.argbToInt(255,178,61);
          break;
        case "Green":
          colorInt = ArgbIntConverter.argbToInt(141,238,127);
          break;
        case "Purple":
          colorInt = ArgbIntConverter.argbToInt(214,142,236);
          break;
        case "Pink":
          colorInt = ArgbIntConverter.argbToInt(255,134,211);
          break;
        case "Mint":
          colorInt = ArgbIntConverter.argbToInt(162,255,255);
          break;
        case "Gray":
        default:
          colorInt = ArgbIntConverter.argbToInt(222,222,222);
          break;
      }
      room.setUserColor(colorInt);
    }

    model.updateRoom(room);
  }

}
