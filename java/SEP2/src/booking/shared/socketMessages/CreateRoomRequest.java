package booking.shared.socketMessages;

import booking.shared.objects.RoomType;

public class CreateRoomRequest extends Request
{

  private String name;
  private RoomType type;
  private int maxComf;
  private int maxSafety;
  private int size;
  private String comment;
  private boolean isDouble;
  private String doubleName;

  public CreateRoomRequest(String name, RoomType type, int maxComf, int maxSafety, int size, String comment,
      boolean isDouble, String doubleName)
  {
    this.name = name;
    this.type = type;
    this.maxComf = maxComf;
    this.maxSafety = maxSafety;
    this.size = size;
    this.comment = comment;
    this.isDouble = isDouble;
    this.doubleName = doubleName;
  }

  public String getName()
  {
    return name;
  }

  public RoomType getType()
  {
    return type;
  }

  public int getMaxComf()
  {
    return maxComf;
  }

  public int getMaxSafety()
  {
    return maxSafety;
  }

  public int getSize()
  {
    return size;
  }

  public String getComment()
  {
    return comment;
  }

  public boolean isDouble()
  {
    return isDouble;
  }

  public String getDoubleName()
  {
    return doubleName;
  }
}
