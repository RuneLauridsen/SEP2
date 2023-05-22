package booking.client.view.CoordinatorGUI;

import booking.client.core.ViewHandler;
import booking.client.model.ClientModel;

public class CoordinatorBookingMenuViewModel
{
    private ViewHandler viewHandler;
    private ClientModel model;

    public CoordinatorBookingMenuViewModel(ViewHandler viewHandler, ClientModel model)
    {
        this.viewHandler = viewHandler;
        this.model = model;
    }

    public void bookRoomAction()
    {
        viewHandler.showCoordinatorBookRoom();
    }
}
