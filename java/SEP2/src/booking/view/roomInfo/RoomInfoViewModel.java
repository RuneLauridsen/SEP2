package booking.view.roomInfo;

import booking.ViewHandler;
import booking.core.BookingInterval;
import booking.core.Room;
import booking.database.Persistence;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class RoomInfoViewModel
{
  private Room room;
  private Persistence persistence;
  private ObservableList<BookingInterval> bookings;
  public RoomInfoViewModel(ViewHandler viewHandler, Persistence persistence, Room room)
  {
    this.room = room;
    this.persistence = persistence;
    bookings = FXCollections.observableArrayList();

  }

  public Room getRoom(){
    return room;
  }

  public ObservableList<BookingInterval> getBookings (){
    bookings.clear();
    bookings.addAll(persistence.getBookingsFromRoomName(room.getName()));
    return bookings;
  }

  public String isAvailable(){
    if (persistence.isAvailable(room.getName()))
      return "Available";
    else
      return "Occupied";
  }
}
