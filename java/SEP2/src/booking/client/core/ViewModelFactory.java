package booking.client.core;

import booking.client.model.ClientModel;
import booking.client.view.CoordinatorGUI.CoordinatorBookRoomViewModel;
import booking.client.view.CoordinatorGUI.CoordinatorBookingMenuViewModel;
import booking.client.view.login.RegisterViewModel;
import booking.client.view.userGUI.UserBookRoomViewModel;
import booking.client.view.userGUI.UserHomeScreenViewModel;
import booking.shared.objects.Room;
import booking.client.view.roomInfo.RoomInfoViewModel;
import booking.client.view.CoordinatorGUI.CoordinatorHomeScreenViewModel;
import booking.client.view.login.LoginViewModel;

import javax.swing.text.View;

public class ViewModelFactory
{
    private LoginViewModel loginViewModel;
    private UserBookRoomViewModel userBookRoomViewModel;
    private UserHomeScreenViewModel userHomeScreenViewModel;

    private CoordinatorHomeScreenViewModel coordinatorHomeScreenViewModel;
    private CoordinatorBookRoomViewModel coordinatorBookRoomViewModel;
    private CoordinatorBookingMenuViewModel coordinatorBookingMenuViewModel;
    private RegisterViewModel registerViewModel;

    private RoomInfoViewModel roomInfoViewModel;

    public ViewModelFactory()
    {
    }

    public LoginViewModel getLoginViewModel(ViewHandler viewHandler, ClientModel model
    )
    {
        if (loginViewModel == null)
        {
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

    public RoomInfoViewModel getRoomInfoViewModel(ViewHandler viewHandler, ClientModel model, Room room)
    {
        if (roomInfoViewModel == null)
            roomInfoViewModel = new RoomInfoViewModel(viewHandler, model, room);
        return roomInfoViewModel;
    }

    public CoordinatorHomeScreenViewModel getCoordinatorHomeScreenViewModel(ViewHandler viewHandler, ClientModel model)
    {
        if (coordinatorHomeScreenViewModel == null)
            coordinatorHomeScreenViewModel = new CoordinatorHomeScreenViewModel(viewHandler, model);
        return coordinatorHomeScreenViewModel;
    }

    public CoordinatorBookRoomViewModel getCoordinatorBookRoomViewModel(ViewHandler viewHandler, ClientModel model)
    {
        if (coordinatorBookRoomViewModel == null)
            coordinatorBookRoomViewModel = new CoordinatorBookRoomViewModel(viewHandler, model);
        return coordinatorBookRoomViewModel;
    }

    public CoordinatorBookingMenuViewModel getCoordinatorBookingMenuViewModel(ViewHandler viewHandler, ClientModel model)
    {
        if (coordinatorBookingMenuViewModel == null)
            coordinatorBookingMenuViewModel = new CoordinatorBookingMenuViewModel(viewHandler, model);

        return coordinatorBookingMenuViewModel;
    }

    public RegisterViewModel getRegisterViewModel(ViewHandler viewHandler, ClientModel model)
    {
        if (registerViewModel == null)
            registerViewModel = new RegisterViewModel(viewHandler, model);
        return registerViewModel;
    }
}
