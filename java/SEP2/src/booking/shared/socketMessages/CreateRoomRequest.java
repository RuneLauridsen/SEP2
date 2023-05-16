package booking.shared.socketMessages;

import booking.shared.objects.BookingInterval;
import booking.shared.objects.Room;

public class CreateRoomRequest extends Request
{
  private final Room room;

    public CreateRoomRequest(Room room)
  {
    this.room = room;
  }

  public Room getRoom()
  {
    return room;
  }

}
