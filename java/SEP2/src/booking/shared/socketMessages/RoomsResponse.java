package booking.shared.socketMessages;

import booking.shared.objects.Room;

import java.util.List;

public class RoomsResponse extends Response
{
  private final List<Room> rooms;

  public RoomsResponse(List<Room> rooms)
  {
    this.rooms = rooms;
  }

  public List<Room> getRooms()
  {
    return rooms;
  }
}
