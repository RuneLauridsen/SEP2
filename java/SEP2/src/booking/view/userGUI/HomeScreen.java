package booking.view.userGUI;

import booking.ViewHandler;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;

public class HomeScreen
{
    public TableView tblActiveBookings;
    public VBox mainVBox;
    public Label lblNoBookings;

    private HomeScreenViewModel viewModel;

    public void init(HomeScreenViewModel viewModel)
    {
        this.viewModel = viewModel;

        //Only remove if no bookings
        mainVBox.getChildren().remove(tblActiveBookings);
        mainVBox.getChildren().add(4, tblActiveBookings);

        //Only remove if active bookings
        mainVBox.getChildren().remove(lblNoBookings);
        mainVBox.getChildren().add(4, lblNoBookings);
    }
}
