package booking.shared.socketMessages;

import booking.shared.objects.Room;

public class DeleteRoomRequest extends Request
{
  private final Room room;

  public DeleteRoomRequest(Room room)
  {
    this.room = room;
  }

  public Room getRoom()
  {
    return room;
  }
}
