package booking.view.roomInfo;

import javafx.scene.control.Label;

public class RoomInfo
{
  public Label lblType;
  public Label lblCapacity;
  public Label lblStatus;
  public Label lblName;

  public void init(RoomInfoViewModel viewModel){
    lblName.textProperty().set(viewModel.getRoom().getName());
    lblType.textProperty().set(viewModel.getRoom().getType().getName());
    lblCapacity.textProperty().set(String.valueOf(viewModel.getRoom().getComfortCapacity()));
    //Todo julie show status and bookings
  }
}
