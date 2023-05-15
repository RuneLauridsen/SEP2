package booking.shared.socketMessages;

import booking.shared.objects.Room;

public class RoomResponse extends Response
{
  private Room room;

  public RoomResponse(Room room)
  {
    this.room = room;
  }

  public Room getRoom()
  {
    return room;
  }
}
