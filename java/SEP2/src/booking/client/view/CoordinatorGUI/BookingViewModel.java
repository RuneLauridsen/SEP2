package booking.client.view.CoordinatorGUI;

import booking.client.core.ViewHandler;
import booking.client.model.ClientModel;

public class BookingViewModel
{
    private ViewHandler viewHandler;
    private ClientModel model;

    public void bookRoomAction()
    {
        viewHandler.showCoordinatorBookRoom();
    }
}
