package booking.view.roomInfo;

import booking.core.BookingInterval;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

public class RoomInfo
{
  public Label lblType;
  public Label lblCapacity;
  public Label lblStatus;
  public Label lblName;
  public ListView<BookingInterval> lvRoomBookings;

  public void init(RoomInfoViewModel viewModel){
    lblName.textProperty().set(viewModel.getRoom().getName());
    lblType.textProperty().set(viewModel.getRoom().getType().getName());
    lblCapacity.textProperty().set(String.valueOf(viewModel.getRoom().getComfortCapacity()));
    lblStatus.textProperty().set(viewModel.isAvailable());
    lvRoomBookings.setItems(viewModel.getBookings());
    //Todo Julie show bookings
  }
}
