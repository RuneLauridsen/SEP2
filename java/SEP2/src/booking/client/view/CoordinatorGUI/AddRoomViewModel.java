package booking.client.view.CoordinatorGUI;

import booking.client.core.ViewHandler;
import booking.client.model.ClientModel;
import booking.shared.objects.RoomType;

public class AddRoomViewModel
{
  ViewHandler viewHandler;
  public AddRoomViewModel(ViewHandler viewHandler, ClientModel persistence)
  {
    this.viewHandler = viewHandler;
  }

  public void createRoom(String name, RoomType type, int maxComf, int maxSafety, int size, String comment, boolean isDouble, String doubleName){

  }

}
