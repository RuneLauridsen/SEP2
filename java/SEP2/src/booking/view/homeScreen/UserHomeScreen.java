package booking.view.homeScreen;

import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;

public class UserHomeScreen
{
  public TableView tblActiveBookings;
  public VBox mainVBox;
  public Label lblNoBookings;

  public void init()
  {
    //Only remove if no bookings
    mainVBox.getChildren().remove(tblActiveBookings);
    mainVBox.getChildren().add(4,tblActiveBookings);

    //Only remove if active bookings
    mainVBox.getChildren().remove(lblNoBookings);
    mainVBox.getChildren().add(4,lblNoBookings);


  }
}
