package booking.client.core;

import booking.client.core.ViewHandler;
import booking.client.model.ClientModel;
import booking.client.view.userGUI.UserBookRoomViewModel;
import booking.client.view.userGUI.UserHomeScreenViewModel;
import booking.shared.objects.Room;
import booking.database.Persistence;
import booking.client.view.roomInfo.RoomInfoViewModel;
import booking.client.view.CoordinatorGUI.CoordinatorHomeScreenViewModel;
import booking.client.view.login.LoginViewModel;

public class ViewModelFactory
{
    private LoginViewModel loginViewModel;
    private UserBookRoomViewModel userBookRoomViewModel;
    private UserHomeScreenViewModel userHomeScreenViewModel;

    private CoordinatorHomeScreenViewModel coordinatorHomeScreenViewModel;

    private RoomInfoViewModel roomInfoViewModel;
    public ViewModelFactory()
    {
    }

    public LoginViewModel getLoginViewModel(ViewHandler viewHandler, ClientModel model
    )
    {
        if (loginViewModel == null){
            loginViewModel = new LoginViewModel(viewHandler, model);
        }
        return loginViewModel;
    }

    public UserHomeScreenViewModel getUserHomeScreenViewModel(ViewHandler viewHandler, ClientModel model)
    {
        if (userHomeScreenViewModel == null)
            userHomeScreenViewModel = new UserHomeScreenViewModel(viewHandler, model);
        return userHomeScreenViewModel;
    }
    public UserBookRoomViewModel getUserBookRoomViewModel(ViewHandler viewHandler, ClientModel model)
    {
        if (userBookRoomViewModel == null)
            userBookRoomViewModel = new UserBookRoomViewModel(viewHandler, model);
        return userBookRoomViewModel;
    }

    public RoomInfoViewModel getRoomInfoViewModel (ViewHandler viewHandler, ClientModel model,
        Room room)
    {
        if (roomInfoViewModel == null)
            roomInfoViewModel = new RoomInfoViewModel(viewHandler, model, room);
        return roomInfoViewModel;
    }

    public CoordinatorHomeScreenViewModel getCoordinatorHomescreenViewModel (ViewHandler viewHandler, ClientModel model){
        if (coordinatorHomeScreenViewModel == null)
            coordinatorHomeScreenViewModel = new CoordinatorHomeScreenViewModel(viewHandler, model);
        return coordinatorHomeScreenViewModel;
    }
}
