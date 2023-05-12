package booking.view.roomInfo;

import booking.ViewHandler;
import booking.core.Room;
import booking.database.Persistence;

public class RoomInfoViewModel
{
  private Room room;
  Persistence persistence;
  public RoomInfoViewModel(ViewHandler viewHandler, Persistence persistence, Room room)
  {
    this.room = room;
    this.persistence = persistence;
  }

  public Room getRoom(){
    return room;
  }

  public String isAvailable(){
    if (persistence.isAvailable(room.getName()))
      return "Available";
    else
      return "Occupied";
  }
}
