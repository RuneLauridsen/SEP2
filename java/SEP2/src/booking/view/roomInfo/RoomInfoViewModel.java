package booking.view.roomInfo;

import booking.ViewHandler;
import booking.core.Room;
import booking.database.Persistence;

public class RoomInfoViewModel
{
  private Room room;
  public RoomInfoViewModel(ViewHandler viewHandler, Persistence persistence, Room room)
  {
    this.room = room;
  }

  public Room getRoom(){
    return room;
  }
}
