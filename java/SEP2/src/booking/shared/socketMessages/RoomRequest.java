package booking.shared.socketMessages;

public class RoomRequest extends Request
{
  private final String roomName;

  public RoomRequest(String roomName)
  {
    this.roomName = roomName;
  }

  public String getRoomName()
  {
    return roomName;
  }
}
