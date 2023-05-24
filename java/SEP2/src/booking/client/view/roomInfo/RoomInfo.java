package booking.client.view.roomInfo;

import booking.client.viewModel.roomInfoVM.RoomInfoViewModel;
import booking.shared.objects.Booking;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

public class RoomInfo
{
    public Label lblType;
    public Label lblCapacity;
    public Label lblStatus;
    public Label lblName;
    public ListView<Booking> lvRoomBookings;
    public Label lblComment;
    public Label lblNoBookings;

    private RoomInfoViewModel viewModel;

    public void init(RoomInfoViewModel viewModel)
    {
        this.viewModel = viewModel;
        lblName.textProperty().set(viewModel.getRoom().getName());
        lblType.textProperty().set(viewModel.getRoom().getType().getName());
        lblCapacity.textProperty().set(String.valueOf(viewModel.getRoom().getComfortCapacity()));
        lblStatus.textProperty().set(viewModel.isAvailable());
        lvRoomBookings.setItems(viewModel.getBookings());

        //TODO skal måske ikke checkes her
        if (viewModel.getRoom().getComment().isEmpty())
            lblComment.textProperty().set(viewModel.getRoom().getComment());

        if (viewModel.getBookings().size() > 0)
        {
            lvRoomBookings.setManaged(true);
            lblNoBookings.setManaged(false);
        }
        else
        {
            lvRoomBookings.setManaged(false);
            lblNoBookings.setManaged(true);
        }

    }

}
